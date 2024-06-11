package Educator;

import ds.assignment.*;
import ds.assignment.DatabaseConnection;
import ds.assignment.UserRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    private String email;
    private String username;
    private String password;
    private String location;
    private String role;

    // Constructor
    public User(String email, String username, String password, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.location = generateLocation();
        this.role = role;
        System.out.println(location);
        recordToUserCSV(this.username,this.email,this.role,this.location);
    }

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);

    // Method to insert user data into the database
    //public abstract void insertIntoDatabase();
//    public void insertIntoStudentDatabase() {
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
    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Method to generate the registration date
    private String generateLocation() {
        Random rd = new Random();
        double x = rd.nextDouble(-500, 501);
        double y = rd.nextDouble(-500, 501);
        return String.format("%.2f,%.2f", x, y);  // You can customize the registration date logic
    }

    private boolean InfoCheck() {
        Connection conn = null;
        try {
            // Database connection details
            conn = dbConnect.linkDatabase();

            String selectQuery = "SELECT Email, Username FROM " + role + " WHERE Email = ? OR Username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

            preparedStatement.setString(1, email);
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

    public int getCurrentPoints(String identifier) {
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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(UserRepository userRepository, String newName, String pass) {
        userRepository.updateUsernameInDatabase(email, newName, pass);
        // Update other necessary variables
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
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

    public String getRole() {
        return this.role;
    }

    public static void recordToUserCSV(String username, String email, String role, String location) {
        String fileName = "Users.csv";
        File file = new File(fileName);
        boolean fileExists = file.exists();
        System.out.println("Processing");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write the header if the file is newly created
            if (!fileExists) {
                bw.write("Username,Email,Role,Location");
                bw.newLine();
            }
            // Write the user data
            bw.write(username + "," + email + "," + role + "," + location);
            bw.newLine();
            System.out.println("done record");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
