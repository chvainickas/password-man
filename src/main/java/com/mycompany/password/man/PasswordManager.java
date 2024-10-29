/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author pylyp
 */

public class PasswordManager {

    private final HashMap<String, String> passwordStore;
    private final EncryptionUtil encryptionUtil;
    private final FileStorage fileStorage;

    public PasswordManager(String masterPassword, String filename) throws Exception {
        this.encryptionUtil = new EncryptionUtil(masterPassword);
        this.fileStorage = new FileStorage(filename);
        this.passwordStore = fileStorage.load();
    }

    // Метод для добавления нового пароля
    public void addPassword(String service, String login, String password) throws Exception {
        String combined = login + ":" + password;
        String encrypted = encryptionUtil.encrypt(combined);
        passwordStore.put(service, encrypted);
        System.out.println("Password for " + service + " added successfully.");
    }

    // Метод для просмотра пароля
    public void viewPassword(String service) throws Exception {
        String encryptedData = passwordStore.get(service);
        if (encryptedData != null) {
            String decryptedData = encryptionUtil.decrypt(encryptedData);
            String[] parts = decryptedData.split(":", 2);
            System.out.println("Service: " + service);
            System.out.println("Login: " + parts[0]);
            System.out.println("Password: " + parts[1]);
        } else {
            System.out.println("No password found for this service.");
        }
    }

    // Метод для сохранения паролей в файл
    public void savePasswords() throws Exception {
        fileStorage.save(passwordStore);
        System.out.println("Passwords saved successfully.");
    }
}
