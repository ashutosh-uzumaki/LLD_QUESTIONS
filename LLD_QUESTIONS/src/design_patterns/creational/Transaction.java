package design_patterns.creational;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final BigDecimal amount;
    private final String sender;
    private final String transactionId;
    private final String receiver;
    private final String notes;
    private final LocalDateTime timeStamp;
    private final TransactionType transactionType;

    private Transaction(TransactionBuilder builder){
        this.amount = builder.amount;
        this.sender = builder.sender;
        this.receiver = builder.receiver;
        this.transactionId = builder.transactionId;
        this.notes = builder.notes;
        this.timeStamp = builder.timeStamp;
        this.transactionType = builder.transactionType;
    }

    public static class TransactionBuilder{
        private final BigDecimal amount;
        private final String sender;
        private final String transactionId;
        private String receiver;
        private String notes;
        private final LocalDateTime timeStamp;
        private final TransactionType transactionType;

        public TransactionBuilder(BigDecimal amount, String sender, TransactionType transactionType){
            this.amount = amount;
            this.sender = sender;
            this.transactionId = UUID.randomUUID().toString();
            this.timeStamp = LocalDateTime.now();
            this.transactionType = transactionType;
        }

        public TransactionBuilder notes(String notes){
            this.notes = notes;
            return this;
        }

        public TransactionBuilder receiver(String receiver){
            this.receiver = receiver;
            return this;
        }

        public Transaction build(){
            return new Transaction(this);
        }
    }

    public static void main(String[] args) {
        Transaction t1 = new Transaction.TransactionBuilder(new BigDecimal("200"), "Ashutosh",  TransactionType.CASH).build();
        Transaction t2 = new TransactionBuilder(new BigDecimal("200"), "Ashutosh",  TransactionType.CREDIT).receiver("Pandey").build();

    }
}
