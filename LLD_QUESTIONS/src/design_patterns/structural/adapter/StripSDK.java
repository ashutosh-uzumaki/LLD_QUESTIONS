package design_patterns.structural.adapter;

import java.math.BigDecimal;

public class StripSDK {
    public void pay(BigDecimal amount){
        System.out.println("Amount paid");
    }
}
