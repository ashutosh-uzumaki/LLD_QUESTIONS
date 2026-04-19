package design_patterns.behavioral.observer.order_status;

public interface OrderSubject {
    void register(OrderObserver observer);
    void remove(OrderObserver observer);
}
