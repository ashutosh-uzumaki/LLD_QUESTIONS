package BookMyShow.service;

import java.math.BigDecimal;

public interface PaymentMethod {
    void pay(BigDecimal amount);
}
