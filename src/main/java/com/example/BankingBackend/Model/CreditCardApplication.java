package com.example.BankingBackend.Model;

import javax.persistence.*;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    // --- Personal Information ---
    private String fullName;
    private String dateOfBirth;
    private String gender;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String panNumber;

    @Column(nullable = false)
    private String aadhaarNumber;

    private String pincode;
    private String address;
    private String city;
    private String state;

    private String occupation;
    private Double annualIncome;
    private String employer;

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

    // --------- ENUMS INSIDE SAME FILE ---------

    public enum ApplicationStatus {
        UNDER_REVIEW, APPROVED, REJECTED
    }

    public enum CardType {
        PLATINUM, GOLD, CLASSIC
    }

    @PrePersist
    public void generateApplicationId() {
        if (this.applicationId == null || this.applicationId.isEmpty()) {
            this.applicationId = "CRD" + System.currentTimeMillis();
        }
    }


    public enum PaymentMethod {
        AUTO, MANUAL
    }
}