package com.example.BankingBackend.Model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_requests")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_requests_seq")
    @SequenceGenerator(name = "user_requests_seq", sequenceName = "USER_REQUESTS_SEQ", allocationSize = 1)
    private int requestId;


    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name="email",nullable = false,  length = 150)
    private String email;

    @Column(name="occupation",length = 100)
    private String occupation;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;

    @Column( name="phone" ,nullable = false,length = 15)
    private String phone;

    @Column(name = "aadhaar_number", nullable = false,  length = 12)
    private String aadhaarNumber;

    @Column(name = "pan_number", nullable = false,  length = 10)
    private String panNumber;

    @Column(name = "annual_income")
    private Double annualIncome;


    @Column(name="status",length = 20)
    private String status = "PENDING";

    @Column(name = "request_date")
    private LocalDate requestDate = LocalDate.now();

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getPhone() {
        return phone;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }
}
