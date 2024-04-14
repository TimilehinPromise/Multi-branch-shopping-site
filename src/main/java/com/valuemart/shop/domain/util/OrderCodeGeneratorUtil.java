package com.valuemart.shop.domain.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OrderCodeGeneratorUtil {

    public static String generateOrderCode(long orderId) {
        try {
            byte[] bytesOfMessage = String.valueOf(orderId).getBytes(StandardCharsets.UTF_8);

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] thedigest = md.digest(bytesOfMessage);

            String hex = bytesToHex(thedigest);

            long num = Long.parseLong(hex.substring(0, 15), 16);
            String encodedString = Long.toString(num, 36).toUpperCase();

            return encodedString.substring(0, Math.min(7, encodedString.length()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 digest algorithm not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Example usage
        int orderId = 200;
        System.out.println("Generated Order Code: " + generateOrderCode(orderId));
    }
}
