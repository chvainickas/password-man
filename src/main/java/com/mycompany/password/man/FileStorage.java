/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;
import java.sql.*;
/**
 *
 * @author pylyp
 */



public class FileStorage {
    private static final String DB_URL = "jdbc:sqlite:password_manager.db";

    public FileStorage() throws Exception {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id INTEGER PRIMARY KEY,
                    service TEXT NOT NULL,
                    login TEXT NOT NULL,
                    encrypted_password TEXT NOT NULL
                );
                """;
            stmt.execute(createTableSQL);

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

    public void saveMasterPassword(String hashedPassword) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO master_password (id, hash) VALUES (1, ?)")) {
            pstmt.setString(1, hashedPassword);
            pstmt.executeUpdate();
        }
    }

    public String getMasterPasswordHash() throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT hash FROM master_password WHERE id = 1")) {
            return rs.next() ? rs.getString("hash") : null;
        }
    }
}
