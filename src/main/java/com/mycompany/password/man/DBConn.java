package com.mycompany.password.man;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConn implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(DBConn.class.getName());
    private final Connection connection;
    private static final String SECRET_NAME = "prod/passwordman/db";
    private static final Region REGION = Region.of("eu-north-1");
    private static final int CONNECTION_TIMEOUT = 10;
    private static final int SOCKET_TIMEOUT = 30;
    
    // Class to map JSON secret value
    private static class DbSecret {
        String username;
        String password;
        String host;
        String port;
        String dbname;

        // Validate required fields
        void validate() throws SQLException {
            if (host == null || host.trim().isEmpty()) throw new SQLException("Database host is missing");
            if (port == null || port.trim().isEmpty()) throw new SQLException("Database port is missing");
            if (dbname == null || dbname.trim().isEmpty()) throw new SQLException("Database name is missing");
            if (username == null || username.trim().isEmpty()) throw new SQLException("Username is missing");
            if (password == null || password.trim().isEmpty()) throw new SQLException("Password is missing");
        }
    }
    
    public DBConn() throws SQLException {
        this.connection = getRemoteConnection();
        if (this.connection == null) {
            throw new SQLException("Failed to establish database connection");
        }
        validateConnection();
    }
    
    private String getSecret() {
        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(REGION)
                .build()) {
                
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(SECRET_NAME)
                    .build();
                    
            GetSecretValueResponse response = client.getSecretValue(request);
            return response.secretString();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving secret from AWS Secrets Manager", e);
            throw new RuntimeException("Failed to retrieve database credentials", e);
        }
    }
    
    private Connection getRemoteConnection() throws SQLException {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            
            // Get and validate secret
            String secretJson = getSecret();
            Gson gson = new Gson();
            DbSecret dbSecret = gson.fromJson(secretJson, DbSecret.class);
            dbSecret.validate();
            
            // Construct JDBC URL
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s",
                    dbSecret.host.trim(),
                    dbSecret.port.trim(),
                    dbSecret.dbname.trim());
            
            // Set connection properties with enhanced security
            Properties props = new Properties();
            props.setProperty("user", dbSecret.username.trim());
            props.setProperty("password", dbSecret.password.trim());
            props.setProperty("connectTimeout", String.valueOf(CONNECTION_TIMEOUT));
            props.setProperty("socketTimeout", String.valueOf(SOCKET_TIMEOUT));
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", "require");
            props.setProperty("tcpKeepAlive", "true");
            props.setProperty("ApplicationName", "PasswordManager");
            
            LOGGER.info("Attempting to connect to PostgreSQL RDS at " + dbSecret.host);
            Connection conn = DriverManager.getConnection(jdbcUrl, props);
            LOGGER.info("Successfully connected to RDS database: " + dbSecret.dbname);
            return conn;
            
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC driver not found", e);
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to database", e);
            handleSQLException(e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during database connection", e);
            throw new SQLException("Database connection failed: " + e.getMessage(), e);
        }
    }

    private void validateConnection() throws SQLException {
        try (var stmt = connection.createStatement()) {
            // Verify connection and permissions
            var rs = stmt.executeQuery("SELECT current_database(), current_user, version()");
            if (rs.next()) {
                LOGGER.info(String.format("Connected to %s as %s (PostgreSQL %s)",
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)));
            }
        }
    }
    
    private void handleSQLException(SQLException e) {
        if (e.getMessage().contains("password authentication")) {
            LOGGER.severe("Authentication failed. Verify credentials in AWS Secrets Manager");
        } else if (e.getMessage().contains("timeout")) {
            LOGGER.severe("Connection timed out. Verify network connectivity and security group rules");
        }
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Database connection is closed or null");
            }
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Error checking connection state", e);
        }
    }
    
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed successfully");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database connection", e);
        }
    }
}