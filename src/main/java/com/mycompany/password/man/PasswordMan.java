package com.mycompany.password.man;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PasswordMan {
    public static void main(String[] args) {
        System.out.println("Starting Password Manager...\n");
        
        try (DBConn dbConn = new DBConn()) {
            Connection conn = dbConn.getConnection();
            System.out.println("Successfully connected to database!");
            
            // Test basic database functionality
            try (Statement stmt = conn.createStatement()) {
                // Get database information
                try (ResultSet rs = stmt.executeQuery(
                    "SELECT current_database(), current_user, version()")) {
                    if (rs.next()) {
                        System.out.println("\nDatabase Information:");
                        System.out.println("Database: " + rs.getString(1));
                        System.out.println("User: " + rs.getString(2));
                        System.out.println("Version: " + rs.getString(3));
                    }
                }
                
                // Create passwords table if it doesn't exist
                String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS passwords (
                        id SERIAL PRIMARY KEY,
                        website VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        notes TEXT,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        UNIQUE(website, username)
                    )
                """;
                
                stmt.execute(createTableSQL);
                System.out.println("\nPassword table created/verified");
                
                // Check existing records
                try (ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM passwords")) {
                    if (rs.next()) {
                        System.out.println("Current password entries: " + rs.getInt(1));
                    }
                }
                
                // List all tables in current schema
                System.out.println("\nAvailable tables:");
                try (ResultSet rs = stmt.executeQuery(
                    "SELECT table_name FROM information_schema.tables " +
                    "WHERE table_schema = 'public'")) {
                    while (rs.next()) {
                        System.out.println("- " + rs.getString(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("\nError: " + e.getMessage());
            if (e.getMessage().contains("password authentication")) {
                System.err.println("\nPossible solutions:");
                System.err.println("1. Verify credentials in AWS Secrets Manager");
                System.err.println("2. Check if database password needs to be reset");
                System.err.println("3. Ensure database user has proper permissions");
            } else if (e.getMessage().contains("timeout")) {
                System.err.println("\nPossible solutions:");
                System.err.println("1. Check network connectivity");
                System.err.println("2. Verify security group allows access from your IP");
                System.err.println("3. Ensure database instance is running");
            }
            e.printStackTrace();
        }
    }
}