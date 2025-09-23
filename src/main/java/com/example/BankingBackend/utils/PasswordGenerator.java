package com.example.BankingBackend.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "@#$%&*!";
    private static final String ALL = UPPERCASE + LOWERCASE + DIGITS + SYMBOLS;

    private static final int PASSWORD_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword(String firstName) {
        String namePart = firstName != null && firstName.length() >= 2
                ? firstName.substring(0, 2).toUpperCase()
                : "US";

        String base = namePart + "ORA"; // Prefix with name + ORA

        StringBuilder password = new StringBuilder(base);

        // Ensure required character types
        password.append(randomChar(UPPERCASE));
        password.append(randomChar(LOWERCASE));
        password.append(randomChar(DIGITS));
        password.append(randomChar(SYMBOLS));

        // Fill the rest with random characters until reaching 10
        while (password.length() < PASSWORD_LENGTH) {
            password.append(randomChar(ALL));
        }

        // Shuffle so pattern is unpredictable
        return shuffle(password.toString());
    }

    private static char randomChar(String pool) {
        return pool.charAt(random.nextInt(pool.length()));
    }

    private static String shuffle(String input) {
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return new String(array);
    }
}
