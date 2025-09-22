package com.example.BankingBackend.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserAccountDto {
    private int userId;
    private Long accountId;
    private String firstName;
    private String lastName;
    private Long accountNumber;
    private String accountType;
    private double balance;
    private String cardStatus;
    private boolean frozen;
    private boolean pendingLoan;
    private Account.AccountStatus status;
    private String email;
    private String phone;

    public UserAccountDto(
            int userId,
            Long accountId,
            String firstName,
            String lastName,
            Long accountNumber,
            String accountType,
            double balance,
            String cardStatus,
            boolean frozen,
            boolean pendingLoan,
            Account.AccountStatus status,
            String email,
            String phone
    ) {
        this.userId = userId;
        this.accountId=accountId;
        this.firstName = firstName;
        this.lastName=lastName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.cardStatus = cardStatus;
        this.frozen = frozen;
        this.pendingLoan = pendingLoan;
        this.status = status;
        this.email = email;
        this.phone = phone;
    }

}
