package DigitalWallet.models;

import DigitalWallet.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class Wallet {
    private final String id;
    private Money balance;
    private final List<Transaction> transactions;
    private final ReentrantLock lock = new ReentrantLock();

    public Wallet(String id) {
        this.id = id;
        this.balance = new Money(new BigDecimal("0"), "INR");
        this.transactions = new ArrayList<>();
    }

    // Add money to wallet
    public void addMoney(Money amount) {
        lock.lock();
        try{
            this.balance = this.balance.addMoney(amount);
        }finally {
            lock.unlock();
        }
    }

    // Transfer to another wallet
    public boolean transfer(Wallet dest, Money amount) {
        if (this.balance.getAmount().compareTo(amount.getAmount()) < 0) {
            return false;
        }
        Wallet first = this.id.compareTo(dest.id) < 0 ? this : dest;
        Wallet second = first == this ? dest : this;
        first.lock.lock();
        second.lock.lock();
        try {
            this.balance = this.balance.debitMoney(amount);
            dest.addMoney(amount);
            Transaction txn = new Transaction(
                    generateTxnId(),
                    amount,
                    LocalDateTime.now(),
                    TransactionStatus.SUCCESS
            );

            this.recordTransaction(txn);
            dest.recordTransaction(txn);
            return true;
        }finally{
            second.lock.unlock();
            first.lock.unlock();
        }
    }

    // Get balance
    public Money getBalance() {
        return this.balance;
    }

    // Get transaction history
    public List<Transaction> getHistory() {
        return new ArrayList<>(this.transactions); // Return copy for safety
    }

    // Record a transaction
    private void recordTransaction(Transaction txn) {
        transactions.add(txn);
    }

    private String generateTxnId(){
        return "TXN_"+System.currentTimeMillis();
    }
}