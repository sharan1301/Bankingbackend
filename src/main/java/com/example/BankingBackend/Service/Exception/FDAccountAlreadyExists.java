package com.example.BankingBackend.Service.Exception;

public class FDAccountAlreadyExists extends RuntimeException {
    public FDAccountAlreadyExists(String message) {
        super(message);
    }
}
