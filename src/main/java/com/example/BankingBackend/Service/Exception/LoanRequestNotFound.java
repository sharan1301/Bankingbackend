// Creating a custom exception that can be thrown when a user tries to add a customer that already exists
package com.example.BankingBackend.Service.Exception;

public class LoanRequestNotFound extends RuntimeException {
    private String message;

    public LoanRequestNotFound() {}

    public LoanRequestNotFound(String msg) {
        super(msg);
        this.message = msg;
    }
}