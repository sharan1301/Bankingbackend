package com.example.BankingBackend.utils;

public class AccountNumberGenerator {
    public static Long generateAccountNumber(int userId) {

        return 1000000000L + userId;
    }
}
