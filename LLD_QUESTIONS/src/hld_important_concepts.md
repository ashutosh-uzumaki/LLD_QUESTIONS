# 7 Universal HLD Patterns: Complete Deep-Dive Guide

> **Purpose:** Master system design patterns for HLD interviews at Product/Infrastructure companies
> **Target Roles:** SDE-2/Senior at PhonePe, Paytm, Rubrik, Rippling, Amazon
> **Last Updated:** April 18, 2026

---

## 📋 Table of Contents

1. [Pattern 1: Scaling Reads](#pattern-1-scaling-reads)
2. [Pattern 2: Scaling Writes](#pattern-2-scaling-writes)
3. [Pattern 3: Realtime Updates](#pattern-3-realtime-updates)
4. [Pattern 4: Long-Running Tasks](#pattern-4-long-running-tasks)
5. [Pattern 5: Dealing with Contention](#pattern-5-dealing-with-contention)
6. [Pattern 6: Large Blobs](#pattern-6-large-blobs)
7. [Pattern 7: Multi-Step Processes](#pattern-7-multi-step-processes-distributed-transactions)
8. [Weekend Study Plan](#weekend-study-plan)
9. [Active Recall Template](#active-recall-template)
10. [Quick Reference Card](#quick-reference-card)

---

## Pattern 1: Scaling Reads

**Use Case:** Instagram feed, news portals, product catalogs (read:write ratio 100:1+)

### 🎯 Core Strategies

| Strategy | How It Works | When to Use |
|----------|--------------|-------------|
| **Read Replicas** | Master handles writes; replicas handle reads | Read-heavy, eventual consistency OK |
| **Caching (Multi-layer)** | L1: In-memory (Guava), L2: Distributed (Redis), L3: CDN | Hot data, low-latency requirements |
| **CQRS** | Separate read/write models; denormalized read DB | Complex queries, different read/write scales |
| **Materialized Views** | Pre-computed query results refreshed periodically | Expensive aggregations (analytics dashboards) |
| **CDN Edge Caching** | Static assets cached at edge locations | Global users, media content |

### ⚖️ Key Trade-offs

```
Cache Invalidation vs Freshness:
├─ Write-through: Strong consistency, but higher write latency
├─ Write-back: Low write latency, but risk of data loss on crash
├─ TTL-based: Simple, but stale data possible within TTL window
└─ Cache-aside: Flexible, but cache miss penalty on first read

Read Replicas Trade-off:
├─ More replicas = better read scalability
├─ BUT: Replication lag = stale reads (eventual consistency)
└─ Solution: Route critical reads (e.g., "my order status") to master
```

### 🛠️ Implementation Checklist

```yaml
Caching Layer:
  - Cache key design: namespace:user_id:resource:id
  - Cache stampede protection: probabilistic early expiration
  - Fallback strategy: cache miss → DB → populate cache async

Read Replicas:
  - Connection pooling with read/write routing
  - Health checks + automatic failover
  - Monitor replication lag (alert if > 5s)

CQRS:
  - Event sourcing for write side
  - Separate read-optimized schema (denormalized)
  - Sync mechanism: CDC (Debezium) or event bus
```

### 💡 Practice Question

> "Design Instagram's newsfeed for 100M DAU. How do you handle the fan-out problem for celebrity accounts?"

### 📚 Resources for Scaling Reads

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | System Design Patterns for Scaling Reads | [GyanByte](https://gyanbyte.com/software-design/system-design-patterns-scaling-reads/) | Clear breakdown of caching strategies with diagrams |
| 📖 Article | Cache Invalidation Strategies | [DesignGurus](https://www.designgurus.io/blog/cache-invalidation-strategies) | Practical patterns with code snippets |
| 📖 Book Chapter | Replication (Ch. 5) | *Designing Data-Intensive Applications* | Deep dive into consistency models |
| 📖 Book Chapter | Caching (Ch. 6) | *Designing Data-Intensive Applications* | When and how to cache effectively |
| 🎥 Video | Caching Strategies for System Design | [Gaurav Sen](https://youtu.be/U3X4XfKdL4Y) | Indian context, interview-focused |
| 🎥 Video | Read Replicas & CQRS | [ByteByteGo](https://youtu.be/06B850GcGWE) | Visual explanation with trade-offs |
| 🧪 Practice | Design Twitter Newsfeed | [LeetCode Discuss](https://leetcode.com/discuss/interview-question/system-design/124656/) | Real interview question with solutions |

---

## Pattern 2: Scaling Writes

**Use Case:** Analytics pipelines, IoT telemetry, logging systems

### 🎯 Core Strategies

| Strategy | How It Works | When to Use |
|----------|--------------|-------------|
| **Sharding** | Partition data by key (user_id, geo, time) | Single DB bottleneck, horizontal scale needed |
| **Write Buffering** | Batch writes in memory/queue before DB flush | High-frequency small writes (metrics, logs) |
| **Async Writes** | Acknowledge client → queue → process async | Non-critical data, eventual consistency OK |
| **Time-Series DBs** | Optimized for append-only, time-partitioned data | IoT, monitoring, financial ticks |

### ⚖️ Key Trade-offs

```
Sharding Key Selection:
├─ By user_id: Even distribution, but user data scattered
├─ By time: Good for range queries, but hot partitions for recent data
└─ By geo: Low-latency for regional users, but complex cross-region queries

Write Buffering Trade-off:
├─ Larger batches = better DB throughput
├─ BUT: Higher memory usage + data loss risk on crash
└─ Mitigation: Write-ahead log (WAL) + periodic flush to durable storage
```

### 🛠️ Implementation Checklist

```yaml
Sharding:
  - Consistent hashing for minimal rebalancing
  - Shard mapping service (ZooKeeper/Etcd)
  - Cross-shard query strategy: scatter-gather or denormalize

Write Buffering:
  - In-memory ring buffer with backpressure
  - Flush triggers: size-based OR time-based OR memory pressure
  - Idempotent writes for retry safety

Async Pipeline:
  - Queue choice: Kafka (replay) vs SQS (simplicity)
  - Dead letter queue for failed writes
  - Monitoring: queue depth, processing lag
```

### 💡 Practice Question

> "Design a system to ingest 1M IoT sensor readings/second. How do you ensure no data loss while maintaining low latency?"

### 📚 Resources for Scaling Writes

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | Guide for Designing Highly Scalable Systems | [GeeksforGeeks](https://www.geeksforgeeks.org/system-design/guide-for-designing-highly-scalable-systems/) | Comprehensive overview with diagrams |
| 📖 Article | Database Sharding Patterns | [Martin Fowler](https://martinfowler.com/articles/scaling.html) | Enterprise-grade sharding strategies |
| 📖 Article | Message Queue Comparison 2025 | [Youngju.dev](https://www.youngju.dev/blog/culture/2026-03-22-message-queue-kafka-rabbitmq-sqs-comparison-2025.en) | Up-to-date queue comparison with benchmarks |
| 📖 Book Chapter | Partitioning (Ch. 6) | *Designing Data-Intensive Applications* | Deep fundamentals on sharding & routing |
| 🎥 Video | Sharding & Partitioning | [Gaurav Sen](https://youtu.be/8MslYp7jRfk) | Simple explanation with real examples |
| 🎥 Video | Write-Heavy System Design | [ByteByteGo](https://youtu.be/V02h8R8Y0jE) | Focus on buffering & async patterns |
| 🧪 Practice | Design a Metrics Collection System | [Educative](https://www.educative.io/courses/grokking-the-system-design-interview) | Step-by-step solution with trade-offs |

---

## Pattern 3: Realtime Updates

**Use Case:** Chat, live scores, collaborative editing, notifications

### 🎯 Protocol Comparison

| Protocol | Direction | Latency | Auto-reconnect | Best For |
|----------|-----------|---------|----------------|----------|
| **Short Polling** | Client → Server | High (interval) | Manual | Low-frequency dashboards |
| **Long Polling** | Client → Server | Low | Manual | Fallback, legacy support |
| **SSE** | Server → Client | Low | ✅ Built-in | Live feeds, notifications |
| **WebSocket** | Bidirectional | Lowest | Manual | Chat, gaming, collaboration |

### ⚖️ Key Trade-offs

```
WebSocket vs SSE:
├─ WebSocket: Full-duplex, binary support, but complex connection mgmt
├─ SSE: Simpler, HTTP-native, auto-reconnect, but server→client only
└─ Decision: Need client→server frequent? → WebSocket. Else → SSE.

Scaling Realtime:
├─ Single server: 50K-100K concurrent connections (file descriptor limits)
├─ Multi-server: Need pub/sub backbone (Redis/Kafka) to bridge servers
└─ Sticky sessions vs Pub/Sub: Pub/Sub distributes load better but adds complexity
```

### 🛠️ Implementation Checklist

```yaml
Connection Management:
  - Heartbeat/ping-pong to detect dead connections
  - Exponential backoff reconnection on client
  - Graceful shutdown: drain connections before deploy

Pub/Sub Backbone:
  - Redis Pub/Sub: Low latency, fire-and-forget (good for chat)
  - Kafka: Durable, replayable (good for audit trails)
  - Message format: Include event_id for idempotency

Presence System:
  - Redis ZSET with TTL for online users
  - Fan-out optimization: Notify only friends/subscribers, not all users
```

### 💡 Practice Question

> "Design WhatsApp's 'last seen' and typing indicators. How do you scale presence detection for 1B users?"

### 📚 Resources for Realtime Updates

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | Real-Time Systems: WebSockets, SSE & Polling | [GyanByte](https://www.gyanbyte.com/crash-courses/system-design-masterclass/10-real-time-systems/) | Protocol comparison with use cases |
| 📖 Article | WebSockets vs SSE vs Long Polling | [AbstractAlgorithms](https://www.abstractalgorithms.dev/system-design-real-time-communication) | Decision framework with code examples |
| 📖 Article | Scaling WebSocket Connections | [Socket.io Docs](https://socket.io/docs/v4/using-multiple-nodes/) | Practical scaling patterns |
| 📖 Book Chapter | Data Streams (Ch. 11) | *Designing Data-Intensive Applications* | Foundations of stream processing |
| 🎥 Video | Real-Time Communication Patterns | [Gaurav Sen](https://youtu.be/6VhmZ6p0J7k) | Interview-focused with trade-offs |
| 🎥 Video | Building Chat Systems | [ByteByteGo](https://youtu.be/8m5VZKv6c8g) | End-to-end chat architecture |
| 🧪 Practice | Design a Notification System | [LeetCode Discuss](https://leetcode.com/discuss/interview-question/system-design/369533/) | Real question with multiple approaches |
| 🔧 Tool | Socket.io | [socket.io](https://socket.io) | Production-ready WebSocket library |
| 🔧 Tool | Pusher / Ably | [pusher.com](https://pusher.com) | Managed realtime infrastructure |

---

## Pattern 4: Long-Running Tasks

**Use Case:** Video transcoding, report generation, ML inference, email campaigns

### 🎯 Architecture Pattern

```
Client → API → Queue → Worker Pool → Storage → Notification
                ↑
          (Retry + DLQ + Monitoring)
```

### 🎯 Queue Selection Guide

| Queue | Throughput | Persistence | Best For |
|-------|------------|-------------|----------|
| **Redis Streams** | 100K-500K msgs/s | In-memory (+AOF) | Lightweight tasks, low latency |
| **RabbitMQ** | 50K-100K msgs/s | Memory + Disk | Complex routing, RPC patterns |
| **Kafka** | 500K-1M+ msgs/s | Disk (retention) | Event streaming, replay, audit |
| **SQS** | 3K-30K msgs/s | AWS managed | Serverless, AWS-native workloads |

### ⚖️ Key Trade-offs

```
Queue Choice:
├─ Kafka: High throughput + replayability, but operational complexity
├─ RabbitMQ: Flexible routing, but lower throughput than Kafka
├─ SQS: Zero ops, but limited features (no complex routing)
└─ Redis: Ultra-low latency, but durability concerns without AOF

Retry Strategy:
├─ Exponential backoff: Prevent thundering herd on recovery
├─ Max retries + DLQ: Avoid infinite loops on poison messages
└─ Idempotency: Ensure retries don't cause duplicate side effects
```

### 🛠️ Implementation Checklist

```yaml
Task Definition:
  - Unique task_id for idempotency
  - Payload: minimal data + S3 references for large blobs
  - Priority queue support for urgent tasks

Worker Design:
  - Horizontal scaling: Stateless workers + queue partitioning
  - Resource isolation: CPU-bound vs I/O-bound tasks in separate pools
  - Progress tracking: Update task status in DB for client polling

Monitoring:
  - Queue depth alerts (scale workers if backlog grows)
  - Task duration percentiles (detect slow workers)
  - DLQ inspection dashboard (manual retry/analysis)
```

### 💡 Practice Question

> "Design YouTube's video upload pipeline: from user upload to HD/SD/360p transcoding + thumbnail generation. How do you handle failures?"

### 📚 Resources for Long-Running Tasks

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | Message Queue Showdown 2025 | [Youngju.dev](https://www.youngju.dev/blog/culture/2026-03-22-message-queue-kafka-rabbitmq-sqs-comparison-2025.en) | Detailed comparison with benchmarks |
| 📖 Article | Saga Pattern for Distributed Transactions | [GeeksforGeeks](https://www.geeksforgeeks.org/system-design/saga-design-pattern/) | Clear explanation with compensation examples |
| 📖 Article | Building Resilient Background Jobs | [Martin Fowler](https://martinfowler.com/articles/patterns-of-distributed-systems/) | Enterprise patterns for reliability |
| 📖 Book Chapter | Batch Processing (Ch. 10) | *Designing Data-Intensive Applications* | Foundations of async processing |
| 🎥 Video | Background Job Processing | [Gaurav Sen](https://youtu.be/8MslYp7jRfk) | Queue patterns with real examples |
| 🎥 Video | Designing Async Systems | [ByteByteGo](https://youtu.be/V02h8R8Y0jE) | End-to-end async architecture |
| 🧪 Practice | Design a Video Transcoding Service | [Educative](https://www.educative.io/courses/grokking-the-system-design-interview) | Step-by-step with failure handling |
| 🔧 Tool | Celery (Python) | [docs.celeryq.dev](https://docs.celeryq.dev) | Production-ready task queue |
| 🔧 Tool | Bull (Node.js) | [github.com/OptimalBits/bull](https://github.com/OptimalBits/bull) | Redis-based job queue |
| 🔧 Tool | AWS Step Functions | [aws.amazon.com/step-functions](https://aws.amazon.com/step-functions) | Managed workflow orchestration |

---

## Pattern 5: Dealing with Contention

**Use Case:** Ticket booking, flash sales, auctions, seat selection

### 🎯 Concurrency Control Strategies

| Strategy | How It Works | Pros | Cons |
|----------|--------------|------|------|
| **Pessimistic Locking** | Lock row before read-modify-write | Strong consistency, no lost updates | Lower throughput, deadlock risk |
| **Optimistic Locking** | Version check on write (CAS) | Higher throughput, no locks | Retry logic needed, wasted work |
| **Queue Serialization** | Single-threaded queue per resource | Simple, no DB locks | Latency, single point of failure |
| **Distributed Locks** | Redis/ZooKeeper lock across services | Cross-service coordination | Complexity, network partitions |

### ⚖️ Key Trade-offs

```
Optimistic vs Pessimistic:
├─ High contention (flash sale): Pessimistic avoids retry storms
├─ Low contention (profile updates): Optimistic gives better throughput
└─ Hybrid: Optimistic by default, fallback to pessimistic after N retries

Distributed Locks Caveats:
├─ Lease-based locks (auto-expire) to avoid deadlocks on crash
├─ Watchdog pattern: Extend lock if task still running
└─ Avoid fine-grained locks: Lock "event-123" not "user-456-seat-789"
```

### 🛠️ Implementation Checklist

```yaml
Database Level:
  - Optimistic: Add version/timestamp column, check on UPDATE
  - Pessimistic: SELECT ... FOR UPDATE (with timeout)
  - Indexing: Ensure lock queries use indexed columns

Application Level:
  - Retry with jitter: Avoid synchronized retries causing thundering herd
  - Circuit breaker: Stop retrying if downstream is overloaded
  - Fallback: "Join waitlist" if contention too high

Testing:
  - Chaos testing: Simulate network partitions during lock acquisition
  - Load testing: Measure throughput under contention scenarios
```

### 💡 Practice Question

> "Design BookMyShow's seat booking system for a concert with 10K users trying to book simultaneously. How do you prevent double-booking?"

### 📚 Resources for Dealing with Contention

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | Pessimistic vs Optimistic Locking | [Modern Treasury](https://www.moderntreasury.com/learn/pessimistic-locking-vs-optimistic-locking) | Clear comparison with real-world examples |
| 📖 Article | Concurrency Control in Distributed Systems | [GeeksforGeeks](https://www.geeksforgeeks.org/operating-systems/concurrency-control-in-distributed-transactions/) | Academic foundation with practical tips |
| 📖 Article | Distributed Locks with Redis | [Redis Labs](https://redis.io/docs/manual/patterns/distributed-locks/) | Official guide with Redlock algorithm |
| 📖 Book Chapter | Transactions (Ch. 7) | *Designing Data-Intensive Applications* | Deep dive into isolation levels & locks |
| 🎥 Video | Handling High Contention | [Gaurav Sen](https://youtu.be/8MslYp7jRfk) | Interview patterns with trade-offs |
| 🎥 Video | Flash Sale System Design | [ByteByteGo](https://youtu.be/V02h8R8Y0jE) | Real-world contention scenario |
| 🧪 Practice | Design a Ticket Booking System | [LeetCode Discuss](https://leetcode.com/discuss/interview-question/system-design/369533/) | Multiple approaches with pros/cons |
| 🔧 Tool | Redis Redlock | [redis.io](https://redis.io/topics/distlock) | Distributed lock implementation |
| 🔧 Tool | ZooKeeper Locks | [zookeeper.apache.org](https://zookeeper.apache.org/doc/current/recipes.html#sc_recipes_locks) | CP-system friendly locking |

---

## Pattern 6: Large Blobs

**Use Case:** Image/video uploads, file storage, backup systems

### 🎯 Architecture Pattern

```
Client → Presigned URL → Object Storage (S3) → CDN → Metadata DB
                    ↑
              (Virus Scan / Transcoding async)
```

### 🎯 Key Strategies

| Strategy | How It Works | When to Use |
|----------|--------------|-------------|
| **Presigned URLs** | Client uploads directly to S3 with temporary credentials | Offload bandwidth from app servers |
| **Multipart Upload** | Split large files into chunks, upload in parallel | Files >100MB, unreliable networks |
| **Resumable Uploads** | Track uploaded chunks, resume from last checkpoint | Mobile apps, spotty connectivity |
| **CDN + Origin Shield** | Cache at edge, single origin fetch for cache miss | Global users, popular content |

### ⚖️ Key Trade-offs

```
Direct Upload vs Proxy:
├─ Direct (presigned): Scalable, but client needs S3 permissions logic
└─ Proxy via app server: More control (virus scan before store), but bandwidth bottleneck

Consistency vs Availability:
├─ Upload → Process → Notify: Strong consistency, but higher latency
└─ Upload → Notify → Async Process: Low latency, but user sees "processing" state

CDN Invalidation:
├─ Versioned URLs (image_v2.jpg): Simple, but storage grows
├─ Purge API: Immediate, but rate-limited by CDN provider
└─ TTL-based: Automatic, but stale content possible within TTL
```

### 🛠️ Implementation Checklist

```yaml
Upload Flow:
  - Validate file type/size on client + server
  - Generate unique object key: user_id/timestamp/uuid.ext
  - Store metadata separately: DB record with S3 key, size, checksum

Processing Pipeline:
  - Event-driven: S3 event → SQS → worker (transcode/thumbnail)
  - Progress tracking: Update metadata DB with processing status
  - Idempotent workers: Handle duplicate S3 events safely

Security:
  - Presigned URL expiry: 5-15 mins max
  - Virus scanning: Async scan post-upload, quarantine if malicious
  - Access control: Bucket policies + IAM roles, not hardcoded credentials
```

### 💡 Practice Question

> "Design Instagram's photo upload system: from mobile app to CDN-delivered, resized images. How do you handle viral posts?"

### 📚 Resources for Large Blobs

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | Large Object Storage Patterns | [AWS Docs](https://docs.aws.amazon.com/AmazonS3/latest/userguide/large-objects.html) | Official guide with multipart upload details |
| 📖 Article | CDN Best Practices | [Cloudflare Blog](https://blog.cloudflare.com/advanced-cdn-configuration/) | Real-world CDN optimization tips |
| 📖 Article | Presigned URLs Deep Dive | [AWS Blog](https://aws.amazon.com/blogs/security/using-presigned-url-to-share-private-s3-objects/) | Security-focused implementation guide |
| 📖 Book Chapter | Storage & Retrieval (Ch. 3) | *Designing Data-Intensive Applications* | Foundations of blob storage systems |
| 🎥 Video | Designing File Upload Systems | [Gaurav Sen](https://youtu.be/8MslYp7jRfk) | End-to-end upload architecture |
| 🎥 Video | CDN & Media Delivery | [ByteByteGo](https://youtu.be/V02h8R8Y0jE) | Global delivery patterns |
| 🧪 Practice | Design a Photo Sharing App | [Educative](https://www.educative.io/courses/grokking-the-system-design-interview) | Step-by-step with scaling considerations |
| 🔧 Tool | AWS S3 Multipart Upload | [aws.amazon.com/s3](https://aws.amazon.com/s3) | Production-ready blob storage |
| 🔧 Tool | Cloudflare R2 | [cloudflare.com/r2](https://www.cloudflare.com/r2) | S3-compatible, egress-free storage |
| 🔧 Tool | Uppy / Tus | [uppy.io](https://uppy.io) | Resumable upload client library |

---

## Pattern 7: Multi-Step Processes (Distributed Transactions)

**Use Case:** E-commerce checkout, travel booking, payment workflows

### 🎯 Transaction Patterns

| Pattern | How It Works | Consistency | Best For |
|---------|--------------|-------------|----------|
| **2PC (Two-Phase Commit)** | Coordinator prepares all participants → commit/rollback | Strong (ACID) | Small-scale, critical financial ops |
| **Saga Pattern** | Sequence of local transactions + compensating actions | Eventual | Microservices, long-running workflows |
| **Eventual Consistency + Reconciliation** | Async updates + periodic audit jobs | Eventual | High-scale, non-critical data sync |

### ⚖️ Key Trade-offs

```
Saga: Choreography vs Orchestration:
├─ Choreography: Services emit events, others react → Loose coupling, hard to debug
└─ Orchestration: Central coordinator directs flow → Easier to reason, single point of failure

Compensation Design:
├─ Must be idempotent: Retry-safe compensations
├─ Partial compensation: Handle cases where compensation itself fails
└─ Human-in-the-loop: For unrecoverable failures, alert ops team

2PC Caveats:
├─ Blocking: Participants hold locks during prepare phase
├─ Coordinator failure: Can leave transactions in-doubt
└─ Avoid for high-scale: Use only when strong consistency is non-negotiable
```

### 🛠️ Implementation Checklist

```yaml
Saga Implementation:
  - Define forward + compensation logic for each step
  - Store saga state in DB: current_step, context_data, status
  - Timeout handling: Auto-trigger compensation if step stalls

Idempotency:
  - Unique request_id per operation
  - Deduplication table: request_id → result cache
  - Idempotent DB operations: UPSERT instead of INSERT

Monitoring:
  - Saga execution time percentiles
  - Compensation failure rate alerts
  - Dead-letter sagas dashboard for manual intervention
```

### 💡 Practice Question

> "Design an e-commerce checkout: cart → payment → inventory → shipping. How do you handle payment success but inventory failure?"

### 📚 Resources for Multi-Step Processes

| Type | Resource | Link | Why It's Good |
|------|----------|------|---------------|
| 📖 Article | SAGA Design Pattern | [GeeksforGeeks](https://www.geeksforgeeks.org/system-design/saga-design-pattern/) | Clear explanation with compensation examples |
| 📖 Article | Distributed Transactions: 2PC vs Saga | [DEV Community](https://dev.to/smiah/distributed-transactions-2pc-vs-3pc-vs-saga-5c34) | Practical comparison with code snippets |
| 📖 Article | Building Reliable Workflows | [Martin Fowler](https://martinfowler.com/articles/patterns-of-distributed-systems/) | Enterprise patterns for orchestration |
| 📖 Book Chapter | Distributed Transactions (Ch. 9) | *Designing Data-Intensive Applications* | Deep fundamentals on consistency models |
| 🎥 Video | Saga Pattern Explained | [Gaurav Sen](https://youtu.be/8MslYp7jRfk) | Interview-focused with trade-offs |
| 🎥 Video | Designing E-commerce Checkout | [ByteByteGo](https://youtu.be/V02h8R8Y0jE) | End-to-end workflow with failure handling |
| 🧪 Practice | Design a Payment System | [LeetCode Discuss](https://leetcode.com/discuss/interview-question/system-design/369533/) | Real question with multiple approaches |
| 🔧 Tool | Temporal.io | [temporal.io](https://temporal.io) | Code-first workflow orchestration |
| 🔧 Tool | AWS Step Functions | [aws.amazon.com/step-functions](https://aws.amazon.com/step-functions) | Managed saga/orchestration service |
| 🔧 Tool | Camunda | [camunda.com](https://camunda.com) | BPMN-based workflow engine |

---

## Weekend Study Plan

### Week 1

| Day | Time | Pattern | Activity | Deliverable |
|-----|------|---------|----------|-------------|
| Saturday | 10 AM | Scaling Reads + Writes | Design Instagram feed + analytics pipeline | GitHub markdown with UML + trade-off matrix |
| Sunday | 3 PM | Realtime + Long-Running | Design WhatsApp chat + video processing | Sequence diagrams + queue selection rationale |

### Week 2

| Day | Time | Pattern | Activity | Deliverable |
|-----|------|---------|----------|-------------|
| Saturday | 10 AM | Contention + Large Blobs | Design BookMyShow + Instagram upload | Locking strategy doc + presigned URL flow |
| Sunday | 3 PM | Multi-Step Processes | Design e-commerce checkout end-to-end | Saga state machine + compensation logic |

---

## Active Recall Template

Use this template during weekday revision (15 mins per pattern):

```markdown
## Pattern: [Name]

### 1. Core Problem
[What problem does this pattern solve?]

### 2. 3 Key Strategies
1. [Strategy 1]
2. [Strategy 2]
3. [Strategy 3]

### 3. Trade-off I'd Explain in Interview
[One key trade-off with pros/cons]

### 4. One Implementation Gotcha
[Common mistake or edge case]

### 5. Related Pattern
[Which other pattern does this connect to?]
```

---

## Quick Reference Card

### Pattern Selection Flowchart

```
Is it read-heavy?            → Scaling Reads (Cache, Replicas, CDN)
Is it write-heavy?           → Scaling Writes (Sharding, Queue, Buffering)
Need instant updates?        → Realtime (WebSocket/SSE + Pub/Sub)
Takes >5 seconds?            → Long-Running (Queue + Workers)
Multiple users same resource? → Contention (Locking strategies)
Large files/media?           → Large Blobs (Presigned URLs + CDN)
Multiple services involved?  → Multi-Step (Saga/2PC)
```

### Common Interview Phrases

✅ **DO SAY:**
- "Given the read:write ratio of 100:1, I'd prioritize read scaling with..."
- "The trade-off here is between consistency and availability. For this use case..."
- "I'd start with a simple solution and scale it as needed..."
- "To handle failures, I'd implement retry logic with exponential backoff..."

❌ **AVOID:**
- "I'd use Kubernetes and microservices for everything"
- "Let's make everything strongly consistent"
- "We don't need to worry about failures"
- Jumping to specific technologies without discussing trade-offs

---

> **Pro Tip:** Keep this file open during mock interviews. Before answering, quickly scan the relevant pattern section to ensure you're covering trade-offs explicitly — exactly what interviewers at Rubrik, PhonePe, and Amazon look for.

---

**Version:** 1.0
**Created:** April 18, 2026
**Maintained by:** Ashutosh

**Good luck with your HLD interviews! 🚀**