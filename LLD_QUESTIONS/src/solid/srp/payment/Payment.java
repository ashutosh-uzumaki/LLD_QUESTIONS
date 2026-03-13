package solid.srp.payment;

import java.math.BigDecimal;
import java.util.UUID;

public class Payment {
    private final String payee;
    private final String receiver;
    private final BigDecimal amount;
    private final String transactioId;

    public Payment(String payee, String receiver, BigDecimal amount){
        this.payee = payee;
        this.receiver = receiver;
        this.amount = amount;
        this.transactioId = UUID.randomUUID().toString();
    }

    public String getPayee() {
        return payee;
    }

    public String getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactioId() {
        return transactioId;
    }
}
