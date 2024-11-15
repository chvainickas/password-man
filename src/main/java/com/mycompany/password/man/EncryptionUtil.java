/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author pylyp
 */

public class EncryptionUtil {

    static String hashPassword(String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private final SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public EncryptionUtil(String masterPassword) throws Exception {
        byte[] key = generateKey(masterPassword);
        this.secretKey = new SecretKeySpec(key, "AES");
    }

    private byte[] generateKey(String masterPassword) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        return sha.digest(masterPassword.getBytes("UTF-8"));
    }

    public String encrypt(String password) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // Generate random IV (salt)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(password.getBytes("UTF-8"));

        // Concatenate IV and encrypted password
        byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
        System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedWithIv);
    }

    public String decrypt(String encryptedText) throws Exception {
        byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedText);

        byte[] iv = new byte[16];
        byte[] encrypted = new byte[encryptedWithIv.length - 16];

        System.arraycopy(encryptedWithIv, 0, iv, 0, 16);
        System.arraycopy(encryptedWithIv, 16, encrypted, 0, encrypted.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, "UTF-8");
    }
}
