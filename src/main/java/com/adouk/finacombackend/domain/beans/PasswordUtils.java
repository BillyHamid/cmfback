package com.adouk.finacombackend.domain.beans;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    // Generate a random password
    public static String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    // Encrypt a password
    public static String encryptPassword(String password, String secretKey) throws Exception {
        Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
    public static String generateSecurityKey() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[16]; // 16 bytes for 128-bit key
        secureRandom.nextBytes(keyBytes);
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);
        return secretKey;
    }

    // Decrypt a password
    public static String decryptPassword(String encryptedPassword, String secretKey) throws Exception {
        Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decrypted);
    }
}
