# URL Shortener — LLD Revision Notes

---

# 🧠 Problem Overview

Design a URL Shortener system like Bitly:

* Convert long URL → short URL
* Redirect short URL → original URL

---

# 🧩 Core Design

## 📦 Entity

### UrlRecord

Fields:

* longUrl
* shortUrl
* createdAt
* expiresAt

Method:

* isExpired()

---

## ⚙️ Strategy Pattern

### UrlShortenerStrategy (Interface)

```java
String shortenUrl(String longUrl);
```

### Base62EncodingStrategy

* Uses AtomicLong counter
* Converts ID → Base62 string
* Handles:

    * id = 0 case
    * Optional fixed length (7 chars)

---

## 🗄️ Repository (DAO)

### UrlRecordRepository

```java
void save(UrlRecord record);
Optional<UrlRecord> findByShortUrl(String shortUrl);
```

### InMemoryUrlRecordRepository

* Uses ConcurrentHashMap
* Key → shortUrl
* Value → UrlRecord

---

## 🧠 Service

### UrlService

#### shortenUrl()

Flow:

1. Generate short URL via strategy
2. Create UrlRecord
3. Save in repository
4. Return short URL

#### getOriginalUrl()

Flow:

1. Fetch record
2. Check expiry
3. Return longUrl

---

# 🧩 UML Diagram

```
+----------------------+
|     UrlService       |
+----------------------+
| - strategy           |
| - repository         |
+----------------------+
| + shortenUrl()       |
| + getOriginalUrl()   |
+----------------------+
        | uses
        v
+---------------------------+
|   UrlShortenerStrategy    |<<interface>>
+---------------------------+
| + shortenUrl()            |
+---------------------------+
            ^
            | implements
            |
+------------------------------+
|  Base62EncodingStrategy      |
+------------------------------+
| + shortenUrl()               |
+------------------------------+

        |
        | uses
        v

+---------------------------+
|   UrlRecordRepository     |
+---------------------------+
| + save()                  |
| + findByShortUrl()        |
+---------------------------+
            |
            | manages
            v
+---------------------------+
|        UrlRecord          |
+---------------------------+
| - longUrl                 |
| - shortUrl                |
| - createdAt               |
| - expiresAt               |
+---------------------------+
| + isExpired()             |
+---------------------------+
```

---

# 🌐 API Design

## 1. Create Short URL

**POST /urls**

Request:

```json
{
  "longUrl": "https://example.com"
}
```

Response:

```json
{
  "shortUrl": "abc123"
}
```

Status:

```
201 Created
```

Optional Header:

```
Location: /urls/abc123
```

---

## 2. Redirect

**GET /{shortUrl}**

Example:

```
GET /abc123
```

Response:

```
302 Found
Location: https://example.com
```

---

# 🧠 Key Design Decisions

## Why Strategy Pattern?

* Multiple encoding approaches (Base62, hashing, etc.)
* Easily extensible

---

## Why Repository?

* Decouples storage logic
* Can switch DB easily

---

## Why return Entity (not DTO)?

* No API layer yet
* DTO introduced at API boundary

---

# ⚠️ Edge Cases

* Expired URL
* URL not found
* Hash collision (if using hash)
* Counter overflow (if fixed length)

---

# 🚀 Interview Talking Points

* “I used Strategy pattern for pluggable encoding logic”
* “Repository abstracts persistence layer”
* “Service handles business logic and validation”
* “DTO can be introduced at API layer for controlled exposure”
* “Base62 ensures compact, URL-safe encoding”

---

# 🔥 Mental Model

All problems reduce to:

* Entity → data
* Service → logic
* Repository → storage
* Strategy → flexible behavior

---

# ⚔️ Final Goal

> “Given any LLD problem → map to patterns → design cleanly in 45 minutes”

---
