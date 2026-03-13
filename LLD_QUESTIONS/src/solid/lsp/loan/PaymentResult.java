package solid.lsp.loan;

import oops.abstraction.PaymentStatus;

public class PaymentResult {
    private final PaymentStatus paymentStatus;
    private final String message;

    public PaymentResult(PaymentStatus paymentStatus, String message){
        this.paymentStatus = paymentStatus;
        this.message = message;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getMessage() {
        return message;
    }
}
