package com.example.BankingBackend.Model;

import javax.persistence.*;
//import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.ForeignKey;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq")
    @SequenceGenerator(name = "loan_seq", sequenceName = "LOAN_SEQ", allocationSize = 1)
    @Column(name = "LOAN_ID", nullable = false, precision = 10)
    private Long loanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LOAN_USER"))

    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LOAN_ACCOUNT"))

    private Account account;

    @Column(name = "LOAN_TYPE", nullable = false, length = 50)

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Column(name = "LOAN_AMOUNT", nullable = false, precision = 15, scale = 2)

    private Double loanAmount;

    @Column(name = "INTEREST_RATE", nullable = false, precision = 5, scale = 2)

    private Double interestRate;

    @Column(name = "TENURE_MONTHS", nullable = false, precision = 10)

    private Integer tenureMonths;

    @Column(name = "START_DATE", nullable = false)

    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;


    @Column(name = "EMI")
    private Double emi;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public enum LoanType {
        HOME_LOAN, PERSONAL_LOAN, CAR_LOAN, EDUCATION_LOAN, BUSINESS_LOAN, GOLD_LOAN
    }

    public enum LoanStatus {
        PENDING, APPROVED, ACTIVE, CLOSED, DEFAULTED, REJECTED
    }
}