package com.example.BankingBackend.utils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class CustomerIdGenerator {
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*";
    private static final SecureRandom random = new SecureRandom();

    private static final Set<String> generatedIds = new HashSet<>();

    public static String generateUniqueId(String firstName, int maxLength) {
        String uniqueId;
        do {

            String namePart = firstName != null ? firstName.trim() : "User";
            namePart = namePart.length() >= 4 ? namePart.substring(0, 4)
                    : namePart.substring(0, Math.min(3, namePart.length()));

            int remainingLength = maxLength - namePart.length();
            if (remainingLength < 2) {
                remainingLength = 2; // minimum randomness
            }

            String pool = NUMBERS + SYMBOLS;
            StringBuilder suffix = new StringBuilder();
            for (int i = 0; i < remainingLength; i++) {
                suffix.append(pool.charAt(random.nextInt(pool.length())));
            }

            uniqueId = namePart + suffix.toString();

        } while (generatedIds.contains(uniqueId));

        generatedIds.add(uniqueId);
        return uniqueId;
    }

}

