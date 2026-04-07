# UML Relationships - English Words Mapping Guide

**Interview Cheat Sheet** — Use this to identify relationships from problem statements.

---

## 🎯 Quick Reference Table

| English Words/Phrases | UML Relationship | Symbol | Code Implementation |
| :--- | :--- | :--- | :--- |
| **"is a"**, **"extends"**, **"inherits"** | Inheritance | `──▷` | `class Dog extends Animal` |
| **"implements"**, **"can do"** | Realization | `- -▷` | `class Dog implements Animal` |
| **"has a"**, **"owns"**, **"contains"** (strong) | Composition | `◆──` | `private final List<Order> orders` |
| **"has a"**, **"uses"**, **"references"** (weak) | Aggregation | `◇──` | `private Wallet wallet` |
| **"uses"**, **"calls"**, **"depends on"** | Dependency | `- ->` | Method parameter or local variable |
| **"works with"**, **"associated with"** | Association | `────` | Instance variable |

---

## 🔍 Detailed Breakdown with Examples

### 1️⃣ **Inheritance** (`is-a` / `extends`)

**English Words:**
- "is a"
- "is a type of"
- "inherits from"
- "extends"
- "specialization of"

**Example Problem Statement:**
> "A **Savings Account is a** type of **Account**. A **Current Account is also an** Account."

**UML:**
```
SavingsAccount ──▷ Account
CurrentAccount ──▷ Account
```

**Code:**
```java
class Account { }
class SavingsAccount extends Account { }
class CurrentAccount extends Account { }
```

**Interview Tip:** Use when there's **true "is-a" relationship** and **code reuse**.

---

### 2️⃣ **Realization/Implementation** (`implements`)

**English Words:**
- "implements"
- "can do"
- "provides"
- "fulfills"
- "behaves as"

**Example Problem Statement:**
> "A **Wallet can make** payments. A **Credit Card can also make** payments."

**UML:**
```
Wallet - -▷ PaymentMethod
CreditCard - -▷ PaymentMethod
```

**Code:**
```java
interface PaymentMethod {
    boolean pay(Money amount);
}
class Wallet implements PaymentMethod { }
class CreditCard implements PaymentMethod { }
```

**Interview Tip:** Use for **capabilities/behaviors**, not identity.

---

### 3️⃣ **Composition** (`has-a` / `owns` / `contains` - STRONG)

**English Words:**
- "owns"
- "contains"
- "consists of"
- "is made of"
- "has a" (strong ownership)
- "part of" (lifecycle dependent)

**Key Question:** **"If parent is deleted, should child also be deleted?"** → YES = Composition

**Example Problem Statement:**
> "A **Wallet owns** its **Transactions**. If wallet is deleted, **delete all transactions**."

**UML:**
```
Wallet ◆── Transaction
```

**Code:**
```java
class Wallet {
    private final List<Transaction> transactions; // ✅ Final, owned
    
    public Wallet() {
        this.transactions = new ArrayList<>();
    }
}
```

**Characteristics:**
- ✅ Child cannot exist without parent
- ✅ Parent creates child
- ✅ Parent deletes child
- ✅ Exclusive ownership (one parent only)

**More Examples:**
- House ◆ Room (If house demolished, rooms gone)
- Order ◆ OrderItem (If order cancelled, items deleted)
- Human ◆ Heart (If human dies, heart stops)

---

### 4️⃣ **Aggregation** (`has-a` / `uses` / `references` - WEAK)

**English Words:**
- "has a" (weak reference)
- "uses"
- "references"
- "works with"
- "associated with"
- "part of" (lifecycle independent)

**Key Question:** **"If parent is deleted, can child exist independently?"** → YES = Aggregation

**Example Problem Statement:**
> "A **User has a** Wallet. But if user is deleted, **wallet can still exist** (for audit)."

**UML:**
```
User ◇── Wallet
```

**Code:**
```java
class User {
    private Wallet wallet; // ❌ Not final, can change
    
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
```

**Characteristics:**
- ✅ Child can exist without parent
- ✅ Child can have multiple parents
- ✅ Parent doesn't create child
- ✅ Shared ownership

**More Examples:**
- Department ◇ Professor (Prof can move departments)
- Playlist ◇ Song (Song can be in multiple playlists)
- Team ◇ Player (Player can switch teams)

---

### 5️⃣ **Dependency** (`uses` / `depends on`)

**English Words:**
- "uses"
- "depends on"
- "calls"
- "creates"
- "passes to"
- "receives from"

**Key Question:** **"Is this a temporary relationship (method parameter/local variable)?"** → YES = Dependency

**Example Problem Statement:**
> "A **PaymentService uses** a **Logger** to log transactions."

**UML:**
```
PaymentService - -> Logger
```

**Code:**
```java
class PaymentService {
    public void processPayment(Money amount) {
        Logger logger = Logger.getInstance(); // ⚠️ Local variable
        logger.log("Processing payment: " + amount);
    }
}
```

**Characteristics:**
- ⚠️ Temporary relationship
- ⚠️ Not stored as field
- ⚠️ One class uses another's functionality
- ⚠️ Weakest relationship

**More Examples:**
- Customer - -> CreditCardValidator (validates once)
- ReportGenerator - -> DatabaseConnection (uses temporarily)
- EmailSender - -> SMTPServer (sends via)

---

### 6️⃣ **Association** (`works with` / `connected to`)

**English Words:**
- "works with"
- "communicates with"
- "connected to"
- "knows about"
- "interacts with"

