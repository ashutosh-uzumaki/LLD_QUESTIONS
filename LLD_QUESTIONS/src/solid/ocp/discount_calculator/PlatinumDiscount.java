package solid.ocp.discount_calculator;

import java.math.BigDecimal;

public class PlatinumDiscount implements DiscountCalculator {
    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("0.10");
    @Override
    public BigDecimal calculateDiscount(BigDecimal amount){
        BigDecimal discountedAmount = amount.multiply(DISCOUNT_PERCENT);
        return discountedAmount;
    }
}
