package design_patterns.behavioral.observer.order_status;

public class OrderNotifyDemo {
    public static void main(String[] args) {
        OrderObserver a = new EmailObserver();
        OrderObserver b = new SmsObserver();
        Order one = new Order("100");
        one.register(a);
        one.setOrderStatus("PLACED");
        one.register(b);
        one.setOrderStatus("PACKED AND READY FOR SHIPPING");
    }
}
