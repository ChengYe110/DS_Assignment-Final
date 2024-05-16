/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import ds.assignment.DatabaseConnection;
import ds.assignment.UserRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.sql.PreparedStatement;
import java.util.Date;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author USER
 */
public class User {
    protected String email;
    protected String username;
    protected String password;
    protected String location;
    protected String role;

    // Constructor
    public User(String email, String username, String password, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.location = generateLocation();
        this.role = role;
        System.out.println(location);
    }
    
    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);

    // Method to insert user data into the database
    
    //public abstract void insertIntoDatabase();
//    public void insertIntoDatabase() {
//        if (!InfoCheck()){
//        Connection connection = null;
//        try {
//            // Database connection details
//            connection=dbConnect.linkDatabase();
//            
//            String insertQuery = "INSERT INTO student (Username, Email, Password, Location) VALUES (?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//            
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, email);
//            preparedStatement.setString(3, hashPassword(password));
//            preparedStatement.setString(4, location);
//            
//            preparedStatement.executeUpdate();
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            dbConnect.endDatabase();
//        }
//    }   else
//            System.out.println("Username or password already exists. Please choose a different username or password.");
//            
//    }
    
    // Method to hash a password
    protected String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
    
    // Method to generate the registration date
    private String generateLocation() {
        Random rd = new Random();
        double x = rd.nextDouble(-500,501);
        double y = rd.nextDouble(-500,501);
        return String.format("%.2f,%.2f", x,y);  // You can customize the registration date logic
    }

    protected boolean InfoCheck() {
        Connection conn = null;
        try {
            // Database connection details
            conn=dbConnect.linkDatabase();
            
            String selectQuery = "SELECT Email, Username FROM " + role + " WHERE Email = ? OR Username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            
            preparedStatement.setString(1,email);
            preparedStatement.setString(2, username);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If there is a result, username or password exists.
            
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Consider handling the error appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
    }
    
    
    public int getCurrentPoints(String identifier){
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "Points FROM student WHERE Username = ? OR Email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, identifier);
                preparedStatement.setString(2, identifier);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt("Points");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }

        return -1; // Return a default value or handle accordingly if the points are not found.

    }
    
    
    
    public String getUsername(){
        return this.username;
    }
    
    public void setUsername(UserRepository userRepository, String newName, String pass) {
        userRepository.updateUsernameInDatabase(email, newName, pass);
    // Update other necessary variables
}
    
    public void setPassword(String newPassword){
        this.password=newPassword;
    }
    
    public void setEmail(String newEmail) {
        //userRepository.updateEmailInDatabase(this.username, newEmail);
        this.email = newEmail; // Update the email field in the User object
    }
    
    public void setUsername(String username) {
        
        this.username = username; // Update the email field in the User object
    }

    public String getEmail(String username) {
        // Assuming you have a method in UserRepository to get the email by username
        return userRepository.getEmailByUsername(this.username);
    }
    
    public String getEmail() {
        // Assuming you have a method in UserRepository to get the email by username
        return userRepository.getEmailByUsername(this.username);
    }
    
    public String getRole(){
        return this.role;
    }
    
}



