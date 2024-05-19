package ds.assignment;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionManager {

    public static User currentUser;
    private UserRepository userRepository;
    private Login login;
    DatabaseConnection dbConnect = new DatabaseConnection();
    private List<String> topUsernames = new ArrayList<>();
    private List<Integer> topPoints = new ArrayList<>();

    public SessionManager(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.login = new Login();  // Using the first constructor
        this.login.setSessionManager(this);  // Set the session manager
    }

    // Constructor using the second Login constructor
    public SessionManager(UserRepository userRepository, Login login) {
        this.userRepository = userRepository;
        this.login = login;  // Using the second constructor
        this.login.setSessionManager(this);  // Set the session manager
    }

    public void logout() {
        // Clear the current user when logging out
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean login(String enteredEmail, String enteredPassword) {

        boolean isAuthenticated = login.authenticateUser(enteredEmail, enteredPassword);

        if (isAuthenticated) {
            if (!enteredEmail.matches("^(.+)@(gmail\\.com|hotmail\\.com|yahoo\\.com|siswa\\.um\\.edu\\.my)$")) {
                currentUser = userRepository.getUserByUsername(enteredEmail);
                
            } else {
                currentUser = userRepository.getUserByEmail(enteredEmail);
                System.out.println(currentUser);
                System.out.println("email");
            }
        }

        return isAuthenticated;
    }
    
    public void setPoints(int newPoints) {
        if (currentUser != null) {
            // Update points for the current user
            userRepository.updatePoints(currentUser.getUsername(), newPoints);
            //currentUser.setPoints(newPoints);
        }
    }

    public void updatePointsInDatabase(int newPoints) {
        if (currentUser != null) {
            // Update points for the current user in the database
            userRepository.updatePoints(currentUser.getUsername(), newPoints);
        }
    }

    public boolean setUsername(String newUsername, String enteredPassword) {
        // Check if the entered password is correct for the current user
        if (login.isPasswordCorrectForUser(enteredPassword)) {
            // Check if a user is currently logged in
            if (getCurrentUser() != null) {
                // Update the username for the current user
                String userEmail = getCurrentUser().getEmail(toString());
                return userRepository.updateUsernameInDatabase(userEmail, newUsername, enteredPassword);
            } else {
                JOptionPane.showMessageDialog(null, "No logged-in user. Username not updated.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect password. Username not updated.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // Return false if the update was not successful
    }

    public String getCurrentUsername() {
        if (getCurrentUser() != null) {
            return getCurrentUser().getUsername();
        } else {
            return null; // or some default value indicating no user is currently logged in
        }
    }

    public String toString() {
        return getCurrentUsername();
    }

    public String getEmail() {
        if (getCurrentUser() != null) {
            return currentUser.getEmail();
        } else {
            return null; // or some default value indicating no user is currently logged in
        }
    }

    public int getPoint() {
        return currentUser.getCurrentPoints(toString());
    }

    public void setEmail(String newEmail, String enteredPassword) {
        if (currentUser != null) {
            if (login.isPasswordCorrectForUser(enteredPassword)) {
                // Check if the new email is already taken
                if (!userRepository.isEmailTaken(newEmail.trim().toLowerCase())) {
                    // Update email for the current user
                    userRepository.updateEmailInDatabase(currentUser.getUsername(), newEmail);
                    currentUser.setEmail(newEmail); // Update the email field in the User object
                } else {
                    System.out.println("Email already exists. Please choose a different email.");
                }
            } else {
                System.out.println("Incorrect password. Email not updated.");
            }
        }
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        if (currentUser != null) {
            // Check if the old password is correct for the current user
            if (login.isPasswordCorrectForUser(oldPassword)) {
                // Check if the new password and confirmation match
                if (newPassword.equals(confirmPassword)) {
                    // Update the password for the current user
                    userRepository.updatePasswordInDatabase(currentUser.getUsername(), newPassword);
                    currentUser.setPassword(newPassword); // Update the password field in the User object
                    //System.out.println("Password changed successfully.");
                } else {
                    System.out.println("New password and confirmation do not match. Please try again.");
                }
            } else {
                System.out.println("Incorrect old password. Password not changed.");
            }
        }
    }

    //Global Leaderboard
    public void timestampPoints() {
        String username = getCurrentUsername();
        Connection connection = dbConnect.linkDatabase();
        try (
                // Use an UPDATE statement instead of INSERT
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE profile SET timestampPoints = ? WHERE Username = ?"
                )) {
                    // Get the current date and time
                    Date currentDate = new Date();

                    // Format the date and time to match DATETIME format in SQL
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = sdf.format(currentDate);

                    // Create a Timestamp object from the formatted date string
                    Timestamp currentTimestamp = Timestamp.valueOf(formattedDate);

                    // Set parameters in the prepared statement
                    preparedStatement.setTimestamp(1, currentTimestamp);
                    preparedStatement.setString(2, username);

                    // Execute the update
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (log it, throw a custom exception, etc.)
                } finally {
                    // Make sure to close the connection
                    dbConnect.endDatabase();
                }
    }

    public void printTopUsers() {

        try {
            // Establish database connection
            Connection connection = dbConnect.linkDatabase();

            // SQL query
            String sqlQuery = "SELECT Username, Point, TimeStampPoints FROM student ORDER BY Points DESC, TimeStampPoints ASC LIMIT 10;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Process the results
                    while (resultSet.next()) {
                        String username = resultSet.getString("Username");
                        int currentPoint = resultSet.getInt("Points");

                        // Add username and currentPoint to the lists
                        topUsernames.add(username);
                        topPoints.add(currentPoint);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the database connection in the finally block
            dbConnect.endDatabase();
        }
    }
    
    public String getTopUsername(int i){
        printTopUsers();
        return topUsernames.get(i);
    }
    
    public String getTopPoints(int i){
        printTopUsers();
        return String.valueOf(topPoints.get(i));
    }
}
