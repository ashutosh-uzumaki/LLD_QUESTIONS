# SOLID Principles — Revision Guide

## Quick Reference

| Principle | One-Liner | Violation Smell |
|-----------|-----------|-----------------|
| **S** — Single Responsibility | One class, one reason to change | Class doing validation + DB + notification + receipt |
| **O** — Open/Closed | New behavior = new class, not modifying old ones | Adding `else if` for every new type |
| **L** — Liskov Substitution | Subtypes must honor the parent's contract | `throw new UnsupportedOperationException()` in override |
| **I** — Interface Segregation | Many small interfaces > one fat interface | Class forced to implement methods it doesn't use |
| **D** — Dependency Inversion | Depend on abstractions, inject via constructor | `new MySQLRepository()` inside a service class |

---

## S — Single Responsibility Principle

### Rule
> A class should have **only one reason to change.**

### Violation Example
```java
// ❌ BAD — 4 reasons to change in one class
public class PaymentService {
    public void processPayment(String payer, String receiver, BigDecimal amount) {
        // 1. Validates input
        if (payer == null || amount.compareTo(BigDecimal.ZERO) <= 0) { ... }

        // 2. Saves to database
        Connection conn = DriverManager.getConnection("jdbc:mysql://...");
        stmt.executeUpdate();

        // 3. Sends notification
        SmtpClient client = new SmtpClient("smtp.gmail.com");
        client.send(payer + "@gmail.com", emailBody);

        // 4. Generates PDF receipt
        PDDocument doc = new PDDocument();
        doc.save("/receipts/" + payer + ".pdf");
    }
}
```

### Fix — Extract Responsibilities
```java
// ✅ GOOD — Each class has one job
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final ReceiptGenerator receiptGenerator;

    public PaymentService(PaymentRepository repo, NotificationService notif,
                          ReceiptGenerator receipt) {
        this.paymentRepository = repo;
        this.notificationService = notif;
        this.receiptGenerator = receipt;
    }

    public PaymentResult makePayment(String payee, String receiver, BigDecimal amount) {
        // Inline validation (simple enough to stay here)
        if (payee == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(PaymentStatus.FAILURE, "Validation failed");
        }

        Payment payment = new Payment(payee, receiver, amount);

        // Critical path — must succeed
        try {
            paymentRepository.save(payment);
        } catch (Exception e) {
            return new PaymentResult(PaymentStatus.FAILURE, "Save failed");
        }

        // Non-critical — best effort
        try { receiptGenerator.generate(payment); } catch (Exception e) { /* log */ }
        try { notificationService.send(payment); } catch (Exception e) { /* log */ }

        return new PaymentResult(PaymentStatus.SUCCESS, "Transaction successful");
    }
}
```

### SRP Scorecard
| Class | Responsibility | Reason to Change |
|-------|---------------|-----------------|
| `PaymentService` | Orchestrates flow | Business flow changes |
| `PaymentRepository` | Persists data | Database changes |
| `NotificationService` | Sends alerts | Notification channel changes |
| `ReceiptGenerator` | Creates receipts | Receipt format changes |
| `Payment` | Holds payment data | Payment fields change |

### Key Decision — When to Extract vs Keep Inline
| Situation | Decision |
|-----------|----------|
| 3 lines of null checks | Keep inline — not worth a class |
| Complex fraud checks, KYC, blacklist | Extract — independent reason to change |
| Same validation in multiple services | Extract — avoid duplication |

### Interview Tip
> "I'd keep the critical path synchronous with error handling, and push non-critical steps like notifications and receipts to an async event-based flow."

---

## O — Open/Closed Principle

### Rule
> A class should be **open for extension, closed for modification.**

### Violation Example
```java
// ❌ BAD — Must modify this class for every new tier
public class DiscountCalculator {
    public BigDecimal calculateDiscount(String customerTier, BigDecimal fee) {
        if (customerTier.equals("GOLD")) {
            return fee.multiply(new BigDecimal("0.10"));
        } else if (customerTier.equals("PLATINUM")) {
            return fee.multiply(new BigDecimal("0.20"));
        } else if (customerTier.equals("REGULAR")) {
            return BigDecimal.ZERO;
        }
        throw new IllegalArgumentException("Unknown tier");
    }
}
```

### Why It's Bad
- Adding DIAMOND tier → modify tested, deployed code
- Regression risk — touching working code can break it
- Merge conflicts — multiple devs editing same file
- Ever-growing if-else chain — unreadable at 20 types

### Fix — Interface + Implementations
```java
// ✅ GOOD — New tier = new class, zero changes to existing code
public interface DiscountCalculator {
    BigDecimal calculateDiscount(BigDecimal processingFee);
}

public class GoldDiscount implements DiscountCalculator {
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    @Override
    public BigDecimal calculateDiscount(BigDecimal processingFee) {
        return processingFee.multiply(DISCOUNT_RATE);
    }
}

public class PlatinumDiscount implements DiscountCalculator {
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.20");

    @Override
    public BigDecimal calculateDiscount(BigDecimal processingFee) {
        return processingFee.multiply(DISCOUNT_RATE);
    }
}
```

