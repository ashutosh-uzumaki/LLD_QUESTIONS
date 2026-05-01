package BookMyShow.service;

import java.math.BigDecimal;

public class CreditCardPaymet implements PaymentMethod{
    @Override
    public void pay(BigDecimal amount){
        System.out.println("Payment made via Credit Card!!");
    }
}
