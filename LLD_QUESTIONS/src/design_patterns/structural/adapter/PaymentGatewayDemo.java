package design_patterns.structural.adapter;

public class PaymentGatewayDemo {
    public static void main(String[] args) {
        StripeAdaptor stripeAdaptor = new StripeAdaptor();
        stripeAdaptor.makePayment(100000);
    }
}
