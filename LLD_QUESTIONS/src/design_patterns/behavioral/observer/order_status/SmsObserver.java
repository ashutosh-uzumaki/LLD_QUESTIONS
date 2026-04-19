package design_patterns.behavioral.observer.order_status;

public class SmsObserver implements OrderObserver{
    @Override
    public void update(){
        System.out.println("ORDER STATUS UPDATED");
    }
}
