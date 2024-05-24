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
    private Login login = new Login();

    public UserRepository(DatabaseConnection dbConnect) {
        this.dbConnect = dbConnect;
        sessionManager = new SessionManager(this, login);
    }

    public String getEmailByUsername(String username) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT Email FROM user WHERE Username = ?";
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

    public User getUserByUsername(String username) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT * FROM user WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Extract user details from the result set and create a User object
                    String email = resultSet.getString("Email");
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

    public String getLocation(String username) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT * FROM user WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String location = resultSet.getString("Location");
                    return location;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
        return null; // User not found in the database
    }

    public String getRole(String username) {
        Connection connection = dbConnect.linkDatabase();
        try {
            String selectQuery = "SELECT * FROM user WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("Role");
                    return role;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
        return null; // User not found in the database
    }

    public int getPoints(String username) {
        Connection connection = dbConnect.linkDatabase();
        try {
            String selectQuery = "SELECT * FROM student WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int point = resultSet.getInt("Points");
                    return point;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
        return -1; // User not found in the database
    }

    public void updatePoints(String username, int newPoints) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String updateQuery = "UPDATE student SET Points = ? WHERE Username = ?";
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
                String updateQuery = "UPDATE user SET Username = ? WHERE Email = ? AND Password = ?";
                String userRole = getRole(sessionManager.getCurrentUser().getUsername());
                String updateQueryRole = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Username = ? WHERE Email = ? AND Password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, newUsername);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, login.getHashedPasswordFromDatabase(email));
                    int rowsUpdated = preparedStatement.executeUpdate();

                    PreparedStatement preparedStatement2 = connection.prepareStatement(updateQueryRole);
                    preparedStatement2.setString(1, newUsername);
                    preparedStatement2.setString(2, email);
                    preparedStatement2.setString(3, login.getHashedPasswordFromDatabase(email));

                    int rowsUpdated2 = preparedStatement2.executeUpdate();
                    if (rowsUpdated > 0 && rowsUpdated2 > 0) {

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

    public boolean updateUsernameInDatabaseNew(String email, String newUsername, String inputPassword) {
        try (Connection connection = dbConnect.linkDatabase()) {
            String updateQuery = "UPDATE user SET Username = ? WHERE Email = ? AND Password = ?";
            String userRole = getRole(sessionManager.getCurrentUser().getUsername());
            String updateQueryRole = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Username = ? WHERE Email = ? AND Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, login.getHashedPasswordFromDatabase(email));

                PreparedStatement preparedStatement2 = connection.prepareStatement(updateQueryRole);
                preparedStatement2.setString(1, newUsername);
                preparedStatement2.setString(2, email);
                preparedStatement2.setString(3, login.getHashedPasswordFromDatabase(email));
                int rowsUpdated = preparedStatement.executeUpdate();
                int rowsUpdated2 = preparedStatement2.executeUpdate();
                if (rowsUpdated > 0 && rowsUpdated2 > 0) {
                    SessionManager.currentUser.setUsername(newUsername);
                    return true; // Return true to indicate success

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException as needed
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

    public void updateEmailInDatabase(String username, String newEmail, String password) {
        try (Connection connection = dbConnect.linkDatabase()) {
            String updateQuery = "UPDATE user SET Email = ? WHERE Username = ? AND Password = ?";
            String updateQueryRole = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Email = ? WHERE Username = ? AND Password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, login.getHashedPasswordFromDatabase(username));
            int rowsUpdated = preparedStatement.executeUpdate();

            PreparedStatement preparedStatement2 = connection.prepareStatement(updateQueryRole);
            preparedStatement2.setString(1, newEmail);
            preparedStatement2.setString(2, username);
            preparedStatement2.setString(3, login.getHashedPasswordFromDatabase(username));

            int rowsUpdated2 = preparedStatement2.executeUpdate();
            if (rowsUpdated > 0 && rowsUpdated2 > 0) {

                SessionManager.currentUser.setEmail(newEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateEmailInDatabaseNew(String username, String newEmail, String password) {
        try (Connection connection = dbConnect.linkDatabase()) {
            String updateQuery = "UPDATE user SET Email = ? WHERE Username = ? AND Password = ?";
            String updateQueryRole = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Email = ? WHERE Username = ? AND Password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, login.getHashedPasswordFromDatabase(username));

            PreparedStatement preparedStatement2 = connection.prepareStatement(updateQueryRole);
            preparedStatement2.setString(1, newEmail);
            preparedStatement2.setString(2, username);
            preparedStatement2.setString(3, login.getHashedPasswordFromDatabase(username));
            int rowsUpdated = preparedStatement.executeUpdate();
            int rowsUpdated2 = preparedStatement2.executeUpdate();
            if (rowsUpdated > 0 && rowsUpdated2 > 0) {
                SessionManager.currentUser.setEmail(newEmail);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean updateUsernameAndEmailInDatabase(String newEmail, String newUsername, String inputPassword) {
//        if (!isUsernameTaken(newUsername) && (!isEmailTaken(newEmail))) {
//            try (Connection connection = dbConnect.linkDatabase()) {
//                String updateQuery = "UPDATE user SET Username = ? WHERE Username = ? AND Password = ?";
//                String updateQueryRole = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Username = ? WHERE Username = ? AND Password = ?";
//                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
//                    preparedStatement.setString(1, newUsername);
//                    preparedStatement.setString(2, sessionManager.getCurrentUser().getUsername());
//                    preparedStatement.setString(3, login.getHashedPasswordFromDatabase(inputPassword));
//                    int rowsUpdated = preparedStatement.executeUpdate();
//
//                    PreparedStatement preparedStatement2 = connection.prepareStatement(updateQueryRole);
//                    preparedStatement2.setString(1, newUsername);
//                    preparedStatement2.setString(2, sessionManager.getCurrentUser().getUsername());
//                    preparedStatement2.setString(3, login.getHashedPasswordFromDatabase(inputPassword));
//
//                    int rowsUpdated2 = preparedStatement2.executeUpdate();
//                    SessionManager.currentUser.setUsername(newUsername);
//                    
//                    //change email
//                    String updateQuery2 = "UPDATE user SET Email = ? WHERE Username = ? AND Password = ?";
//                    String updateQueryRole2 = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Email = ? WHERE Username = ? AND Password = ?";
//                    PreparedStatement preparedStatement3 = connection.prepareStatement(updateQuery);
//                    preparedStatement3.setString(1, newEmail);
//                    preparedStatement3.setString(2, sessionManager.getCurrentUser().getUsername());
//                    preparedStatement3.setString(3, login.getHashedPasswordFromDatabase(inputPassword));
//                    int rowsUpdated3 = preparedStatement3.executeUpdate();
//
//                    PreparedStatement preparedStatement4 = connection.prepareStatement(updateQueryRole);
//                    preparedStatement4.setString(1, newEmail);
//                    preparedStatement4.setString(2, sessionManager.getCurrentUser().getUsername());
//                    preparedStatement4.setString(3, login.getHashedPasswordFromDatabase(inputPassword));
//
//                    int rowsUpdated4 = preparedStatement4.executeUpdate();
//                    SessionManager.currentUser.setEmail(newEmail);
//                    
//                    if (rowsUpdated > 0 && rowsUpdated2 > 0 && rowsUpdated3 > 0 && rowsUpdated4 > 0) {
//
//                        JOptionPane.showMessageDialog(null, "Username and Email updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                        SessionManager.currentUser.setUsername(newUsername);
//                        return true; // Return true to indicate success
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Failed to update Username and Email. Please check again.", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                // Handle SQLException as needed
//                JOptionPane.showMessageDialog(null, "Failed to update Username and Email. Please check again.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } else if(isUsernameTaken(newUsername) && !(isEmailTaken(newEmail))) {
//            update
//            JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        return false; // Return false if the update was not successful
//    }
    public void updateUsernameAndEmailInDatabase(String newEmail, String newUsername, String inputPassword) {
        if (!isUsernameTaken(newUsername) && !isEmailTaken(newEmail)) {
            if (updateUsernameInDatabaseNew(newEmail, newUsername, inputPassword) && updateEmailInDatabaseNew(newUsername, newEmail, inputPassword)) {
                JOptionPane.showMessageDialog(null, "Username and Email has been updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (isUsernameTaken(newUsername) && !isEmailTaken(newEmail)) {
            if (updateEmailInDatabaseNew(newUsername, newEmail, inputPassword)) {
                JOptionPane.showMessageDialog(null, "Email updated succesfully! Username already exists. Please choose a different username.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (!isUsernameTaken(newUsername) && (isEmailTaken(newEmail))) {
            if (updateUsernameInDatabaseNew(newEmail, newUsername, inputPassword)) {
                JOptionPane.showMessageDialog(null, "Username updated succesfully! Email already exists. Please choose a different Email.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            //clear email or reset to the initial
            //set username with new one
        } else {
            JOptionPane.showMessageDialog(null, "Both Username and Email already exists. Please choose a different Email and Username.", "Error", JOptionPane.ERROR_MESSAGE);
            //clear both or reset to the initial
        }
    }

    public void updatePasswordInDatabase(String username, String newPassword) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String updateQuery = "UPDATE user SET Password = ? WHERE Username = ?";
            String updateQueryRole = "UPDATE " + getRole(sessionManager.getCurrentUser().getUsername()) + " SET Password = ? WHERE Username = ?";
            String pass = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQueryRole)) {
                preparedStatement.setString(1, pass);
                preparedStatement.setString(2, username);

                int rowsUpdated = preparedStatement.executeUpdate();

                PreparedStatement preparedStatement2 = connection.prepareStatement(updateQuery);
                preparedStatement2.setString(1, pass);
                preparedStatement2.setString(2, username);

                int rowsUpdated2 = preparedStatement2.executeUpdate();

                if (rowsUpdated > 0 && rowsUpdated2 > 0) {
                    System.out.println(login.isPasswordCorrectForUser(newPassword));
                    System.out.println("Password updated successfully for user: " + username);
                    // Update the password field in the User object
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

    //ADD
    public int getNumQuizCreated(String username) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT * FROM educator WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int numQuiz = resultSet.getInt("NumQuiz");
                    return numQuiz;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
        return -1; // User not found in the database
    }

    //ADD
    public int getNumEventCreated(String username) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String selectQuery = "SELECT * FROM educator WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int numEvent = resultSet.getInt("NumEvent");
                    return numEvent;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately in a real application.
        } finally {
            dbConnect.endDatabase();
        }
        return -1; // User not found in the database
    }

    //ADD
    public void updateNumQuizCreated(String username, int newNumQuizCreated) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String updateQuery = "UPDATE educator SET NumQuiz = ? WHERE Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newNumQuizCreated);
            preparedStatement.setString(2, username);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("NumQuizCreated updated successfully for user: " + username);
            } else {
                System.out.println("Failed to update NumQuizCreated. User not found: " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnect.endDatabase();
        }
    }

    //ADD
    public void updateNumEventCreated(String username, int newNumEventCreated) {
        Connection connection = dbConnect.linkDatabase();

        try {
            String updateQuery = "UPDATE educator SET NumEvent = ? WHERE Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newNumEventCreated);
            preparedStatement.setString(2, username);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("NumEventCreated updated successfully for user: " + username);
            } else {
                System.out.println("Failed to update NumEventCreated. User not found: " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnect.endDatabase();
        }
    }
}
