/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import java.sql.*;
/**
 *
 * @author ed
 */


/**
 * FileStorage class handles all database operations for the password manager
 * It uses SQLite as the database to store encrypted passwords and the master password hash
 */

public class FileStorage {
    private static final String DB_URL = "jdbc:sqlite:password_manager.db";    // Database URL - creates/connects to a SQLite database file named "password_manager.db" that is in the project directory.
    
    
    public FileStorage() throws Exception {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {         // Uses try-with-resources to automatically close database connections
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id INTEGER PRIMARY KEY,
                    service TEXT NOT NULL,
                    login TEXT NOT NULL,
                    encrypted_password TEXT NOT NULL
                );
                """;
            stmt.execute(createTableSQL);

            // SQL command to create the master_password table
            // This table stores the master password hash that protects all other passwords
            // Only one master password exists (with id=1)
            String createMasterTableSQL = """
                CREATE TABLE IF NOT EXISTS master_password (
                    id INTEGER PRIMARY KEY,
                    hash TEXT NOT NULL
                );
                """;
            stmt.execute(createMasterTableSQL);
        }
    }
    

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Saves the master password hash to the database
     * The master password is what the user needs to enter to access all other passwords
     * @param hashedPassword The hashed version of the master password (not the plain text password)
     */
    
    public void saveMasterPassword(String hashedPassword) throws SQLException {
        // Uses PreparedStatement to prevent SQL injection attacks
        try (Connection conn = connect(); 
             PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO master_password (id, hash) VALUES (1, ?)")) {
            pstmt.setString(1, hashedPassword);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Retrieves the stored master password hash from the database
     * @return The stored hash of the master password, or null if no master password is set
     */
    public String getMasterPasswordHash() throws SQLException {
        // Uses try-with-resources to automatically close database resources
        try (Connection conn = connect(); 
             Statement stmt = conn.createStatement();
             // Queries the database for the master password hash with id=1
             ResultSet rs = stmt.executeQuery("SELECT hash FROM master_password WHERE id = 1")) {
            // Returns the hash if found, null otherwise
            return rs.next() ? rs.getString("hash") : null;
        }
    }
}