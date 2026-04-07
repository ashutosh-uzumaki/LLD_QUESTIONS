package DigitalWallet.models;

import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;
    private final String currency;

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

    public Money addMoney(Money other){
        if(!this.getCurrency().equals(other.getCurrency())){
            throw new IllegalArgumentException("Invalid currency!!");
        }
        return new Money(
                this.amount.add(other.amount),
                this.currency
        );
    }

    public Money debitMoney(Money other){
        if(!this.getCurrency().equals(other.getCurrency())){
            throw new IllegalArgumentException("Invalid currency!!");
        }

        return new Money(
                this.amount.subtract(other.amount),
                this.currency
        );
    }
}
