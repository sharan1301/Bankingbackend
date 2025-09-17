// Creating a custom exception that can be thrown when a user tries to add a customer that already exists
package com.example.BankingBackend.Service.Exception;

public class UserRequestNotFound extends RuntimeException {
    private String message;

    public UserRequestNotFound() {}

    public UserRequestNotFound(String msg) {
        super(msg);
        this.message = msg;
    }
}