package mini_project.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class securityutil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LEN = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LEN = 256; // bits

    public static String generateSalt() {
        byte[] s = new byte[SALT_LEN];
        RANDOM.nextBytes(s);
        return Base64.getEncoder().encodeToString(s);
    }

    public static String hashPassword(String password, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new javax.crypto.spec.PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return Base64.getEncoder().encodeToString(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKeySpec deriveAESKey(String password, String saltBase64) throws Exception {
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new javax.crypto.spec.PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN);
        SecretKey tmp = skf.generateSecret(spec);
        byte[] aesKey = tmp.getEncoded();
        return new SecretKeySpec(aesKey, "AES");
    }

    public static String encryptAES(String plainText, String password, String saltBase64) {
        try {
            SecretKeySpec key = deriveAESKey(password, saltBase64);
            byte[] iv = new byte[16];
            RANDOM.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] ct = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(ct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptAES(String combined, String password, String saltBase64) {
        try {
            String[] parts = combined.split(":");
            if (parts.length != 2) return null;
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] ct = Base64.getDecoder().decode(parts[1]);
            SecretKeySpec key = deriveAESKey(password, saltBase64);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] plain = cipher.doFinal(ct);
            return new String(plain, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
