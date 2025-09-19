package com.example.BankingBackend.Model;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LOAN_REQUEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_req_seq")
    @SequenceGenerator(name = "loan_req_seq", sequenceName = "LOAN_REQ_SEQ", allocationSize = 1)
    @Column(name = "REQUEST_ID", nullable = false)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_REQUEST_USER"))
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOAN_TYPE", nullable = false, length = 50)
    private LoanType loanType;

    @Column(name = "LOAN_AMOUNT", nullable = false, precision = 15, scale = 2)
    private Double loanAmount;

    @Column(name = "TENURE_MONTHS", nullable = false)
    private Integer tenureMonths;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    public enum LoanType {
        HOME_LOAN, PERSONAL_LOAN, CAR_LOAN, EDUCATION_LOAN, BUSINESS_LOAN, GOLD_LOAN
    }

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }
}



