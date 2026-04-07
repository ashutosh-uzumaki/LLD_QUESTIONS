# Digital Wallet System

A thread-safe digital wallet implementation demonstrating LLD concepts, concurrency patterns, and clean code principles.

## 📌 Features

- **User Management** — Create users with unique wallets
- **Add Money** — Add funds to wallet with currency validation
- **Transfer Money** — P2P transfers with balance checks
- **Transaction History** — View all past transactions
- **Thread-Safe** — ReentrantLock with deadlock prevention
- **Immutable Money** — Value object with BigDecimal precision

## 🏗️ Architecture

```
DigitalWallet/
├── models/
│   ├── User.java
│   ├── Wallet.java
│   ├── Money.java
│   └── Transaction.java
├── enums/
│   └── TransactionStatus.java
└── Main.java
```

## 🚀 Versions

| Version | Features | Status |
| :--- | :--- | :--- |
| **V1** | Basic logic (add, transfer, history) | ✅ Complete |
| **V2** | Thread-safety with ReentrantLock | ✅ Complete |
| **V3** | Design patterns (Strategy, State, Observer) | 🔄 Planned |

## 🛠️ Tech Stack

- **Language:** Java 11+
- **Concurrency:** ReentrantLock, Lock Ordering
- **Data Types:** BigDecimal (for money), LocalDateTime
- **Design:** Immutability, Encapsulation, Single Responsibility

## 📦 Classes

| Class | Responsibility | Thread-Safe |
| :--- | :--- | :--- |
| `Money` | Value object for currency | ✅ (Immutable) |
| `Transaction` | Transaction record | ✅ (Mostly immutable) |
| `Wallet` | Balance & transfer logic | ✅ (ReentrantLock) |
| `User` | User identity & wallet access | ⚠️ (Single-threaded) |

## 🧪 Usage Example

```java
// Create users
User alice = new User("U1", "Alice");
alice.createWallet("W1");

User bob = new User("U2", "Bob");
bob.createWallet("W2");

// Add money
alice.getWallet().addMoney(new Money(new BigDecimal("1000"), "INR"));

// Transfer
boolean success = alice.getWallet().transfer(
    bob.getWallet(),
    new Money(new BigDecimal("200"), "INR")
);

// Check balance
System.out.println(alice.getWallet().getBalance().getAmount()); // 800
System.out.println(bob.getWallet().getBalance().getAmount());   // 200

// View history
System.out.println(alice.getWallet().getHistory().size()); // 1
```

## 🔒 Concurrency Design

### Lock Strategy
- **ReentrantLock** for fine-grained control
- **Lock Ordering** by Wallet ID to prevent deadlocks
- **try-finally** for guaranteed lock release

### Deadlock Prevention
```java
// Always acquire locks in consistent order (by Wallet ID)
Wallet first = this.id.compareTo(dest.id) < 0 ? this : dest;
Wallet second = first == this ? dest : this;

first.lock.lock();
second.lock.lock();
try {
    // Critical section
} finally {
    second.lock.unlock();
    first.lock.unlock();
}
```

## 🎯 Key Design Decisions

| Decision | Choice | Reason |
| :--- | :--- | :--- |
| **Money Type** | `BigDecimal` | No precision loss (unlike `double`) |
| **Money Immutability** | `final` fields | Thread-safe, no synchronization needed |
| **Lock Type** | `ReentrantLock` | Timeout support, fairness, deadlock prevention |
| **Transaction Status** | Mutable | Lifecycle changes (PENDING → SUCCESS) |
| **History Storage** | `List<Transaction>` | Simple, in-memory for V1/V2 |

## 🧪 Testing

### Multi-Threaded Test
```bash
# Run Main.java with 5 concurrent threads
# Expected: All transfers complete without race conditions
# Verify: Final balances match expected values
```

### Test Cases
- ✅ Add money to wallet
- ✅ Transfer with sufficient balance
- ✅ Transfer with insufficient balance (returns false)
- ✅ Concurrent transfers (5 threads)
- ✅ Transaction history accuracy

## 📈 Future Enhancements (V3)

- [ ] **Strategy Pattern** — Payment methods (UPI, Card, NetBanking)
- [ ] **State Pattern** — Transaction lifecycle management
- [ ] **Observer Pattern** — Notifications (Email, SMS, Push)
- [ ] **ReadWriteLock** — Optimize read-heavy operations
- [ ] **Custom Exceptions** — InsufficientBalance, CurrencyMismatch
- [ ] **Transaction Types** — P2P, MERCHANT, REFUND, RECHARGE

## 🎤 Interview Talking Points

1. **Why ReentrantLock over synchronized?**
    - Timeout support, fairness policy, deadlock prevention, lock status check

2. **Why immutable Money class?**
    - Thread-safe without locks, predictable behavior, safe publication

3. **How is deadlock prevented?**
    - Lock ordering by Wallet ID ensures consistent acquisition order

4. **Why lock on read operations?**
    - Prevents ConcurrentModificationException during list iteration

5. **V1 vs V2 trade-offs?**
    - V1: Simple, fast to implement | V2: Thread-safe, production-ready

## 📝 License

MIT License — Free for learning and interview preparation.

---

**Built with ❤️ for LLD Interview Preparation**