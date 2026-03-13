package oops.abstraction;

import java.math.BigDecimal;


import static oops.abstraction.PaymentStatus.FAILURE;
import static oops.abstraction.PaymentStatus.SUCCESS;

public class UpiPaymentProcessor implements PaymentProcessor{
    @Override
    public PaymentResult makePayment(BigDecimal amount, String payer, String receiver){

        System.out.println("Making Payment via UPI");
        if(!validateUPI(payer)){
            return new PaymentResult(FAILURE, "Payer is not valid");
        }
        if(!validateUPI(receiver)){
            return new PaymentResult(FAILURE, "Receiver is not valid");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            return new PaymentResult(FAILURE, "Amount is not valid");
        }
        return new PaymentResult(SUCCESS, "Payment is successful");
    }

    private boolean validateUPI(String upi){
        if(upi == null || upi.trim().isEmpty() || !upi.contains("@")){
            return false;
        }
        String[] parts = upi.split("@");
        return parts[0].length() >= 5 && parts[1].length() >= 5;
    }
}
