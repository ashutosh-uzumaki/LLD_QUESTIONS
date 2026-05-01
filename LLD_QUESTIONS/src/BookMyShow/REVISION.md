# 🎬 BookMyShow — Complete Revision Guide

---

## 📋 Requirements

### Functional
1. User can browse shows by city
2. User can select seats for a show
3. User can book multiple seats in one booking
4. Multiple payment modes supported
5. Ticket generated per seat after payment
6. Seat temporarily locked during payment

### Non-Functional
1. No two users can book the same seat
2. Highly scalable
3. Seat locked for N minutes during payment — auto release on timeout

### Out of Scope
- Ratings, Reviews
- Seat countdown timer UI
- Admin flow (Theatre/Screen/Seat creation)
- Repository/DB layer

---

## 🗂️ Entities

### Enums
```java
enum ScreenType { IMAX, S2D, S3D }
enum AudioType { DOLBY_DIGITAL, DOLBY_ATMOS, STEREO }
enum SeatTier { SILVER, GOLD, PLATINUM }
enum ShowSeatStatus { AVAILABLE, HELD, BOOKED }
enum BookingStatus { IN_PROGRESS, PAYMENT_PENDING, BOOKED, CANCELLED, FAILED }
```

### Core Entities
```
Movie        → movieId, title, duration, genre
Theatre      → theatreId, name, city, address
Screen       → screenId, theatreId, name, screenType, audioType
Seat         → seatId, screenId, displayName, seatTier
Show         → showId, screenId, movieId, startTime, endTime, price
ShowSeat     → showSeatId, showId, seatId, status, price
User         → userId, name, email, phone
Booking      → bookingId, showId, userId, List<ShowSeat>, bookingStatus, payment, totalAmount
Ticket       → ticketId, bookingId, showId, seatId, amount
SeatLock     → userId, seatId, lockUntil
```

### Payment (Strategy Pattern)
```
Payment (interface) → pay(BigDecimal amount)
CreditCardPayment   → cardNumber, cvv, expiryDate
DebitCardPayment    → cardNumber, cvv, expiryDate
UpiPayment          → upiId, phoneNumber
```

---

## 🏗️ Services

### ShowService
```java
ConcurrentHashMap<Long, List<ShowSeat>> showSeats

addSeat(Long showId, List<ShowSeat> showSeats)
getSeats(Long showId) → List<ShowSeat>
getAvailableSeats(Long showId) → List<ShowSeat>
```

### SeatLockingService
```java
ConcurrentHashMap<Long, SeatLock> seatLocks
ConcurrentHashMap<Long, ShowSeat> showSeatsMap
static final int LOCK_DURATION = 10 // minutes
ScheduledExecutorService scheduledExecutorService

lockSeats(Long userId, List<ShowSeat> showSeats) → boolean
validateLock(Long userId, SeatLock currLock) → boolean
releaseLock(Long seatId, ShowSeat showSeat) → void
shutdown() → void
```

### BookingService
```java
ConcurrentHashMap<Long, List<Booking>> bookings
ShowService showService
SeatLockingService seatLockingService

selectSeats(Long userId, Long showId, List<ShowSeat> selectedSeats) → boolean
createBooking(Long userId, Long showId, List<ShowSeat> selectedSeats) → Booking
makePayment(Long bookingId, Long userId, PaymentMethod paymentMethod) → boolean
cancelBooking(Long bookingId, Long userId) → boolean
```

---

## 🔄 Flow

```
1. GET /movies?city=X          → browse movies
2. GET /movies/{id}/shows      → shows for movie
3. GET /shows/{id}/seats       → available seats
4. selectSeats()               → lock seats (SeatLockingService)
5. createBooking()             → Booking IN_PROGRESS
6. makePayment()               → PAYMENT_PENDING → BOOKED/FAILED
7. Ticket generated            → per seat
```

---

## ⚡ Concurrency Handling

### Why ConcurrentHashMap?
- Node-level locking → only affected show's node locked
- Other shows can be updated in parallel
- Non-blocking reads

### Seat Lock Flow
```
lockSeats() called
  → if BOOKED → return false
  → if HELD → validateLock()
      → different user AND lock valid → return false
      → else → create new lock
  → AVAILABLE → create lock directly
  → set status HELD
  → schedule auto-release after 10 mins
```

