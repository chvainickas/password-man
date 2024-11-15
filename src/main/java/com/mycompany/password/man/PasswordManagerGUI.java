/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;

/**
 *
 * @author Jodie Boylan    
 * x20515659
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

public class PasswordManagerGUI extends JFrame {
    private PasswordManager passwordManager;
    private EncryptionUtil encryptionUtil;
    private FileStorage storage;
    
    // Constructor
    public PasswordManagerGUI() throws Exception {
        storage = new FileStorage();
        
        // handle master password
        String masterPasswordHash = storage.getMasterPasswordHash();
        if (masterPasswordHash == null) {
            String masterPassword = JOptionPane.showInputDialog(this, "Set your master password:");
            String hashedMasterPassword = hashPassword(masterPassword);
            storage.saveMasterPassword(hashedMasterPassword);
            encryptionUtil = new EncryptionUtil(masterPassword);
        } else {
            String masterPassword = JOptionPane.showInputDialog(this, "Enter your master password:");
            String hashedInputPassword = hashPassword(masterPassword);
            if (!hashedInputPassword.equals(masterPasswordHash)) {
                JOptionPane.showMessageDialog(this, "Incorrect master password!");
                return;
            }
            encryptionUtil = new EncryptionUtil(masterPassword);
        }

        passwordManager = new PasswordManager(encryptionUtil, storage);
        
        // sets up the JFrame
        setTitle("Password Manager");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // creates the panel and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JButton btnAddAccount = new JButton("Add Account");
        JButton btnFindAccount = new JButton("Find Account");
        JButton btnExit = new JButton("Exit");

        // add action listeners to buttons
        btnAddAccount.addActionListener(e -> showAddAccountDialog());
        btnFindAccount.addActionListener(e -> showFindAccountDialog());
        btnExit.addActionListener(e -> System.exit(0));

        panel.add(btnAddAccount);
        panel.add(btnFindAccount);
        panel.add(btnExit);
        
        add(panel);
        setVisible(true);
    }

    // Show dialog  to add an account
    private void showAddAccountDialog() {
        JTextField serviceField = new JTextField(20);
        JTextField loginField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Service:"));
        panel.add(serviceField);
        panel.add(new JLabel("Login:"));
        panel.add(loginField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String service = serviceField.getText();
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                passwordManager.addAccount(service, login, password);
                JOptionPane.showMessageDialog(this, "Account added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding account: " + e.getMessage());
            }
        }
    }
    
    private void showFindAccountDialog() {
    String service = JOptionPane.showInputDialog(this, "Enter service:");
    if (service != null && !service.trim().isEmpty()) {
        try {
            List<Accounts> accounts = passwordManager.findAccountsByService(service);

            if (accounts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No accounts found for the service: " + service);
            } else {
                // Create a panel to display accounts in a structured way
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                for (Accounts account : accounts) {
                    // Display the service and login in regular text
                    panel.add(new JLabel("Service: " + account.getService()));
                    panel.add(new JLabel("Login: " + account.getLogin()));

                    // Create a JPasswordField to display the password, initially hidden
                    JPasswordField passwordField = new JPasswordField(account.getPassword());
                    passwordField.setEchoChar('*');  // Password is hidden by default

                    // Add mouse listener to reveal password on hover
                    passwordField.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            passwordField.setEchoChar('\0'); // Reveal password on hover
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            passwordField.setEchoChar('*'); // Hide password again after hover
                        }
                    });

                    // Add the password field to the panel
                    panel.add(passwordField);
                    panel.add(Box.createVerticalStrut(5)); // Add space between accounts
                }

                JScrollPane scrollPane = new JScrollPane(panel);
                JOptionPane.showMessageDialog(this, scrollPane, "Found Accounts", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching accounts: " + e.getMessage());
        }
    }
}

    
    // Eds Method to hash the password - modified
    private static String hashPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(sha.digest(password.getBytes("UTF-8")));
    }

    public static void main(String[] args) {
        // start the GUI in the EDT
        SwingUtilities.invokeLater(() -> {
            try {
                new PasswordManagerGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

