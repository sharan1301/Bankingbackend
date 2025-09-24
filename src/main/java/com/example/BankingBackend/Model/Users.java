package com.example.BankingBackend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_seq", allocationSize = 1)
    private int userId;

    @Column(name = "CUSTID", length = 15)
    private String custId;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "aadhaar_number", nullable = false, length = 12)
    private String aadhaarNumber;

    @Column(name = "pan_number", nullable = false, length = 10)
    private String panNumber;

    @Column(name = "account_type", nullable = false, length = 100)
    private String accountType;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "annual_income")
    private Double annualIncome;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "status", length = 20)
    private String status; // APPROVED

    // --- Relation with Loan Requests ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LoanRequests> loanRequests;

    // --- Relation with Fixed Deposits ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FixedDeposit> fixedDeposits;

    // --- Relation with Recurring Deposits ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RecurringDeposit> recurringDeposits;

    public String getCustId() {
        return custId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
