/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pylyp
 */

public class PasswordManager {
    private final EncryptionUtil encryptionUtil;
    private final FileStorage storage;
    

    public PasswordManager(EncryptionUtil encryptionUtil, FileStorage storage) {
        this.encryptionUtil = encryptionUtil;
        this.storage = storage;
        
    }

    // Getter for the storage 
    public FileStorage getStorage() {
        return storage;
    }
    //adds account to database
    public void addAccount(String service, String login, String password) throws Exception {
        String encryptedPassword = encryptionUtil.encrypt(password);    //encrypts password 
        try (Connection conn = storage.connect(); PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO accounts (service, login, encrypted_password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, service);
            pstmt.setString(2, login);
            pstmt.setString(3, encryptedPassword);
            pstmt.executeUpdate();
        }
    }
    
    //list to hold Accounts objects for specified servicee
    public List<Accounts> findAccountsByService(String service) throws Exception {
    List<Accounts> accounts = new ArrayList<>();
    
    try (Connection conn = storage.connect(); 
         PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT login, encrypted_password FROM accounts WHERE service = ?")) {
        pstmt.setString(1, service);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            String login = rs.getString("login");
            String encryptedPassword = rs.getString("encrypted_password");
            String password = encryptionUtil.decrypt(encryptedPassword);
            
            Accounts account = new Accounts(service, login, password);
            accounts.add(account);
        }
    }

    return accounts;
}

}

/*
Code by Max - edited/modified for GUI
public class PasswordManager {
    private final EncryptionUtil encryptionUtil;
    private final FileStorage storage;

    public PasswordManager(EncryptionUtil encryptionUtil, FileStorage storage) {
        this.encryptionUtil = encryptionUtil;
        this.storage = storage;
    }

    public void addAccount(String service, String login, String password) throws Exception {
        String encryptedPassword = encryptionUtil.encrypt(password);
        try (Connection conn = storage.connect(); PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO accounts (service, login, encrypted_password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, service);
            pstmt.setString(2, login);
            pstmt.setString(3, encryptedPassword);
            pstmt.executeUpdate();
        }
    }

    public void findAccountsByService(String service) throws Exception {
        try (Connection conn = storage.connect(); PreparedStatement pstmt = conn.prepareStatement(
                "SELECT login, encrypted_password FROM accounts WHERE service = ?")) {
            pstmt.setString(1, service);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String login = rs.getString("login");
                String encryptedPassword = rs.getString("encrypted_password");
                String password = encryptionUtil.decrypt(encryptedPassword);
                System.out.printf("Service: %s, Login: %s, Password: %s%n", service, login, password);
            }
        }
    }
}

*/
