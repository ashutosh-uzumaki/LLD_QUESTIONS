package oops.abstraction;

import java.math.BigDecimal;

public interface PaymentProcessor {
    PaymentResult makePayment(BigDecimal amount, String payer, String receiver);
}
