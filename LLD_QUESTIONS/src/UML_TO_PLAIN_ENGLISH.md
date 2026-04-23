# UML Relationships — English + Arrow Mapping

---

## 🔗 1. Association

### English Words

* uses
* calls
* interacts with
* depends on

### Arrow

A --------> B

### Meaning

A uses B (no ownership)

### Example

UrlService --------> UrlRecordRepository
UrlService --------> UrlShortenerStrategy

---

## 🧱 2. Composition (Strong Ownership)

### English Words

* has-a (strong)
* contains
* made of
* cannot exist without

### Arrow

A ◆--------> B

(◆ = filled diamond at A side)

### Meaning

B cannot exist without A

### Example

Order ◆--------> OrderItem

---

## 🧩 3. Aggregation (Weak Ownership)

### English Words

* has-a (weak)
* references
* loosely contains

### Arrow

A ◇--------> B

(◇ = empty diamond at A side)

### Meaning

B can exist independently

### Example

Team ◇--------> Player

---

## 🧬 4. Inheritance (is-a)

### English Words

* is-a
* type of
* extends

### Arrow

A --------▷ B

(▷ = hollow triangle pointing to parent)

### Meaning

A is a type of B

### Example

HashStrategy --------▷ UrlShortenerStrategy

---

## 🔌 5. Interface Implementation

### English Words

* implements
* contract
* pluggable

### Arrow

A - - - -▷ B

(dashed line + hollow triangle)

### Meaning

A implements B

### Example

Base62Strategy - - - -▷ UrlShortenerStrategy

---

## ⚡ 6. Dependency (Temporary Use)

### English Words

* temporarily uses
* passed as parameter
* depends briefly

### Arrow

A - - - -> B

(dashed arrow)

### Meaning

Used temporarily, not stored

---

# 🧠 Quick Mapping Trick

| English        | UML       |
| -------------- | --------- |
| uses           | --------> |
| has-a          | ◆ or ◇    |
| is-a           | --------▷ |
| implements     | - - - -▷  |
| depends (temp) | - - - ->  |

---

# 🎯 Your Turn

Write relationships for:

* UrlService
* UrlRecordRepository
* UrlShortenerStrategy
* UrlRecord

Example format:

UrlService --------> UrlRecordRepository

Then we convert to UML diagram 🚀
