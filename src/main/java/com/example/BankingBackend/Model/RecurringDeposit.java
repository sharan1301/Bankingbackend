package com.example.BankingBackend.Model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "RECURRING_DEPOSIT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rd_seq")
    @SequenceGenerator(name = "rd_seq", sequenceName = "RD_SEQ", allocationSize = 1)
    @Column(name = "RD_ID", nullable = false, precision = 10)
    private Long rdId;

    // --- Relation with Account ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({
            "fixedDeposits",
            "recurringDeposits",
            "transactions",
            "loans",
            "cards",
            "payees",
            "hibernateLazyInitializer",
            "handler"
    })
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RD_ACCOUNT"))
    private Account account;

    // --- Relation with User ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({
            "fixedDeposits",
            "recurringDeposits",
            "loanRequests",
            "hibernateLazyInitializer",
            "handler"
    })
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RD_USER"))
    private Users user;

    @Column(name = "MONTHLY_INSTALLMENT", precision = 15, scale = 2)
    private Double monthlyInstallment;

    @Column(name = "INTEREST_RATE", precision = 5, scale = 2)
    private Double interestRate;

    @Column(name = "MATURITY_AMOUNT", precision = 15, scale = 2)
    private Double maturityAmount;

    @Column(name = "MATURITY_DATE")
    private LocalDate maturityDate;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private DepositStatus status = DepositStatus.ACTIVE;

    @Column(name = "TENURE_MONTHS", precision = 10)
    private Integer tenureMonths;

    @Column(name = "TOTAL_DEPOSITED", precision = 15, scale = 2)
    private Double totalDeposited;

    @Column(name = "LAST_INSTALLMENT_DATE")
    private LocalDate lastInstallmentDate;

    @Column(name="FINE_IMPOSED", precision = 10, scale = 2)
    private Double fine = 0.0;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public enum DepositStatus {
        ACTIVE, MATURED, CLOSED, DEFAULTED, PREMATURE_CLOSURE, PAID_WAIT_MATURE
    }
}
