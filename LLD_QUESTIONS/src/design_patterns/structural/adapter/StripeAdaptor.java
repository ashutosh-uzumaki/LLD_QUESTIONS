package design_patterns.structural.adapter;

import java.math.BigDecimal;

public class StripeAdaptor implements PaymentProcessor{

    private StripSDK stripSDK = new StripSDK();

    @Override
    public void makePayment(double amount){
        BigDecimal cents = BigDecimal.valueOf(amount * 100);
        stripSDK.pay(cents);
    }
}
