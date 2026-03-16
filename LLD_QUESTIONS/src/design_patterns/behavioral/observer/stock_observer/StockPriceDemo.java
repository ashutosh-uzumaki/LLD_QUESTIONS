package design_patterns.behavioral.observer.stock_observer;

public class StockPriceDemo {
    public static void main(String[] args) {
        Stock reliance = new Stock("Reliance");
        StockObserver ashutosh = new StockSubscriber("Ashutosh");
        StockObserver ankita = new StockSubscriber("Ankita");
        reliance.register(ashutosh);
        reliance.register(ankita);
        reliance.updateStockPrice();
    }
}
