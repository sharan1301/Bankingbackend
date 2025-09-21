package com.example.BankingBackend.Model;

import javax.persistence.*;
//import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "FIXED_DEPOSIT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fd_seq")
    @SequenceGenerator(name = "fd_seq", sequenceName = "FD_SEQ", allocationSize = 1)
    @Column(name = "FD_ID", nullable = false, precision = 10)
    private Long fdId;

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
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_FD_ACCOUNT"))

    private Account account;

    @Column(name = "DEPOSIT_AMOUNT", precision = 15, scale = 2)

    private Double depositAmount;

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

    @Column(name = "TENURE_MONTHS", nullable = false, precision = 10)

    private Integer tenureMonths;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public enum DepositStatus {
        ACTIVE, MATURED, CLOSED, PREMATURE_CLOSURE
    }
}