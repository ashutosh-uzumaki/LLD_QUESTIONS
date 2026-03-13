package solid.ocp.discount_calculator;

import java.math.BigDecimal;

public interface DiscountCalculator {
    BigDecimal calculateDiscount(BigDecimal amount);
}
