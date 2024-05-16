/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

/**
 *
 * @author USER
 */
import ds.assignment.DatabaseConnection;
import ds.assignment.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class UserRepository {

    private SessionManager sessionManager;
    private DatabaseConnection dbConnect;
    private Login login;

    public UserRepository(DatabaseConnection dbConnect) {
        this.dbConnect = dbConnect;
    }

    public String getEmailByUsername(String username) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT Email FROM " +sessionManager.getCurrentUser().getRole() + " WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getString("Email");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }

        return null; // Username not found in the database
    }

    public User getUserByEmail(String email) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT * FROM user WHERE Email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, email);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Extract user details from the result set and create a User object
                    String username = resultSet.getString("Username");
                    String password = resultSet.getString("Password"); // Note: You may want to hash it
                    String role = resultSet.getString("Role");
                    // Add other fields as needed

                    User user = new User(email, username, password, role);
                    return user;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }

        return null; // User not found in the database
    }

    public void updatePoints(String username, int newPoints) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String updateQuery = "UPDATE student SET Point = ? WHERE Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newPoints);
            preparedStatement.setString(2, username);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Points updated successfully for user: " + username);
            } else {
                System.out.println("Failed to update points. User not found: " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnect.endDatabase();
        }
    }

    public boolean isUsernameTaken(String newUsername) {
        try (Connection conn = dbConnect.linkDatabase()) {
            String selectQuery = "SELECT Username FROM user WHERE Username = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, newUsername);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // If there is a result, username is already taken.
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Consider handling the error appropriately in a real application.
        }
    }

    public boolean updateUsernameInDatabase(String email, String newUsername, String inputPassword) {
        if (!isUsernameTaken(newUsername)) {
            try (Connection connection = dbConnect.linkDatabase()) {
                String updateQuery = "UPDATE " +sessionManager.getCurrentUser().getRole() + " SET Username = ? WHERE Email = ? AND Password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, newUsername);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, login.getHashedPasswordFromDatabase(email));

                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Username updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        SessionManager.currentUser.setUsername(newUsername);
                        return true; // Return true to indicate success
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update username. Please check the email address or password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQLException as needed
                JOptionPane.showMessageDialog(null, "Failed to update username. Please check the email address or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // Return false if the update was not successful
    }

    public boolean isEmailTaken(String newEmail) {
        try (Connection conn = dbConnect.linkDatabase()) {
            String selectQuery = "SELECT Email FROM user WHERE Email = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, newEmail);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // If there is a result, email is already taken.
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Consider handling the error appropriately in a real application.
        }
    }

    public void updateEmailInDatabase(String username, String newEmail) {
        try (Connection connection = dbConnect.linkDatabase()) {
            String updateQuery = "UPDATE profile SET Email = ? WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newEmail);
                preparedStatement.setString(2, username);

                int rowsUpdated = preparedStatement.executeUpdate();

                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePasswordInDatabase(String username, String newPassword) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String updateQuery = "UPDATE " +sessionManager.getCurrentUser().getRole() + " SET Password = ? WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                preparedStatement.setString(2, username);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Password updated successfully for user: " + username);
                } else {
                    System.out.println("Failed to update password. User not found: " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnect.endDatabase();
        }
    }

}
