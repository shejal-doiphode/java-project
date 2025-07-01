package com.recipeorganizer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/recipe_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Pranav007@"; // Update with your MySQL password
    
    // No static connection - we'll create a new one each time
    
    static {
        // Load the JDBC driver once during class initialization
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get a connection to the database
     * @return Connection object
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Close a database connection
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * For backwards compatibility with existing code
     * @deprecated Use closeConnection(Connection) instead
     */
    public static void closeConnection() {
        // This method is kept for backward compatibility
        // but doesn't do anything now since we don't store a static connection
        System.out.println("Warning: Called deprecated closeConnection() method that doesn't do anything");
    }
}