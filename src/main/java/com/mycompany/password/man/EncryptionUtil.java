/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
/**
 *
 * @author pylyp
 */


public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private final SecretKey secretKey;

    // Генерация или установка ключа
    public EncryptionUtil(String masterPassword) throws Exception {
        byte[] key = masterPassword.getBytes("UTF-8");
        this.secretKey = new SecretKeySpec(key, 0, 32, ALGORITHM);
    }

    // Метод шифрования текста
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // Метод расшифрования текста
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedText = Base64.getDecoder().decode(cipherText);
        byte[] decryptedData = cipher.doFinal(decodedText);
        return new String(decryptedData, "UTF-8");
    }
}
