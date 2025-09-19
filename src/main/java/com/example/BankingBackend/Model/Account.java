package com.example.BankingBackend.Model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ACCOUNTS")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "ACCOUNT_SEQ", allocationSize = 1)
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ACCOUNT_USER"))
    private Users user;

    @Column(name = "ACCOUNT_NUMBER", nullable = false, unique = true)
    private Long accountNumber;

    @Column(name = "ACCOUNT_TYPE", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "BALANCE", precision = 15)
    private Double balance;

    @Column(name="PIN" , length = 10)
    private String pin;

    @Column(name = "BRANCH_CODE", length = 10)
    private String branchCode;

    @Column(name = "IFSC_CODE", length = 15)
    private String ifscCode;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "MINIMUM_BALANCE", precision = 10)
    private Double minimumBalance;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FixedDeposit> fixedDeposits;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecurringDeposit> recurringDeposits;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payee> payees;

    public enum AccountType {
        SAVINGS, CURRENT, SALARY, NRI, JOINT
    }

    public enum AccountStatus {
        ACTIVE, CLOSED, FROZEN
    }
}
