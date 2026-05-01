package BookMyShow.service;

import java.math.BigDecimal;

public class UPIPayment implements  PaymentMethod{
    @Override
    public void pay(BigDecimal amount){
        System.out.println("Payment Done!!");
    }
}
