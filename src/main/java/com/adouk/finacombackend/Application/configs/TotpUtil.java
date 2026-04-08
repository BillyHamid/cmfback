package com.adouk.finacombackend.Application.configs;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class TotpUtil {

    // Génère une clé secrète aléatoire
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20]; // 160 bits
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        System.out.println("Secret Key: " + secretKey);
        return secretKey;
    }

    // Génère un QR Code au format otpauth://totp
    public static byte[] generateQRCode(String appName, String email, String secretKey) throws WriterException, IOException {
        String otpAuthURL = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                appName, email, generateSecretKey(), appName
        );
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    public static boolean validateCode(String secretKey, int code) {
        long time = System.currentTimeMillis() / 1000 / 30; // Intervalle de 30 secondes
        Base32 base32 = new Base32();
        byte[] key = base32.decode(secretKey);

        // Valide le code pour l'intervalle actuel et les intervalles adjacents (±1)
        for (int i = -1; i <= 1; i++) {
            long hash = generateTOTP(key, time + i);
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    // Génère un code TOTP
    private static long generateTOTP(byte[] key, long time) {
        byte[] data = new byte[8];
        for (int i = 8; i-- > 0; time >>>= 8) {
            data[i] = (byte) time;
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0xF;
            long truncatedHash = 0;
            for (int i = 0; i < 4; i++) {
                truncatedHash <<= 8;
                truncatedHash |= (hash[offset + i] & 0xFF);
            }
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= 1000000; // 6 chiffres
            return truncatedHash;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}