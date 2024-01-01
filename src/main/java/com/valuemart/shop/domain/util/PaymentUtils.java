package com.valuemart.shop.domain.util;

import java.util.concurrent.ThreadLocalRandom;

public class PaymentUtils {

    public static String generateTransRef() {
        // Current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Extract the last 7 digits of the current time
        String timePart = Long.toString(currentTimeMillis).substring(10);

        System.out.println(timePart);

        // Generate a random number between 100 and 999 (inclusive)
        int randomNumber = ThreadLocalRandom.current().nextInt(100, 1000);

        System.out.println(randomNumber);

        // Combine time part and random number
        return "TXN" + timePart + randomNumber;
    }

    public static void main(String[] args) {
        System.out.println(generateTransRef());
    }
}