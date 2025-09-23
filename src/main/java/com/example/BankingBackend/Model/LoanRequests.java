package com.example.BankingBackend.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "LOAN_REQUEST")
@Data
public class LoanRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_req_seq")
    @SequenceGenerator(name = "loan_req_seq", sequenceName = "LOAN_REQ_SEQ", allocationSize = 1)
    @Column(name = "REQUEST_ID", nullable = false)
    private Long requestId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_REQUEST_USER"))
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_REQUEST_ACCOUNT"))
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOAN_TYPE", nullable = false, length = 50)
    private LoanType loanType;

    @Column(name = "LOAN_AMOUNT", nullable = false, precision = 15, scale = 2)
    private Double loanAmount;

    @Column(name = "TENURE_MONTHS", nullable = false)
    private Integer tenureMonths;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private RequestStatus status = RequestStatus.PENDING;

    public enum LoanType {
        HOME_LOAN, PERSONAL_LOAN, CAR_LOAN, EDUCATION_LOAN
    }

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public RequestStatus getStatus() {

        return status;
    }


}
