package com.example.BankingBackend.Service.Exception;

public class FDAccountDoesNotExistException extends RuntimeException {
    public FDAccountDoesNotExistException(String message) {
        super(message);
    }
}
