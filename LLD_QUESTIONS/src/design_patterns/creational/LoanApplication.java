package design_patterns.creational;

import java.math.BigDecimal;

public class LoanApplication {
    private final String applicantName;
    private final BigDecimal loanAmount;
    private final LoanType loanType;
    private final int tenure;
    private final BigDecimal interestRate;
    private final BigDecimal collateralValue;
    private final String coApplicantName;
    private final String remarks;

    private LoanApplication(Builder builder) {
        this.applicantName = builder.applicantName;
        this.loanAmount = builder.loanAmount;
        this.loanType = builder.loanType;
        this.tenure = builder.tenure;
        this.interestRate = builder.interestRate;
        this.collateralValue = builder.collateralValue;
        this.coApplicantName = builder.coApplicantName;
        this.remarks = builder.remarks;
    }

    public static Builder builder(String applicantName, BigDecimal loanAmount, LoanType loanType) {
        return new Builder(applicantName, loanAmount, loanType);
    }

    public static class Builder {
        // Required — final
        private final String applicantName;
        private final BigDecimal loanAmount;
        private final LoanType loanType;

        // Optional — with defaults
        private int tenure = 12;
        private BigDecimal interestRate = new BigDecimal("10.0");
        private BigDecimal collateralValue;
        private String coApplicantName;
        private String remarks;

        private Builder(String applicantName, BigDecimal loanAmount, LoanType loanType) {
            this.applicantName = applicantName;
            this.loanAmount = loanAmount;
            this.loanType = loanType;
        }

        public Builder tenure(int tenure) {
            this.tenure = tenure;
            return this;
        }

        public Builder interestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public Builder collateralValue(BigDecimal collateralValue) {
            this.collateralValue = collateralValue;
            return this;
        }

        public Builder coApplicantName(String coApplicantName) {
            this.coApplicantName = coApplicantName;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public LoanApplication build() {
            return new LoanApplication(this);
        }
    }

    public static void main(String[] args) {
        LoanApplication app1 = LoanApplication.builder("Ashutosh", new BigDecimal("500000"), LoanType.GOLD)
                .tenure(24)
                .collateralValue(new BigDecimal("600000"))
                .interestRate(new BigDecimal("9.5"))
                .remarks("Urgent processing")
                .build();

        LoanApplication app2 = LoanApplication.builder("Priya", new BigDecimal("200000"), LoanType.PERSONAL)
                .build();
    }
}