### ScheduledExecutorService
```java
scheduledExecutorService.schedule(
    () -> releaseLock(seatId, showSeat),
    LOCK_DURATION,
    TimeUnit.MINUTES
);
```

**Limitations at scale:**
- Thread count limited → use Redis TTL in production
- Server crash → all scheduled tasks lost → Redis TTL survives crash

---

## 🌐 API Design

### 1. Browse Movies
```
GET /movies?city=Ahmedabad

200 OK
{
    "data": [
        { "movieId": 1, "movieName": "King Kong", "genre": "Action", "duration": 120 }
    ]
}
404 Not Found → { "error": "No movies found in your city" }
```

### 2. Get Shows for Movie
```
GET /movies/{movieId}/shows?city=Ahmedabad&date=2024-05-01

200 OK
{
    "data": [
        {
            "showId": 1,
            "theatreName": "PVR",
            "screenType": "IMAX",
            "startTime": "09:00",
            "endTime": "11:00",
            "price": 500
        }
    ]
}
404 Not Found → { "error": "No shows found" }
```

### 3. Get Available Seats
```
GET /shows/{showId}/seats

200 OK
{
    "data": [
        { "showSeatId": 1, "seatName": "A1", "tier": "GOLD", "price": 500, "status": "AVAILABLE" }
    ]
}
404 Not Found → { "error": "Show not found" }
```

### 4. Select & Lock Seats
```
POST /shows/{showId}/seats/lock
{ "userId": 1, "seatIds": [1, 2] }

200 OK → { "message": "Seats locked for 10 minutes" }
409 Conflict → { "error": "Seats already locked/booked" }
```

### 5. Create Booking
```
POST /bookings
{ "userId": 1, "showId": 1, "seatIds": [1, 2] }

201 Created
{
    "bookingId": "abc123",
    "status": "IN_PROGRESS",
    "totalAmount": 1000
}
409 Conflict → { "error": "Seats unavailable" }
```

### 6. Make Payment
```
POST /bookings/{bookingId}/payment
{ "paymentMethod": "UPI", "upiId": "user@upi" }

200 OK → { "status": "BOOKED", "message": "Payment successful" }
402 Payment Required → { "error": "Payment failed" }
404 Not Found → { "error": "Booking not found" }
```

### 7. Cancel Booking
```
DELETE /bookings/{bookingId}
{ "userId": 1 }

200 OK → { "message": "Booking cancelled" }
404 Not Found → { "error": "Booking not found" }
409 Conflict → { "error": "Cannot cancel confirmed booking" }
```

### 8. Get User Bookings
```
GET /bookings?userId=1

200 OK
{
    "data": [
        { "bookingId": "abc123", "showName": "King Kong", "status": "BOOKED", "amount": 1000 }
    ]
}
```

---

## 🎯 Design Patterns Used

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Payment | Multiple payment modes, runtime injection |
| Singleton | Services | Single instance across app |
| Builder | Booking | Complex object creation |

---

## 🔧 SOLID Principles Applied

| Principle | How |
|-----------|-----|
| SRP | Each service has one responsibility |
| OCP | Payment interface — add new method without changing existing |
| LSP | CreditCard/DebitCard/UPI all implement Payment |
| ISP | Payment interface has only one method |
| DIP | BookingService depends on Payment interface, not concrete class |

---

## 🚨 Fault Tolerance

| Failure | Handling |
|---------|---------|
| Payment fails | BookingStatus → FAILED, seats → AVAILABLE, locks released |
| Seat lock timeout | ScheduledExecutorService auto releases |
| Server crash (production) | Redis TTL handles lock expiry |
| Concurrent booking | ConcurrentHashMap + SeatLockingService prevents double booking |

---

## 📊 Scaling Considerations

| Component | Single Node | Distributed |
|-----------|-------------|-------------|
| Seat Lock | ConcurrentHashMap | Redis with TTL |
| Booking Store | ConcurrentHashMap | PostgreSQL/MySQL |
| Payment | Direct call | Payment Gateway + Webhook |
| Notifications | Sync | Kafka + Notification Service |

---

## 🧪 Test Scenarios

### Single User Booking ✅
```
selectSeats() → createBooking() → makePayment() → BOOKED
```

### Concurrent Booking ✅
```
User 1 + User 2 → same seats
→ One succeeds, other gets "Seats not available"
```

### Payment Failure
```
makePayment() throws exception
→ BookingStatus → FAILED
→ ShowSeat → AVAILABLE
→ Locks released
```