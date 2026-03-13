package solid.lsp.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Loan {
    private BigDecimal principal;
    private final LocalDate loanStartDate;
    public Loan(BigDecimal principal, LocalDate loanStartDate){
        this.principal = principal;
        this.loanStartDate = loanStartDate;
    }
    public BigDecimal getPrincipal() {
        return principal;
    }

    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public abstract PaymentResult prepayLoan(BigDecimal amount);
}
