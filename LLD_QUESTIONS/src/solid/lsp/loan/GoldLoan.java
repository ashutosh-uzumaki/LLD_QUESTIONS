package solid.lsp.loan;

import oops.abstraction.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class GoldLoan extends Loan{
    public GoldLoan(BigDecimal principal, LocalDate loanStartDate){
        super(principal, loanStartDate);
    }
    @Override
    public PaymentResult prepayLoan(BigDecimal amount){
        LocalDate loanStartDate = this.getLoanStartDate();
        long totalMonths = ChronoUnit.MONTHS.between(loanStartDate, LocalDate.now());
        if(totalMonths < 3){
            return new PaymentResult(PaymentStatus.FAILURE, "Gold loan cannot be prepaid before 3 months");
        }
        BigDecimal principal = this.getPrincipal();
        principal = principal.subtract(amount);
        this.setPrincipal(principal);
        return new PaymentResult(PaymentStatus.SUCCESS, "Gold loan prepaid");
    }
}
