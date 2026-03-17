package design_patterns.structural.decorator;

import java.math.BigDecimal;

public class WhippedCreamDecorator extends CoffeeDecorator{
    public WhippedCreamDecorator(Coffee coffee){
        super(coffee);
    }

    @Override
    public BigDecimal getPrice(){
        return coffee.getPrice().add(new BigDecimal("20"));
    }

    @Override
    public String getDescription(){
        return coffee.getDescription()+" whipped cream";
    }
}
