package solid.srp.payment;

import java.math.BigDecimal;

public class PaymentService {
    private final ReceiptGenerator receiptGenerator;
    private final Notification notification;
    private final PaymentRepository paymentRepository;
    private final PaymentValidator paymentValidator;
    public PaymentService(ReceiptGenerator receiptGenerator, Notification notification, PaymentRepository paymentRepository, PaymentValidator paymentValidator){
        this.receiptGenerator = receiptGenerator;
        this.notification = notification;
        this.paymentRepository = paymentRepository;
        this.paymentValidator = paymentValidator;
    }

    public PaymentResult makePayment(String payee, String receiver, BigDecimal amount){
        if(!paymentValidator.validate(payee, receiver, amount)){
            return new PaymentResult(PaymentStatus.FAILURE, "Payment check validation failed");
        }
        Payment payment = new Payment(payee, receiver, amount);
        paymentRepository.save(payment);
        receiptGenerator.generate(payment);
        notification.send(payment);

        return new PaymentResult(PaymentStatus.SUCCESS, "Transaction is successful");
    }

}
