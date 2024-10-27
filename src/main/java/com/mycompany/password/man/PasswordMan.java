/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.password.man;

import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author ed
 */
public class PasswordMan {

    public static void main(String[] args) {
Dotenv dotenv = Dotenv.load();

        // Get environment variables
        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");

        System.out.println("Database URL: " + dbUrl); // For testing
    }
}
