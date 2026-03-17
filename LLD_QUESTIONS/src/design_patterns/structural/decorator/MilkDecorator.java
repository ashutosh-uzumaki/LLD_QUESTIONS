package design_patterns.structural.decorator;

import java.math.BigDecimal;

public class MilkDecorator extends CoffeeDecorator{
    public MilkDecorator(Coffee coffee){
        super(coffee);
    }

    @Override
    public BigDecimal getPrice(){
        return  coffee.getPrice().add(new BigDecimal("20"));
    }

    @Override
    public String getDescription(){
        return coffee.getDescription()+" milk added";
    }
}
