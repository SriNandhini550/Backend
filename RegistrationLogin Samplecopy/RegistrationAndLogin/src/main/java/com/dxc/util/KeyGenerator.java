package com.dxc.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static String generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(keyBytes);

        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static void main(String[] args) {
        String key = generateKey();
        System.out.println("Generated Key: " + key);
    }
}