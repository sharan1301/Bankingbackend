package com.example.BankingBackend.Model;

import com.example.BankingBackend.Model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "SENDER_ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT"))

    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "RECEIVER_ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT"))

    private Account recvAcc;

    @Column(name = "TRANSACTION_TYPE", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "TRANSACTION_MODE", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TransactionMode transactionMode;

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
        PAYEE, BANK, TRANSFER, SELF
    }

    public enum TransactionMode {
        NEFT, RTGS, IMPS, AUTOPAY
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, INSUFFICIENT_FUNDS, REVERSED
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getRecvAcc() {
        return recvAcc;
    }

    public void setRecvAcc(Account recvAcc) {
        this.recvAcc = recvAcc;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(TransactionMode transactionMode) {
        this.transactionMode = transactionMode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}