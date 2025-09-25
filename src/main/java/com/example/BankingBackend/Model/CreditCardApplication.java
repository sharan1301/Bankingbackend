package com.example.BankingBackend.Model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_card_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_apply_seq_gen")
    @SequenceGenerator(name = "credit_apply_seq_gen", sequenceName = "credit_apply_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String applicationId;   // e.g., CRD20250916862

    // --- Foreign Key (Account) ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(
            name = "ACCOUNT_ID",
            referencedColumnName = "ACCOUNT_ID",
            foreignKey = @ForeignKey(name = "FK_CC_APP_ACCOUNT"),
            nullable = false
    )
    private Account account;

    // --- Transient field to accept account number from JSON ---
    @Transient
    private Long accountNumber;

    // --- Personal Information ---
    private String fullName;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String panNumber;

    @Column(nullable = false)
    private String aadhaarNumber;

    private String occupation;
    private Double annualIncome;

    // --- Card Preferences ---
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // --- Status ---
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(nullable = true)
    private String remarks;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;

    // --------- ENUMS ---------
    public enum ApplicationStatus {
        UNDER_REVIEW, APPROVED, REJECTED
    }

    public enum CardType {
        PLATINUM, GOLD, CLASSIC
    }

    public enum PaymentMethod {
        AUTO, MANUAL
    }

    @PrePersist
    public void generateApplicationId() {
        if (this.applicationId == null || this.applicationId.isEmpty()) {
            this.applicationId = "CRD" + System.currentTimeMillis();
        }
    }
}