### Adding a New Tier (DIAMOND)
```java
// Just create a new class — nothing else changes
public class DiamondDiscount implements DiscountCalculator {
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.30");

    @Override
    public BigDecimal calculateDiscount(BigDecimal processingFee) {
        return processingFee.multiply(DISCOUNT_RATE);
    }
}
```

### OCP Violation Smells
- `if-else` or `switch` on a type string
- Adding a new type requires editing an existing class
- A method grows every time a new variant is introduced

### Interview One-Liner
> "I create a new implementation class. No existing class is modified. Open for extension, closed for modification."

### Constants vs Config
| Situation | Approach |
|-----------|----------|
| Rate changes rarely | `private static final BigDecimal RATE = new BigDecimal("0.10")` |
| Rate changes frequently | Inject via constructor: `new GoldDiscount(rateFromConfig)` |

### Interview Tip
> "I'm keeping it as a constant for now. In production, if fees change frequently, I'd externalize them to a config service."

---

## L — Liskov Substitution Principle

### Rule
> If `B extends A`, you should be able to replace `A` with `B` **without breaking anything.**

### Violation Example
```java
// ❌ BAD — GoldLoan breaks parent's contract
public class Loan {
    public void prepay(BigDecimal amount) {
        // reduces outstanding principal
    }
}

public class GoldLoan extends Loan {
    @Override
    public void prepay(BigDecimal amount) {
        throw new UnsupportedOperationException("Prepayment not allowed");
    }
}

// Caller code — breaks at runtime!
Loan loan = getLoanFromDB(loanId); // could return GoldLoan
loan.prepay(amount); // 💥 UnsupportedOperationException
```

### Why It's Bad
- Caller expects `prepay()` to work — `Loan` promises it
- `GoldLoan` **breaks that promise** with an exception
- Caller has no way to know this will happen
- Can't swap parent with child without breaking behavior

### Fix — Change the Contract
```java
// ✅ GOOD — Return a result instead of throwing
public abstract class Loan {
    private BigDecimal principal;
    private final LocalDate loanStartDate;

    public Loan(BigDecimal principal, LocalDate loanStartDate) {
        this.principal = principal;
        this.loanStartDate = loanStartDate;
    }

    // Getters and setters
    public BigDecimal getPrincipal() { return principal; }
    public LocalDate getLoanStartDate() { return loanStartDate; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }

    // Contract: "attempts prepayment, returns result"
    public abstract PaymentResult prepayLoan(BigDecimal amount);
}

public class GoldLoan extends Loan {
    public GoldLoan(BigDecimal principal, LocalDate loanStartDate) {
        super(principal, loanStartDate);
    }

    @Override
    public PaymentResult prepayLoan(BigDecimal amount) {
        long totalMonths = ChronoUnit.MONTHS.between(getLoanStartDate(), LocalDate.now());
        if (totalMonths < 3) {
            return new PaymentResult(PaymentStatus.FAILURE,
                "Gold loan cannot be prepaid before 3 months");
        }
        BigDecimal principal = getPrincipal();
        principal = principal.subtract(amount);
        setPrincipal(principal);
        return new PaymentResult(PaymentStatus.SUCCESS, "Gold loan prepaid");
    }
}
```

### LSP-Compliant Caller Code
```java
// Works for ANY loan type — no surprises
Loan loan = getLoanFromDB(loanId);
PaymentResult result = loan.prepayLoan(amount);

if (result.getPaymentStatus() == PaymentStatus.SUCCESS) {
    // proceed
} else {
    // handle gracefully — no exception, no crash
}
```

### LSP Violation Smells
- `throw new UnsupportedOperationException()` in an override
- Child method does nothing (empty body) for a parent's method
- Caller needs `instanceof` checks before calling methods
- Child adds unexpected preconditions the parent didn't have

### The Key Insight
- **Old contract:** "prepay always works" → forces exception in child
- **New contract:** "prepay attempts and returns a result" → child can reject gracefully
- Conditional behavior (allowed after 3 months) ≠ unsupported behavior

### Interview One-Liner
> "Subtypes must honor the parent's contract. If behavior is conditional, return a result instead of throwing an exception."

---

## I — Interface Segregation Principle

### Rule
> No client should be forced to depend on methods it doesn't use.

### Violation Example
```java
// ❌ BAD — Fat interface forces unnecessary implementations
public interface LoanService {
    void applyLoan();
    void approveLoan();
    void disburseLoan();
    void prepayLoan();
    void calculateInterest();
    void generateStatement();
    void closeAccount();
    void auctionCollateral();
}

public class PersonalLoan implements LoanService {
    // Forced to implement this — personal loans have no collateral!
    public void auctionCollateral() {
        throw new UnsupportedOperationException("No collateral");
    }
}
```

### Why It's Bad
- `PersonalLoan` forced to implement `auctionCollateral()` it doesn't need
- Leads to `UnsupportedOperationException` → which is an LSP violation
- ISP violations **cause** LSP violations — they're connected

