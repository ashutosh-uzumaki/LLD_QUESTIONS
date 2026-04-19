package design_patterns.structural.adapter;

public class RazorpayAdapter implements PaymentProcessor{
    private RazaorpaySDK razaorpaySDK = new RazaorpaySDK();
    @Override
    public void makePayment(double amount){
        int paise = (int)(amount * 100);
        razaorpaySDK.processPayment(paise);
    }
}
