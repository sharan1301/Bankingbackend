package com.example.BankingBackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LOAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq")
    @SequenceGenerator(name = "loan_seq", sequenceName = "LOAN_SEQ", allocationSize = 1)
    @Column(name = "LOAN_ID", nullable = false)
    private Long loanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LOAN_USER"))
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LOAN_ACCOUNT"))
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOAN_TYPE", nullable = false, length = 50)
    private LoanType loanType;

    @Column(name = "LOAN_AMOUNT", nullable = false, precision = 15, scale = 2)
    private Double loanAmount;

    @Column(name = "INTEREST_RATE", nullable = false, precision = 5, scale = 2)
    private Double interestRate;

    @Column(name = "TENURE_MONTHS", nullable = false)
    private Integer tenureMonths;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;

    @Column(name = "EMI", nullable = false)
    private Double emi;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private LoanStatus status = LoanStatus.ACTIVE;

    public enum LoanType {
        HOME_LOAN, PERSONAL_LOAN, CAR_LOAN, EDUCATION_LOAN, BUSINESS_LOAN, GOLD_LOAN
    }

    public enum LoanStatus {
        ACTIVE, CLOSED, DEFAULTED
    }

    public Users getUser() {
        return user;
    }
}
