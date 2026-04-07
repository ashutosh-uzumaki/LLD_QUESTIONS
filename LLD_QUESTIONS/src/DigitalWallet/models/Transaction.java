package DigitalWallet.models;

import DigitalWallet.enums.TransactionStatus;

import java.time.LocalDateTime;

public class Transaction {
    private final String txId;
    private final Money amount;
    private TransactionStatus status;
    private final LocalDateTime timeStamp;

    public Transaction(String txId, Money amount, LocalDateTime timeStamp, TransactionStatus status) {
        this.txId = txId;
        this.amount = amount;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getTxId() {
        return txId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
