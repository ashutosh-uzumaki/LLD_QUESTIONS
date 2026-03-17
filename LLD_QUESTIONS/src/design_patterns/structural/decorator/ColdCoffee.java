package design_patterns.structural.decorator;

import java.math.BigDecimal;

public class ColdCoffee implements Coffee{
    private final BigDecimal price;
    private final String description;

    public ColdCoffee(){
        this.price = new BigDecimal("220");
        this.description = "Plain Cold Coffee";
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