### Fix — Split by Who Needs It
| Interface | Methods | Who Implements |
|-----------|---------|----------------|
| `LoanService` | `applyLoan()`, `approveLoan()`, `disburseLoan()`, `prepayLoan()`, `calculateInterest()`, `closeLoan()` | All loans |
| `SecuredLoanService` | `auctionCollateral()` | GoldLoan, HomeLoan |
| `StatementGenerator` | `generateStatement(Loan loan)` | Separate class |

```java
// ✅ GOOD — Each loan picks only what it needs
public class PersonalLoan implements LoanService {
    // Only core methods — no auction forced
}

public class GoldLoan implements LoanService, SecuredLoanService {
    // Core methods + auction — because it has collateral
}
```

### How to Split Interfaces — The Process
1. List all methods in the fat interface
2. For each method, ask: "Does **every** implementor need this?"
3. Group methods by **who needs them**
4. Extract groups into separate interfaces
5. Also check: does this method **belong** in this class at all? (`generateStatement` → separate class = SRP)

### ISP Violation Smells
- Empty method bodies in implementations
- `UnsupportedOperationException` in implementations
- Interface with 8+ methods where some implementors skip half
- A change to one method forces recompilation of unrelated implementors

### Interview One-Liner
> "Many small, specific interfaces are better than one big, general interface. Split by who needs it, not by what it does."

---

## D — Dependency Inversion Principle

### Rule
> High-level modules should not depend on low-level modules. Both should depend on abstractions.

### Violation Example
```java
// ❌ BAD — High-level class creates low-level dependency directly
public class PaymentService {
    private final MySQLPaymentRepository repository;

    public PaymentService() {
        this.repository = new MySQLPaymentRepository(); // tight coupling!
    }
}
```

### Why It's Bad
- `PaymentService` (business logic) **knows** it's MySQL
- Migrating to PostgreSQL → change `PaymentService` (business logic should NOT change for DB reasons)
- Can't unit test without a real MySQL database
- High-level module depends on low-level detail

### Fix — Depend on Abstraction + Constructor Injection
```java
// ✅ GOOD — Depends on interface, receives implementation from outside
public interface PaymentRepository {
    void save(Payment payment);
}

public class MySQLPaymentRepository implements PaymentRepository {
    @Override
    public void save(Payment payment) {
        // MySQL-specific code
    }
}

public class PostgresPaymentRepository implements PaymentRepository {
    @Override
    public void save(Payment payment) {
        // PostgreSQL-specific code
    }
}

public class PaymentService {
    private final PaymentRepository repository; // interface, not concrete!

    public PaymentService(PaymentRepository repository) {
        this.repository = repository; // injected from outside
    }
}

// Caller decides the implementation
PaymentService service = new PaymentService(new MySQLPaymentRepository());

// Migration to PostgreSQL — only this line changes:
PaymentService service = new PaymentService(new PostgresPaymentRepository());
// PaymentService itself is UNTOUCHED
```

### Dependency Direction
```
❌ BEFORE (violation):
PaymentService → MySQLPaymentRepository (concrete)

✅ AFTER (DIP compliant):
PaymentService → PaymentRepository (interface) ← MySQLPaymentRepository
```

Both high-level and low-level modules depend on the abstraction (interface). Neither depends on the other directly.

### DIP Violation Smells
- `new ConcreteClass()` inside a high-level service
- Changing a database/API/library requires modifying business logic classes
- Can't unit test a class without setting up external dependencies
- Import statements showing concrete infrastructure classes in business logic

### Constructor Injection vs Other Approaches
| Approach | Example | When to Use |
|----------|---------|-------------|
| Constructor injection | `new PaymentService(repo)` | Always prefer — dependencies are clear |
| Setter injection | `service.setRepo(repo)` | Optional dependencies only |
| Field injection | `@Autowired` on field | Avoid — hides dependencies |

### Interview One-Liner
> "I inject dependencies through the constructor so the class depends on the interface, not the implementation. In production, Spring's @Autowired handles this automatically."

---

## How SOLID Principles Connect

```
ISP violation (fat interface)
    → forces empty methods / UnsupportedOperationException
    → which is an LSP violation (breaks parent contract)

SRP violation (class does too much)
    → multiple reasons to change
    → harder to apply OCP (can't extend without modifying)

DIP violation (depends on concrete class)
    → can't swap implementations
    → breaks OCP (must modify to change dependency)
```

### In Interviews — The Chain:
> "These principles reinforce each other. ISP violations lead to LSP violations. SRP violations make OCP harder. DIP enables OCP by allowing implementation swapping."

---

## Common Interview Questions on SOLID

1. "What is SRP? Give an example." → PaymentService extracting repository, notification, receipt
2. "What is OCP? How do you achieve it?" → Interface + implementations instead of if-else chains
3. "What is LSP? Give a violation." → GoldLoan throwing UnsupportedOperationException for prepay
4. "What is ISP? When would you split an interface?" → LoanService forced to implement auctionCollateral
5. "What is DIP? How is it different from Dependency Injection?" → DIP is the principle (depend on abstractions), DI is the mechanism (constructor injection)
6. "How are these principles connected?" → ISP→LSP, SRP→OCP, DIP→OCP chain

---

*Last updated: Week 1 — SOLID Complete ✅*