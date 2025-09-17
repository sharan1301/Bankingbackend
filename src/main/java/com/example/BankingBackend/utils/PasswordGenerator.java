package com.example.BankingBackend.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";
    private static final int MIN_LENGTH = 12; // Minimum password length

    public static String generatePassword(String firstName) {
        SecureRandom random = new SecureRandom();


        String namePart = firstName.length() >= 3 ? firstName.substring(0, 3).toUpperCase() : firstName.toUpperCase();


        String base = namePart + "ORA";


        int remainingLength = MIN_LENGTH - base.length();

        StringBuilder password = new StringBuilder(base);
        for (int i = 0; i < remainingLength; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }
}
