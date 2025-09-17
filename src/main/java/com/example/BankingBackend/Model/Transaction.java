package com.example.BankingBackend.Model;

import javax.persistence.*;
//import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    @Column(name = "TRANSACTION_ID", nullable = false, precision = 20)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT"))

    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT"))

    private Account recvAcc;

    @Column(name = "TRANSACTION_TYPE", nullable = false, length = 50)

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)

    private Double amount;

    @Column(name = "BALANCE_AFTER", precision = 15, scale = 2)

    private Double balanceAfter;

    @Column(name = "TRANSACTION_DATE")
    @CreationTimestamp
    private LocalDateTime transactionDate;

    @Column(name = "DESCRIPTION", length = 500)

    private String description;

    @Column(name = "REFERENCE_NUMBER", length = 50, unique = true)

    private String referenceNumber;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.SUCCESS;



    public enum TransactionType {
        DEBIT, CREDIT, TRANSFER_OUT, TRANSFER_IN, ATM_WITHDRAWAL,
        FD_OPENING, FD_CLOSURE, RD_INSTALLMENT, LOAN_DISBURSEMENT,
        LOAN_PAYMENT, INTEREST_CREDIT, CHARGES_DEBIT,NEFT,RTGS,IMPS
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, REVERSED
    }


}
