package ds.assignment;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class Login {

    private SessionManager sessionManager;
    private String lastLoginDate;

    // First constructor
    public Login() {
        // Initialization without a session manager
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    // Second constructor with a session manager
    public Login(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean ExistingUser(String enteredEmailUsername) {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connection = dbConnect.linkDatabase();
        System.out.println("1halo");
        try {
            // Assuming 'Role' is the table name containing user details
            String selectQuery = "SELECT * FROM user WHERE Username = ? OR Email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, enteredEmailUsername);
                preparedStatement.setString(2, enteredEmailUsername);

                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next();
                

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }

        return false; // User not found in the database
    }

    public boolean authenticateUser(String enteredEmailUsername, String enteredPassword) {
        String hashedPasswordFromDatabase = getHashedPasswordFromDatabase(enteredEmailUsername);

        if (hashedPasswordFromDatabase != null) {
            // Use BCrypt to check if the entered password matches the hashed password from the database
            return BCrypt.checkpw(enteredPassword, hashedPasswordFromDatabase);
        }

        return false; // Email not found in the database
    }

    public static String getHashedPasswordFromDatabase(String email_username) {
        String hashedPassword = null;
        Connection connection = null;
        DatabaseConnection dbConnect = new DatabaseConnection();
        try {
            // Database connection details
            connection = dbConnect.linkDatabase();

            // SQL query to retrieve the hashed password based on email

            String selectQuery = "SELECT Password FROM user WHERE Email = ? OR Username = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, email_username);
                preparedStatement.setString(2, email_username);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    hashedPassword = resultSet.getString("Password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
        return hashedPassword;
    }

    public boolean isPasswordCorrectForUser(String enteredPassword) {

        System.out.println(sessionManager.getCurrentUsername());
        String username = sessionManager.getCurrentUsername();

        if (username != null) {

            String enteredEmail = sessionManager.currentUser.getEmail(username);
            String hashedPasswordFromDatabase = getHashedPasswordFromDatabase(enteredEmail);

            if (hashedPasswordFromDatabase != null) {
                // Use BCrypt to check if the entered password matches the stored hashed password
                return BCrypt.checkpw(enteredPassword, hashedPasswordFromDatabase);
            }
        }

        return false; // Current user is null or email not found in the database
    }

//    public String getCurrentUsername() {
//        
//        if (currentUser != null) {
//            return currentUser.getUsername();
//        } else {
//            return null; // or some default value indicating no user is currently logged in
//        }
//    }
}
