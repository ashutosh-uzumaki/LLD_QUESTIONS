package design_patterns.behavioral.observer.order_status;

public class EmailObserver implements OrderObserver {
    @Override
    public void update(){
        System.out.println("ORDER STATUS CHANGED");
    }
}
