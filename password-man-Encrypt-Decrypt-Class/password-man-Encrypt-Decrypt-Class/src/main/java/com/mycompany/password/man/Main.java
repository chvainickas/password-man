/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.password.man;
//import java.security.MessageDigest;
//import java.util.Base64;
//import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 *
 * @author ed
 */


public class Main {
    public static void main(String[] args) {
        // Start the PasswordManagerGUI in EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and show the PasswordManagerGUI
                new PasswordManagerGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
/* Eds Main class - moved/modified code for GUI
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        FileStorage storage = new FileStorage();

        String masterPasswordHash = storage.getMasterPasswordHash();
        EncryptionUtil encryptionUtil;

        if (masterPasswordHash == null) {
            System.out.print("Set your master password: ");
            String masterPassword = scanner.nextLine();

            String hashedMasterPassword = hashPassword(masterPassword);
            storage.saveMasterPassword(hashedMasterPassword);

            encryptionUtil = new EncryptionUtil(masterPassword);
            System.out.println("Master password set.");
        } else {
            System.out.print("Enter your master password: ");
            String masterPassword = scanner.nextLine();

            String hashedInputPassword = hashPassword(masterPassword);
            if (!hashedInputPassword.equals(masterPasswordHash)) {
                System.out.println("Incorrect master password!");
                return;
            }

            encryptionUtil = new EncryptionUtil(masterPassword);
            System.out.println("Access granted.");
        }

        PasswordManager passwordManager = new PasswordManager(encryptionUtil, storage);

        while (true) {
            System.out.println("1. Add account\n2. Find accounts by service\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            if (choice == 1) {
                System.out.print("Service: ");
                String service = scanner.nextLine();
                System.out.print("Login: ");
                String login = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                passwordManager.addAccount(service, login, password);
            } else if (choice == 2) {
                System.out.print("Service: ");
                String service = scanner.nextLine();
                passwordManager.findAccountsByService(service);
            } else {
                break;
            }
        }
    }

    private static String hashPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(sha.digest(password.getBytes("UTF-8")));
    }
}*/ 