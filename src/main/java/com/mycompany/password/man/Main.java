/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.password.man;

import java.util.Scanner;

/**
 *
 * @author ed
 */
public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Set your master password:");
            String masterPassword = scanner.nextLine();

            PasswordManager manager = new PasswordManager(masterPassword, "passwords.dat");

            while (true) {
                System.out.println("\nPassword Manager:");
                System.out.println("1. Add new password");
                System.out.println("2. View password");
                System.out.println("3. Save passwords");
                System.out.println("4. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        System.out.println("Enter service name:");
                        String service = scanner.nextLine();
                        System.out.println("Enter login:");
                        String login = scanner.nextLine();
                        System.out.println("Enter password:");
                        String password = scanner.nextLine();
                        manager.addPassword(service, login, password);
                        break;
                    case 2:
                        System.out.println("Enter service name to view:");
                        service = scanner.nextLine();
                        manager.viewPassword(service);
                        break;
                    case 3:
                        manager.savePasswords();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
