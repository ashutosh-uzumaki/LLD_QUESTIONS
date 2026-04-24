# 🎬 BookMyShow LLD — Quick Revision Guide

---

## 🧠 Core Idea

Design a system to:

* Browse shows
* Select seats
* Lock seats temporarily
* Complete payment
* Generate tickets

---

# 🧱 1. Entities (Nouns)

## 🌍 Physical Layer (Static)

* Location
* Theatre
* Screen
* Seat

## 🎬 Runtime Layer (Dynamic)

* Show
* ShowSeat

## 🔒 Locking Layer

* SeatLock

## 🎟️ Transaction Layer

* Booking

## 🎫 Output Layer

* Ticket

---

# 🔗 2. Relationships (Text UML)

```
Location → Theatre (1:N)
Theatre → Screen (1:N)
Screen → Seat (1:N)

Show → Screen (N:1)
Show → ShowSeat (1:N)
ShowSeat → Seat (N:1)

ShowSeat → SeatLock (1:1 temporary)

Booking → User (N:1)
Booking → ShowSeat (1:N)

Booking → Ticket (1:N)
Ticket → ShowSeat (1:1)
```

---

# 🧠 3. Key Concepts

## ❗ Seat vs ShowSeat

* Seat = static (A1, A2)
* ShowSeat = dynamic (status per show)

👉 Status MUST be in ShowSeat

---

## 🔒 SeatLock (Temporary Reservation)

### Fields:

* showSeatId
* userId
* expiryTime
* createdAt (optional)

---

## 🎟️ Booking (Transaction)

### Fields:

* bookingId
* userId
* showId
* List<showSeatIds>
* status (PENDING, CONFIRMED, FAILED, CANCELLED)
* totalAmount
* createdAt

---

## 🎫 Ticket (Per-seat Entry)

### Fields:

* ticketId
* bookingId
* showSeatId
* status (ACTIVE, USED, CANCELLED)
* qrCode

---

# 🔥 4. Why ShowSeat (CRITICAL)

👉 Same seat can be:

* BOOKED in one show
* AVAILABLE in another

❌ Seat.status → WRONG
✅ ShowSeat.status → CORRECT

---

# 🔒 5. Seat Locking Flow

```
User selects seats
→ create SeatLock

Seat appears unavailable

→ Payment success
   → mark ShowSeat = BOOKED
   → delete SeatLock
   → create Booking
   → generate Tickets

→ Timeout / failure
   → delete SeatLock
```

---

# ⚠️ 6. Concurrency

## Problem:

Two users select same seat

## Solution:

* Use SeatLock (DB / Redis)
* Ensure atomic operation

### Options:

* DB conditional update
* Unique constraint
* Redis SETNX

---

## 🧠 Key Rule

> ReentrantLock = single JVM
> SeatLock (DB/Redis) = distributed safety

---

# ⚙️ 7. Services (Methods)

## 🔒 SeatLockService

* lockSeats(showSeatIds, userId)
* validateLock(showSeatIds, userId)
* releaseLock(showSeatIds)

---

## 🎟️ BookingService

* createBooking(userId, showId, showSeatIds)
* confirmBooking(bookingId)
* cancelBooking(bookingId)

---

## 💳 PaymentService

* processPayment(bookingId, amount)

---

## 🎫 TicketService

* generateTickets(bookingId)

---

# 🔁 8. Booking Flow

```
createBooking()
  → lockSeats()

processPayment()

confirmBooking()
  → validateLock()
  → mark seats BOOKED
  → generateTickets()
  → releaseLock()
```

---

# 🧠 9. Interview Power Lines

* “Seat is static, ShowSeat is dynamic per show.”
* “I use SeatLock to avoid blocking seats during payment.”
* “Booking represents transaction, Ticket represents per-seat access.”
* “I avoid passing seatIds in confirmBooking, rely on bookingId.”
* “For distributed systems, I use Redis/DB instead of in-memory locks.”

---

# ⚡ 10. Common Mistakes

❌ Storing status in Seat
❌ Booking without SeatLock
❌ Using only ReentrantLock
❌ Passing raw seatIds during confirmation
❌ Ignoring expiry handling

---

# 🚀 11. Mental Model

```
Seat → static
ShowSeat → state
SeatLock → temporary hold
Booking → final record
Ticket → entry proof
```

---

# 🧭 12. Practice Strategy

* Design → Code → Review (same day or next day)
* Focus on clarity, not perfection
* Always explain tradeoffs

---

🔥 Goal:
Think → Design → Justify → Implement
