package design_patterns.behavioral.strategy.payment_strategy;

import java.math.BigDecimal;

public class DebitCard implements PaymentStrategy{
    @Override
    public void pay(BigDecimal amount){
        System.out.println("Processed the payment");
    }
}
