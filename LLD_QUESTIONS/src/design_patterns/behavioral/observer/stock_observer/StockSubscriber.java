package design_patterns.behavioral.observer.stock_observer;

public class StockSubscriber implements StockObserver{
    private final String name;
    public StockSubscriber(String name){
        this.name = name;
    }

    @Override
    public void update(String message){
        System.out.println(message);
    }
}
