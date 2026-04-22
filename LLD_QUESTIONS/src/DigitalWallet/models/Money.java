package DigitalWallet.models;

import java.math.BigDecimal;

/**
 * Immutable Value Object representing money.
 *
 * WHY IMMUTABLE?
 * - Financial amounts should never change in place — creates new state instead
 * - Thread-safe by design — no locking needed
 * - Safe for transaction history — old balance preserved after every operation
 * - Follows Value Object pattern — two Money objects with same amount+currency are equal
 *
 * WHY BigDecimal over double/float?
 * - double/float have precision issues: 0.1 + 0.2 = 0.30000000000000004 😱
 * - BigDecimal is exact — critical for financial calculations
 * - Interview tip: always use BigDecimal for money
 */
public class Money {

    // final = cannot be reassigned after construction → guarantees immutability
    private final BigDecimal amount;
    private final String currency;  // e.g. "INR", "USD"

    /**
     * Constructor — only way to set fields (no setters = immutable)
     * Ideally add fail-fast validation here:
     *   if(amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount cannot be negative");
     *   if(currency == null || currency.isBlank()) throw new IllegalArgumentException("Currency required");
     */
    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * Returns a NEW Money object with combined amount.
     * WHY return new object? → Immutability. 'this' never changes.
     * Same pattern as String.concat() — always returns new String.
     *
     * Currency check → prevents adding INR + USD accidentally (fail-fast)
     */
    public Money addMoney(Money other) {
        // Guard: currencies must match before any arithmetic
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Invalid currency!!");
        }
        // BigDecimal.add() also returns new object — consistent immutability all the way down
        return new Money(
                this.amount.add(other.amount),
                this.currency
        );
    }

    /**
     * Returns a NEW Money object with subtracted amount.
     * INTERVIEW FOLLOW-UP: Should we check if result goes negative here?
     * → Yes in a real wallet: if(this.amount.compareTo(other.amount) < 0) throw InsufficientFundsException
     * → Keeps business rule inside Money itself (Single Responsibility)
     */
    public Money debitMoney(Money other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Invalid currency!!");
        }
        return new Money(
                this.amount.subtract(other.amount),
                this.currency
        );
    }

    /*
     * REVISION CHECKLIST:
     * ✅ Immutable — final fields, no setters, operations return new objects
     * ✅ Value Object pattern — represents a value, not an entity with identity
     * ✅ BigDecimal — correct choice for financial precision
     * ✅ Currency guard — fail-fast on mismatch
     * ⚠️  Missing: negative amount validation in constructor
     * ⚠️  Missing: insufficient funds check in debitMoney()
     * ⚠️  Missing: equals() + hashCode() override (needed for Value Objects)
     *
     * INTERVIEW ONE-LINERS:
     * "Money is immutable because financial amounts should never mutate in place —
     *  every operation creates a new balance state, preserving history."
     *
     * "I used BigDecimal over double because floating point arithmetic is imprecise
     *  for financial calculations — 0.1 + 0.2 != 0.3 in double."
     */
}
