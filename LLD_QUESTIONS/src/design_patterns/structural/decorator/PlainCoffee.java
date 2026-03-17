package design_patterns.structural.decorator;

import java.math.BigDecimal;

public class PlainCoffee implements Coffee {
    private final BigDecimal price;
    private final String description;

    public PlainCoffee(){
        this.price = new BigDecimal("100");
        this.description = "This is the plain coffee";
    }
    @Override
    public BigDecimal getPrice(){
        return price;
    }
    @Override
    public String getDescription(){
        return description;
    }
}
