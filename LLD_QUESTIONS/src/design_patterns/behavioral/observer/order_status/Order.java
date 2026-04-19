package design_patterns.behavioral.observer.order_status;

import java.util.ArrayList;
import java.util.List;

public class Order implements OrderSubject{
    private final String orderId;
    private String orderStatus;
    private List<OrderObserver> observers = new ArrayList<>();

    public Order(String orderId){
        this.orderId = orderId;
    }

    @Override
    public void register(OrderObserver observer){
        observers.add(observer);
    }

    @Override
    public void remove(OrderObserver observer){
        observers.remove(observer);
    }

    
    public void notifyObservers(){
        for(OrderObserver observer: observers){
            observer.update();
        }
    }

    public void setOrderStatus(String status){
        this.orderStatus = status;
        notifyObservers();
    }
}
