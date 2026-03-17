package design_patterns.structural.decorator;

public class CoffeeDemo {
    public static void main(String[] args) {
        Coffee coffee = new ColdCoffee();
        coffee = new WhippedCreamDecorator(coffee);
        coffee = new SugarDecorator(coffee);

        System.out.println("Coffee price is: "+coffee.getPrice()+" with item: "+coffee.getDescription());
    }
}
