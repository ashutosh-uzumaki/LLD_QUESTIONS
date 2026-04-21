# Rate Limiter — LLD Revision Guide

---

## 1. Problem Statement
Design a thread-safe Rate Limiter that controls how many requests a client can make in a given time window.

**Core API:**
```java
boolean isAllowed(String clientId);
```
- `clientId` is generic — can be userId, IP, phone number, API key
- Configurable limit and window per client
- Pluggable algorithm via Strategy pattern

---

## 2. Entities & Responsibilities

| Class | Responsibility |
|---|---|
| `RateLimiter` | Singleton orchestrator. Holds configMap + strategy. |
| `ClientConfig` | Immutable config — maxRequests, windowSizeMs. Fail-fast validation. |
| `RateLimiterStrategy` | Interface — `isAllowed(clientId, config)` |
| `TokenBucketStrategy` | Token Bucket implementation. Owns its own stateMap. |
| `TokenBucketState` | Nested static private class — tokens, lastRefillTime, ReentrantLock |

---

## 3. Algorithms

### Token Bucket ✅ (Implemented)
- Each client has a bucket of N tokens
- Each request consumes 1 token
- Tokens refill proportionally over time (lazy refill on each request)
- **Handles burst traffic** — unused tokens accumulate

**Refill formula:**
```
elapsed = now - lastRefillTime
tokensToAdd = (elapsed / windowSizeMs) * maxRequests
tokens = min(capacity, tokens + tokensToAdd)
lastRefillTime updated only if tokensToAdd > 0
```

**Pros:** Handles bursts, smooth traffic  
**Cons:** Slightly complex implementation

---

### Fixed Window Counter (Theory)
- Divide time into fixed windows (e.g. every 60s)
- Count requests per window, reset when window expires
- **Problem:** Boundary spike — client can send 2x requests at window boundary

**Pros:** Simple, memory efficient  
**Cons:** Boundary spike vulnerability

---

### Sliding Window Log (Theory)
- Store timestamp of every request in a log
- On each request, remove timestamps older than window
- Count remaining timestamps — if > limit, reject
- **Most accurate** — no boundary spike

**Pros:** Precise  
**Cons:** High memory (stores every timestamp)

---

### Leaky Bucket (Theory)
- Requests enter a queue, processed at fixed rate
- Excess requests dropped
- Smooths out traffic bursts

**Pros:** Smooth output rate  
**Cons:** Queued requests add latency

---

## 4. Key Design Decisions

| Decision | Choice | Reason |
|---|---|---|
| Algorithm pluggability | Strategy pattern | OCP — add new algorithm without changing existing code |
| State ownership | Each strategy owns its stateMap | OCP win — strategies are self-contained |
| State per client | Lazy init via `computeIfAbsent` | No upfront registration needed |
| Singleton | Bill Pugh (Holder class) | Thread-safe, lazy, no synchronized overhead |
| Config | Immutable `ClientConfig` | Thread-safe by design, fail-fast validation |
| Default config | `configMap.getOrDefault(clientId, defaultConfig)` | No need to register every user/IP |
| Refill approach | Lazy (on-demand in isAllowed) | No background threads, no wasted refills for inactive clients |

---

## 5. Thread Safety

### Two-level locking strategy:
```
ConcurrentHashMap  →  protects map operations (which client → which state)
ReentrantLock      →  protects state field operations (tokens, lastRefillTime)
```

### Why both?
- `ConcurrentHashMap` only protects get/put on the map
- Once state reference is fetched, field modifications are outside map's protection
- Two threads for same client could both decrement tokens without per-client lock → double spend!

### Why `computeIfAbsent` over manual null check?
```java
// UNSAFE — race condition
if(stateMap.get(clientId) == null) {
    stateMap.put(clientId, new State()); // two threads can both enter here!
}

// SAFE — atomic check + create + put
stateMap.computeIfAbsent(clientId, k -> new State());
```

### Why no `volatile` on tokens/lastRefillTime?
`ReentrantLock.unlock()` establishes **happens-before** with subsequent `lock()` — JMM guarantees visibility. `volatile` is redundant.

---

## 6. API Design

