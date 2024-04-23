/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;
    
    //Encapsulation, to make sure another class also can accessed to private method under this class.
    public Connection linkDatabase(){  
        return connectToDatabase(); 
    }
    private Connection connectToDatabase() {
        try {
            // Database connection details
            String dbUrl = DatabaseConfig.URL;
            String dbUsername = DatabaseConfig.USERNAME;
            String dbPassword = DatabaseConfig.PASSWORD;

            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return null;
        }
    }
    
    public void endDatabase(){
        closeConnection();
    }
    
    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

