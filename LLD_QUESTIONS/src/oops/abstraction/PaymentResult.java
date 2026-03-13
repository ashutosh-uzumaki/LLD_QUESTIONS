package oops.abstraction;

import java.util.UUID;

public class PaymentResult {
    private PaymentStatus status;
    private String message;
    private final UUID transactionId;

    public PaymentResult(PaymentStatus status, String message){
        this.status = status;
        this.message = message;
        transactionId = UUID.randomUUID();
    }

    public PaymentStatus getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public UUID getTransactionId(){
        return transactionId;
    }
}
