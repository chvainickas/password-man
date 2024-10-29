/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 *
 * @author pylyp
 */


public class EncryptionUtil {

    private final SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES";

    public EncryptionUtil(String masterPassword) throws Exception {
        byte[] key = generateKey(masterPassword);
        this.secretKey = new SecretKeySpec(key, ALGORITHM);
    }
    
    private byte[] generateKey(String masterPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        return sha.digest(masterPassword.getBytes("UTF-8"));
    }

    // encrypt
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));
        System.out.println(Base64.getEncoder().encodeToString(encryptedData));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // decrypt
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedText = Base64.getDecoder().decode(cipherText);
        byte[] decryptedData = cipher.doFinal(decodedText);
        return new String(decryptedData, "UTF-8");
    }
}
