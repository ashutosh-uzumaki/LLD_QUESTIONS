package design_patterns.behavioral.strategy.payment_strategy;

import design_patterns.structural.adapter.PaymentProcessor;

import java.math.BigDecimal;

public class UPIPayment implements PaymentStrategy {
    @Override
    public void pay(BigDecimal amount){
        System.out.println("Payment processed");
    }
}
