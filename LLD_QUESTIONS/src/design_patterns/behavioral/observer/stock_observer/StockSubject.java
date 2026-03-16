package design_patterns.behavioral.observer.stock_observer;

public interface StockSubject {
    void register(StockObserver stockObserver);
    void remove(StockObserver stockObserver);
    void notifyObservers();
}