```
POST /clients
Body: { "clientId": "A", "maxRequests": 100, "windowSizeMs": 60000 }
Response: 201 Created

POST /clients/{clientId}/check
Response 200 (allowed):
  Headers:
    X-RateLimit-Remaining: 3
    X-RateLimit-Limit: 10
    X-RateLimit-Reset: 1620000000

Response 429 (blocked):
  Headers:
    X-RateLimit-Remaining: 0
    Retry-After: 30
  Body: { "error": "Too Many Requests" }
```

**Retry-After calculation:**
```java
retryAfter = (lastRefillTime + windowSizeMs - System.currentTimeMillis()) / 1000
```

**Why POST for check?** — Modifies state (consumes token). GET = read-only, no side effects.

---

## 7. Scaling — Single Node → Distributed

### Problem with single node:
- Multiple server instances behind load balancer
- Each has its own in-memory state
- Client sends 10 requests across 3 servers → each server thinks limit not reached → rate limiting broken!

### Solution: Redis as shared state store
- Move stateMap out of memory into Redis
- All nodes read/write same Redis instance
- Key: `rate_limit:{clientId}:{windowStart}` with TTL = windowSizeMs

### Why Redis?
- In-memory → microsecond latency
- Single-threaded → naturally atomic operations
- Supports key expiry → auto cleanup
- Atomic commands → INCR, EXPIRE

### Lua Scripts for atomicity:
```lua
-- INCR + EXPIRE must be atomic — if server crashes between them, key never expires
local count = redis.call('INCR', KEYS[1])
if count == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[1])
end
return count
```

---

## 8. Fault Tolerance

### Fail-open vs Fail-closed (when Redis goes down):

| | Fail-Open | Fail-Closed |
|---|---|---|
| Behavior | Allow all requests | Block all requests |
| Use case | Content APIs (Netflix) | Payment APIs (Razorpay) |
| Risk | Abuse during outage | Legitimate users blocked |

### Circuit Breaker pattern (Resilience4j):
```
CLOSED → requests go to Redis normally
  ↓ (too many failures)
OPEN → fail fast, apply fallback immediately (no Redis timeout latency)
  ↓ (after timeout)
HALF-OPEN → test if Redis recovered
  ↓ (success → CLOSED, failure → OPEN)
```
Without Circuit Breaker → every request waits for Redis timeout → latency spike for all users.

---

## 9. CAP Theorem

Rate Limiter = **AP system** (Availability + Partition Tolerance)

- During network partition, nodes may have slightly different token counts
- A few extra requests may slip through → acceptable
- Blocking legitimate users is worse than slightly exceeding the limit

**Exception:** Payment APIs → CP (Consistency + Partition Tolerance)
- During partition, reject requests rather than risk duplicate payments

---

## 10. Design Patterns & SOLID

### Patterns used:
| Pattern | Where |
|---|---|
| Strategy | Pluggable algorithms via `RateLimiterStrategy` interface |
| Singleton | `RateLimiter` — one instance system-wide (Bill Pugh) |
| Lazy Initialization | `computeIfAbsent` — state created on first request |
| Immutable Object | `ClientConfig` — final fields, no setters, fail-fast validation |

### SOLID:
- **S** — Each class has one responsibility. `TokenBucketStrategy` only does token bucket logic. `ClientConfig` only holds config.
- **O** — New algorithm = new class implementing `RateLimiterStrategy`. Zero changes to existing code.
- **D** — `RateLimiter` depends on `RateLimiterStrategy` interface, not `TokenBucketStrategy` directly.

---

## 11. Production Considerations (Mention in Interview)

- **API Gateway rate limiting** (Kong, AWS API Gateway, Nginx) — most common in production, language agnostic
- **Per-service vs centralized** — Sidecar pattern in microservices (Istio)
- **Background refill thread** — more accurate than lazy refill for high precision needs
- **Spring @Autowired** — inject strategy via IoC instead of manual Singleton
- **Default config** — `getOrDefault()` so every user/IP gets rate limited without explicit registration

---

## 12. Interview One-Liners

> "clientId is a generic String — caller decides if it's userId, IP, phone number, or API key."

> "I used lazy refill inside isAllowed() for simplicity. In production I'd consider a background scheduler for more accurate refill intervals."

> "ConcurrentHashMap protects map operations. ReentrantLock protects field-level state. Two different levels of protection."

> "Rate limiters are AP systems — a few extra requests slipping through during a partition is acceptable. Blocking legitimate users is worse."

> "In production I'd implement rate limiting at the API Gateway level — centralized, language agnostic, no overhead on individual services."