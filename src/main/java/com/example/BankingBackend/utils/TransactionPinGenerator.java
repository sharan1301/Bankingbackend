package com.example.BankingBackend.utils;

import java.security.SecureRandom;

public class TransactionPinGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSixDigitPin() {
        int value = secureRandom.nextInt(1_000_000);
        return String.format("%06d", value);
    }

}
