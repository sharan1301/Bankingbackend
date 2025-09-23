package com.example.BankingBackend.Model;


import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Autopay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autopay_seq_gen")
    @SequenceGenerator(name = "autopay_seq_gen", sequenceName = "autopay_seq", allocationSize = 1)
    @Column(name = "autopay_id")
    private Long autopayId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_payee_id")
    private Payee receiver;

    private Double amount;
    private Long frequencySeconds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastExecuted;

    private boolean active = true;

    // getters and setters

    public Long getAutopayId() {
        return autopayId;
    }

    public void setAutopayId(Long autopayId) {
        this.autopayId = autopayId;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Payee getReceiver() {
        return receiver;
    }

    public void setReceiver(Payee receiver) {
        this.receiver = receiver;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getFrequencySeconds() {
        return frequencySeconds;
    }

    public void setFrequencySeconds(Long frequencySeconds) {
        this.frequencySeconds = frequencySeconds;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(LocalDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
