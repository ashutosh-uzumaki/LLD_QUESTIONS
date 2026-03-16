package design_patterns.behavioral.observer.stock_observer;

import design_patterns.behavioral.observer.youtube.Observer;

import java.util.ArrayList;
import java.util.List;

public class Stock implements StockSubject{
    private List<StockObserver> observers = new ArrayList<>();
    private String name;
    public Stock(String name){
        this.name = name;
    }

    @Override
    public void register(StockObserver observer){
        observers.add(observer);
    }

    @Override
    public void remove(StockObserver observer){
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(){
        for(StockObserver observer: observers){
            observer.update("Stock of the price updated!!");
        }
    }

    public void updateStockPrice(){
        System.out.println("Stock price updated!!");
        notifyObservers();
    }
}
