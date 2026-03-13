package solid.srp.payment;

public class PaymentResult {
    private final PaymentStatus paymentStatus;
    private final String message;

    public PaymentResult(PaymentStatus paymentStatus, String message){
        this.paymentStatus = paymentStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public PaymentStatus getPaymentStatus(){
        return paymentStatus;
    }
}
