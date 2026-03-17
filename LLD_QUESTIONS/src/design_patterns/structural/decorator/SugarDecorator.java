package design_patterns.structural.decorator;

import java.math.BigDecimal;

public class SugarDecorator extends CoffeeDecorator{
    public SugarDecorator(Coffee coffee){
        super(coffee);
    }

    @Override
    public BigDecimal getPrice(){
        return coffee.getPrice().add(new BigDecimal("0.1"));
    }

    @Override
    public String getDescription(){
        return coffee.getDescription()+" sugar";
    }
}