**Key Question:** **"Do these classes have a long-term relationship but neither owns the other?"** → YES = Association

**Example Problem Statement:**
> "A **Teacher teaches** Students. A **Student learns from** Teachers."

**UML:**
```
Teacher ──── Student
```

**Code:**
```java
class Teacher {
    private List<Student> students;
}
class Student {
    private List<Teacher> teachers;
}
```

**Characteristics:**
- ✅ Long-term relationship
- ✅ Both know about each other
- ✅ No ownership
- ✅ Can be bidirectional

**More Examples:**
- Doctor ──── Patient
- Driver ──── Car (owns license, not the car)
- User ──── Group (member of)

---

## 🧪 Decision Tree (Use in Interviews)

```
Start: "Class A and Class B have a relationship"
         │
         ▼
    Is it "is-a"? ──YES──▶ Inheritance (extends)
         │
         NO
         ▼
    Is it "can-do"? ──YES──▶ Realization (implements)
         │
         NO
         ▼
    Does A "have" B?
         │
         ├── YES ──▶ Is B's lifecycle dependent on A?
         │              │
         │              ├── YES ──▶ Composition (◆)
         │              │
         │              NO
         │              ▼
         │           Aggregation (◇)
         │
         NO
         ▼
    Does A "use" B temporarily? ──YES──▶ Dependency (- ->)
         │
         NO
         ▼
    Do A and B "work together"? ──YES──▶ Association (────)
```

---

## 📊 Comparison Table

| Relationship | Strength | Lifecycle | Ownership | Multiplicity | Example |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Inheritance** | N/A | Independent | N/A | 1 parent | Dog is-a Animal |
| **Realization** | N/A | Independent | N/A | Many | Wallet implements PaymentMethod |
| **Composition** | 💪 Strongest | Dependent | Exclusive | 1 parent | Wallet ◆ Transaction |
| **Aggregation** | 💪💪 Medium | Independent | Shared | Many parents | User ◇ Wallet |
| **Association** | 💪 Weak | Independent | None | Many | Teacher ──── Student |
| **Dependency** | 💀 Weakest | Independent | None | Temporary | Service - -> Logger |

---

## 🎯 Wallet System Examples (Your Project)

| Relationship | Classes | English Phrase | Why? |
| :--- | :--- | :--- | :--- |
| **Composition** | Wallet ◆ Transaction | "Wallet **owns** transactions" | If wallet deleted, transactions gone |
| **Aggregation** | User ◇ Wallet | "User **has a** wallet" | Wallet can exist without user (audit) |
| **Association** | Wallet ──── Money | "Wallet **has** balance" | Wallet stores Money, doesn't own it |
| **Dependency** | Transaction △ Money | "Transaction **uses** Money" | Transaction references Money type |
| **Dependency** | Wallet △ TransactionStatus | "Wallet **uses** Status" | Wallet checks status, doesn't own it |

---

## 🗣️ Interview Script (How to Explain)

**Interviewer:** "Why did you use composition between Wallet and Transaction?"

> **You Say:**
>
> "I used **composition** because:
>
> 1.  **Lifecycle Dependency:** Transactions belong to a specific wallet. If we delete a wallet, its transaction history should also be deleted (or archived).
>
> 2.  **Ownership:** The Wallet **owns** its transactions — no other wallet can claim them.
>
> 3.  **Encapsulation:** Transactions are created and managed internally by the Wallet, not passed from outside.
>
> If transactions could exist independently or be shared across wallets, I would have used **aggregation** instead."

---

## ✅ Quick Quiz (Test Yourself)

| Problem Statement | Relationship | Answer |
| :--- | :--- | :--- |
| "A **Car has an** Engine" | ______ | Composition ◆ (Engine can't exist without Car) |
| "A **Library has** Books" | ______ | Aggregation ◇ (Books can exist without library) |
| "A **Square is a** Shape" | ______ | Inheritance ──▷ |
| "A **Printer uses** Paper" | ______ | Dependency - -> (temporary) |
| "A **Student enrolls in** Course" | ______ | Association ──── |

---

## 📝 Cheat Sheet (Print This!)

```
┌─────────────────────────────────────────────────────────┐
│           ENGLISH → UML RELATIONSHIP MAP                │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  "is a" / "extends"        → Inheritance (──▷)          │
│  "implements" / "can do"   → Realization (- -▷)         │
│  "owns" / "contains"       → Composition (◆──)          │
│  "has a" (weak)            → Aggregation (◇──)          │
│  "uses" (temporary)        → Dependency (- ->)          │
│  "works with"              → Association (────)         │
│                                                         │
│  KEY QUESTION: "If parent deleted, what happens?"       │
│  - Child deleted too     → Composition                  │
│  - Child survives        → Aggregation                  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 🎯 How to Use This Guide in Interviews

### Step 1: Read Problem Statement Carefully
Underline all verbs and relationship words.

### Step 2: Map Words to Relationships
Use the Quick Reference Table above.

### Step 3: Ask Clarifying Questions
- "Is this a strong or weak relationship?"
- "Can the child exist without the parent?"
- "Is this temporary or permanent?"

### Step 4: Draw UML Diagram
Use the correct symbols (◆, ◇, ──▷, etc.)

### Step 5: Explain Your Choice
Use the Interview Script format to justify your design.

---

**Save this guide and use it in your next interview!** 🚀

When interviewer gives you a problem statement, **underline the verbs** and map them using this guide. You'll never be confused about relationships again! 💙

---

*Last Updated: 2026*  
*For LLD Interview Preparation*