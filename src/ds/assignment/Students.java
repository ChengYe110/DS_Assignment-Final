/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;

import gui.BookedStudyTourColumn;
import gui.EventColumn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import gui.StudentController;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class Students extends User {
//    @FXML
//    private Label showUsernameLabel;

    private static int points;

    public Students(String email, String username, String password, String role) {
        super(email, username, password, role);
        this.points = 0;
    }

    public static int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Getter and setter for location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void insertIntoDatabase() {
        if (!InfoCheck()) {
            Connection connection = null;
            try {
                // Database connection details
                connection = dbConnect.linkDatabase();

                String insertQuery = "INSERT INTO student (Username, Email, Password, Role, Points, Location) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, hashPassword(password));
                preparedStatement.setString(4, role);
                preparedStatement.setInt(5, points);
                preparedStatement.setString(6, location);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnect.endDatabase();
            }
        } else {
            System.out.println("Username or password already exists. Please choose a different username or password.");
        }

    }

    public static void displayStudentInfo(String username) {

        try {
            String usernameStudent = "", emailStudent = "", roleStudent = "", locationStudent = "";
            int pointsStudent = 0;

            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Create SQL query
            String connectQuery = "SELECT Username, Email, Role, Location, Points FROM student WHERE Username = '" + username + "'";

            // Execute query
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            // Get info student r from database
            while (queryOutput.next()) {
                usernameStudent = queryOutput.getString("Username");
                emailStudent = queryOutput.getString("Email");
                roleStudent = queryOutput.getString("Role");
                locationStudent = queryOutput.getString("Location");
                pointsStudent = queryOutput.getInt("Points");

            }

            // Display student's information
            System.out.println("=====Student=====");
            System.out.println("Username: " + usernameStudent);
            System.out.println("Email: " + emailStudent);
            System.out.println("Role: " + roleStudent);
            System.out.println("Location: " + locationStudent);
            System.out.println("Points: " + pointsStudent);
            System.out.println();

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    //uselesss
    public void displayFriendInfo(String username) {
        ArrayList<String> friendList = new ArrayList<>();
        ArrayList<String> friendRequestList = new ArrayList<>();
        try {
            friendList = getFriendList(username);
            friendRequestList = getFriendRequestList(username);

            System.out.println("==== Friends for " + username + " ====");
            for (String friend : friendList) {
                System.out.println(friend);
            }
            System.out.println("Total number of friends: " + friendList.size());
            System.out.println("");

            System.out.println("==== Friends requests for " + username + " ====");
            for (String friendRequest : friendRequestList) {
                System.out.println(friendRequest);
            }
            System.out.println("Total number of friend request: " + friendRequestList.size());
            System.out.println("");

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    //useless
    public static void displayParentInfo(String username) {

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT parent.Username FROM parent "
                    + "JOIN Student ON parent.Email = student.ParentsEmail OR student.ParentsNum";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {

                String parentUsername = queryOutput.getString("Username");
                System.out.println("Parent: " + parentUsername);

                //showParentUsernameLabel.setText(parentUsername);
            }

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getParentList() {
        ArrayList<String> parentList = new ArrayList<>();
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT parent.Username FROM parent "
                    + "JOIN Student ON student.ParentsEmail = parent.Email";
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            ResultSet queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                String parentUsername = queryOutput.getString("Username");
                parentList.add(parentUsername);
            }

            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return parentList;
    }

    public boolean isDuplicateParent(String username, String newParent) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT parent.Username FROM parent "
                    + "JOIN student ON student.ParentsEmail = parent.Email "
                    + "WHERE student.Username = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            preparedStatement.setString(1, username);
            ResultSet queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                String currentParents = queryOutput.getString("ParentsEmail");
                if (currentParents != null && currentParents.contains(newParent)) {
                    return true; // Parent already in the list
                }
            }

            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // Parent not in the list or error occurred
    }

    public static int getTotalFriend(String username) {
        return getFriendList(username).size();
    }

    //get the friend list of a student
    public static ArrayList<String> getFriendList(String username) {
        ArrayList<String> friendList = new ArrayList<>();
        String userId = getIdStudentFromName(username);

        if (userId == null) {
            return friendList;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, userId);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends != null && !currentFriends.isEmpty()) {
                    String[] friendsArray = currentFriends.split(",");
                    for (String friendId : friendsArray) {
                        String friendUsername = getNameStudentFromId(friendId.trim());
                        if (friendUsername != null) {
                            friendList.add(friendUsername);
                        }
                    }
                }
            }
            queryOutput.close();
            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendList;
    }

    //add a friend to a student's friend list
    public static ArrayList<String> addFriendList(String username) {
        ArrayList<String> friendList = new ArrayList<>();
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                friendList.add(queryOutput.getString("Friends"));
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendList;
    }

    //add a friend to a student's friend list
    public static void addFriend(String username, String newFriend) {
        if (!isExistingUser(newFriend)) {
            System.out.println("The friend you are trying to add does not exist.");
            JOptionPane.showMessageDialog(null, "The friend you are trying to add does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isDuplicateFriend(username, newFriend)) {
            System.out.println("Friend already in the list.");
            JOptionPane.showMessageDialog(null, "Friend already in the list.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = getIdStudentFromName(username);
        String newFriendId = getIdStudentFromName(newFriend);

        if (userId == null || newFriendId == null) {
            System.out.println("Error retrieving user IDs.");
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, userId);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends == null) {
                    currentFriends = "";
                }
                String updatedFriends = currentFriends + newFriendId + ",";

                // Update the database with the new friend list
                String updateQuery = "UPDATE student SET Friends = ? WHERE id_student = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, updatedFriends);
                updateStatement.setString(2, userId);
                updateStatement.executeUpdate();
                updateStatement.close();
            }
            queryOutput.close();
            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public static boolean isExistingUser(String username) {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            // Check if the user exists
            String selectQuery = "SELECT COUNT(*) FROM student WHERE Username = ?";
            preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                int count = queryOutput.getInt(1);
                return count > 0; // User exists if count is greater than 0
            }

            queryOutput.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // User does not exist or error occurred
    }

    public static boolean isDuplicateFriend(String username, String newFriend) {
        String userId = getIdStudentFromName(username);
        String newFriendId = getIdStudentFromName(newFriend);

        if (userId == null || newFriendId == null) {
            return false;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, userId);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends != null && currentFriends.contains(newFriendId)) {
                    return true; // Friend already in the list
                }
            }
            queryOutput.close();
            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // Friend not in the list or error occurred
    }

    //delete a friend from a student's friend list
    public static void deleteFriend(String username, String friendToRemove) {
        String userId = getIdStudentFromName(username);
        String friendToRemoveId = getIdStudentFromName(friendToRemove);

        if (userId == null || friendToRemoveId == null) {
            System.out.println("Error retrieving user IDs.");
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Retrieve the current friend list
            String selectQuery = "SELECT Friends FROM student WHERE id_student = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, userId);
            ResultSet queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");

                if (currentFriends != null && !currentFriends.isEmpty()) {
                    String[] friendsArray = currentFriends.split(",");
                    ArrayList<String> friendList = new ArrayList<>(Arrays.asList(friendsArray));

                    if (friendList.remove(friendToRemoveId)) {
                        String updatedFriends = String.join(",", friendList);

                        // Update the database with the new friend list
                        String updateQuery = "UPDATE student SET Friends = ? WHERE id_student = ?";
                        PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                        updateStatement.setString(1, updatedFriends);
                        updateStatement.setString(2, userId);
                        updateStatement.executeUpdate();
                        updateStatement.close();
                    } else {
                        System.out.println("Friend not found in the list.");
                    }
                } else {
                    System.out.println("No friends found for the user.");
                }
            } else {
                System.out.println("No such user found.");
            }
            queryOutput.close();
            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    //get the friend request list of a student
    public static ArrayList<String> getFriendRequestList(String username) {
        ArrayList<String> friendRequestList = new ArrayList<>();
        String userId = getIdStudentFromName(username);

        if (userId == null) {
            return friendRequestList;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, userId);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String friendRequests = queryOutput.getString("FriendRequest");
                if (friendRequests != null && !friendRequests.isEmpty()) {
                    String[] requestsArray = friendRequests.split(",");
                    for (String requestId : requestsArray) {
                        String requesterUsername = getNameStudentFromId(requestId.trim());
                        if (requesterUsername != null) {
                            friendRequestList.add(requesterUsername);
                        }
                    }
                }
            }

            queryOutput.close();
            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendRequestList;
    }

    public static void acceptFriendRequest(String username, String friendToAccept) {
        String userId = getIdStudentFromName(username);
        String friendId = getIdStudentFromName(friendToAccept);

        if (userId == null || friendId == null) {
            System.out.println("Error retrieving user IDs.");
            return;
        }

        if (!isExistingUser(friendToAccept)) {
            System.out.println("The friend you are trying to add does not exist.");
            JOptionPane.showMessageDialog(null, "The friend you are trying to add does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isDuplicateFriend(username, friendToAccept)) {
            System.out.println("Friend already in the list.");
            JOptionPane.showMessageDialog(null, "Friend already in the list.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, userId);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String currentRequests = queryOutput.getString("FriendRequest");
                if (currentRequests == null) {
                    currentRequests = "";
                }
                String updatedRequests = removeFriendFromRequests(currentRequests, friendId);

                // Update the database with the new friend request list
                String updateQuery = "UPDATE student SET FriendRequest = ? WHERE id_student = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, updatedRequests);
                updateStatement.setString(2, userId);
                updateStatement.executeUpdate();

                // Remove pending friend from friend request list
                addFriend(username, friendToAccept);
                addFriend(friendToAccept, username);
                JOptionPane.showMessageDialog(null, "You have a new friend!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateStatement.close();
            }

            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    private static String removeFriendFromRequests(String currentRequests, String friendId) {
        String[] requests = currentRequests.split(",");
        StringBuilder updatedRequests = new StringBuilder();

        for (String request : requests) {
            if (!request.trim().equals(friendId.trim())) {
                if (updatedRequests.length() > 0) {
                    updatedRequests.append(",");
                }
                updatedRequests.append(request.trim());
            }
        }

        return updatedRequests.toString();
    }

    //reject a friend request of a student
    public static void rejectFriendRequest(String username, String friendToReject) {
        String userId = getIdStudentFromName(username);
        String friendToRejectId = getIdStudentFromName(friendToReject);

        if (userId == null || friendToRejectId == null) {
            System.out.println("Error retrieving user IDs.");
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, userId);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String currentRequests = queryOutput.getString("FriendRequest");
                if (currentRequests == null) {
                    currentRequests = "";
                }
                String updatedRequests = currentRequests.replace(friendToRejectId + ",", "");

                // Update the database with the new friend request list
                String updateQuery = "UPDATE student SET FriendRequest = ? WHERE id_student = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, updatedRequests);
                updateStatement.setString(2, userId);
                updateStatement.executeUpdate();
                updateStatement.close();
            }

            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public static void sendFriendRequest(String senderUsername, String receiverUsername) {
        String senderId = getIdStudentFromName(senderUsername);
        String receiverId = getIdStudentFromName(receiverUsername);

        if (senderId == null || receiverId == null) {
            System.out.println("Error retrieving user IDs.");
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the receiver username exists in the student table
            String checkReceiverQuery = "SELECT * FROM student WHERE id_student = ?";
            PreparedStatement checkReceiverStatement = connectDB.prepareStatement(checkReceiverQuery);
            checkReceiverStatement.setString(1, receiverId);
            ResultSet checkReceiverResult = checkReceiverStatement.executeQuery();

            if (!checkReceiverResult.next()) {
                System.out.println("Receiver username does not exist.");
                JOptionPane.showMessageDialog(null, "Receiver username does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the sender already sent a friend request to the receiver
            String checkRequestQuery = "SELECT FriendRequest FROM student WHERE id_student = ?";
            PreparedStatement checkRequestStatement = connectDB.prepareStatement(checkRequestQuery);
            checkRequestStatement.setString(1, receiverId);
            ResultSet checkRequestResult = checkRequestStatement.executeQuery();

            if (checkRequestResult.next()) {
                String currentRequests = checkRequestResult.getString("FriendRequest");
                if (currentRequests != null && currentRequests.contains(senderId)) {
                    System.out.println("Friend request already sent to this user.");
                    JOptionPane.showMessageDialog(null, "Friend request already sent to this user.", "Friend Request Pending", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            // Update the receiver's friend request list with the new request
            String updateQuery = "UPDATE student SET FriendRequest = CONCAT(IFNULL(FriendRequest,''), ?) WHERE id_student = ?";
            PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
            updateStatement.setString(1, senderId + ",");
            updateStatement.setString(2, receiverId);
            updateStatement.executeUpdate();

            System.out.println("Friend request sent successfully.");
            JOptionPane.showMessageDialog(null, "Friend request sent successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            checkReceiverStatement.close();
            checkRequestStatement.close();
            updateStatement.close();
            connectDB.close();

        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public static String getIdStudentFromName(String studentName) {
        String idStudent = null;

        if (studentName == null || studentName.trim().isEmpty()) {
            return null;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            String selectQuery = "SELECT id_student FROM student WHERE LOWER(Username) = LOWER(?) LIMIT 1";
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, studentName.trim());
            ResultSet queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                idStudent = queryOutput.getString("id_student");
            }

            queryOutput.close();
            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return idStudent;
    }

    public static String getNameStudentFromId(String idStudent) {
        String nameStudent = null;

        if (idStudent == null || idStudent.trim().isEmpty()) {
            return null;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            String selectQuery = "SELECT Username FROM student WHERE id_student = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, idStudent.trim());
            ResultSet queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                nameStudent = queryOutput.getString("Username");
            }

            queryOutput.close();
            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return nameStudent;
    }

    public static boolean checkFriendRequestPending(String senderUsername, String receiverUsername) {
        String senderId = getIdStudentFromName(senderUsername);
        String receiverId = getIdStudentFromName(receiverUsername);

        if (senderId == null || receiverId == null) {
            System.out.println("Error retrieving user IDs.");
            return false;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the sender already sent a friend request to the receiver
            String checkRequestQuery = "SELECT FriendRequest FROM student WHERE id_student = ?";
            PreparedStatement checkRequestStatement = connectDB.prepareStatement(checkRequestQuery);
            checkRequestStatement.setString(1, receiverId);
            ResultSet checkRequestResult = checkRequestStatement.executeQuery();

            if (checkRequestResult.next()) {
                String currentRequests = checkRequestResult.getString("FriendRequest");
                if (currentRequests != null && currentRequests.contains(senderId)) {
                    System.out.println("Friend request already sent to this user.");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return false;
    }

    public void checkParentEvent(String username) {
        ArrayList<String> eventList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Date FROM event WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                eventList.add(queryOutput.getString("Date"));
            }

            // Print out the events
            if (eventList.isEmpty()) {
                System.out.println("No events registered by parents.");
            } else {
                System.out.println("Events registered by parents for " + username + ":");
                for (String eventDate : eventList) {
                    System.out.println(eventDate);
                }
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    // get id_event in student table --> get event details in event table
    public static ArrayList<EventColumn> getStudentRegisteredEvents(String studentUsername) {
        ArrayList<EventColumn> registeredEventList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // get id_event from RegisteredEvent column in student table
            String getRegisteredEventsQuery = "SELECT RegisteredEvent FROM student WHERE Username = ?";
            PreparedStatement getRegisteredEventsStmt = connectDB.prepareStatement(getRegisteredEventsQuery);
            getRegisteredEventsStmt.setString(1, studentUsername);
            ResultSet registeredEventsResultSet = getRegisteredEventsStmt.executeQuery();

            if (registeredEventsResultSet.next()) {
                String registeredEvents = registeredEventsResultSet.getString("RegisteredEvent");
                String[] eventIds = registeredEvents.split(",");

                // get event details for each id_event in event table row by row
                String getEventDetailsQuery = "SELECT Date, Title, Venue, Time FROM event WHERE id_event = ?";
                PreparedStatement getEventDetailsStmt = connectDB.prepareStatement(getEventDetailsQuery);

                for (String eventId : eventIds) {
                    getEventDetailsStmt.setString(1, eventId.trim());
                    ResultSet eventDetailsResultSet = getEventDetailsStmt.executeQuery();

                    if (eventDetailsResultSet.next()) {
                        Date date = eventDetailsResultSet.getDate("Date");
                        LocalDate localDate = date.toLocalDate();
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String dateS = localDate.format(dateFormatter).toString();
                        System.out.println(dateS);
                        String title = eventDetailsResultSet.getString("Title");
                        String venue = eventDetailsResultSet.getString("Venue");
                        String time = eventDetailsResultSet.getString("Time");

                        EventColumn event = new EventColumn(dateS, title, venue, time);
                        registeredEventList.add(event);
                    }
                    eventDetailsResultSet.close();  // Close the ResultSet for each event ID                  
                }
                getEventDetailsStmt.close();
            }
            registeredEventsResultSet.close();
            getRegisteredEventsStmt.close();
            connectDB.close();

        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        // Sort the list by date before returning
        Collections.sort(registeredEventList);
        return registeredEventList;
    }

    // get id_booking in student table --> get study tour details in booking table
    public static ArrayList<BookedStudyTourColumn> getStudentBookedStudyTour(String studentUsername) {
        ArrayList<BookedStudyTourColumn> bookedStudyTourList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // get id_booking from RegisteredBooking in student table
            String getRegisteredBookingQuery = "SELECT RegisteredBooking FROM student WHERE Username = ?";
            PreparedStatement getRegisteredBookingStmt = connectDB.prepareStatement(getRegisteredBookingQuery);
            getRegisteredBookingStmt.setString(1, studentUsername);
            ResultSet registeredBookingResultSet = getRegisteredBookingStmt.executeQuery();

            if (registeredBookingResultSet.next()) {
                String registeredBooking = registeredBookingResultSet.getString("RegisteredBooking");
                String[] bookingIds = registeredBooking.split(",");

                // get booking details for each id_booking in booking table row by row
                String getBookingDetailsQuery = "SELECT Date, Venue FROM booking WHERE id_booking = ?";
                PreparedStatement getBookingDetailsStmt = connectDB.prepareStatement(getBookingDetailsQuery);

                for (String bookingId : bookingIds) {
                    getBookingDetailsStmt.setString(1, bookingId.trim());
                    ResultSet bookingDetailsResultSet = getBookingDetailsStmt.executeQuery();

                    if (bookingDetailsResultSet.next()) {
                        String date = bookingDetailsResultSet.getString("Date");
                        String venue = bookingDetailsResultSet.getString("Venue");

                        BookedStudyTourColumn bookedStudyTour = new BookedStudyTourColumn(date, venue);
                        bookedStudyTourList.add(bookedStudyTour);
                    }
                    bookingDetailsResultSet.close();  // Close the ResultSet for each booking ID
                    getBookingDetailsStmt.close();
                }
            }
            registeredBookingResultSet.close();
            getRegisteredBookingStmt.close();
            connectDB.close();

        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return bookedStudyTourList;
    }

    public static int getNumberOfParents(String childName) {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int currentParentsNum = 0;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            // Retrieve the current number of parents and parents' email
            String connectStudentQuery = "SELECT ParentsNum FROM student WHERE Username = ?";
            preparedStatement = connectDB.prepareStatement(connectStudentQuery);
            preparedStatement.setString(1, childName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                currentParentsNum = resultSet.getInt("ParentsNum");
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return currentParentsNum;
    }

    public static int getParentIdByParentUsername(String parentUsername) {
        int parentId = -1;

        try {
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Create SQL query
            String query = "SELECT id_parent FROM parent WHERE Username = ?";

            // Use PreparedStatement to prevent SQL injection
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, parentUsername);

            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve the parent ID
            if (resultSet.next()) {
                parentId = resultSet.getInt("id_parent");
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return parentId;
    }

    public static boolean isExistingParent(int idParent) {
        boolean exists = false;

        try {
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the parent exists using id_parent
            String query = "SELECT COUNT(*) FROM parent WHERE id_parent = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, idParent);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = count > 0; // Parent exists if count is greater than 0
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return exists;
    }

    public static String getParentEmailById(int idParent) {
        String parentEmail = null;
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            String query = "SELECT Email FROM parent WHERE id_parent = ?";
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, idParent);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parentEmail = resultSet.getString("Email");
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return parentEmail;
    }

    // update student table: parentNums and parentEmail
    public static void updateStudentInfoAfterAddParent(String childName, String parentEmail) {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            // Retrieve the current number of parents and parents' email
            String connectStudentQuery = "SELECT ParentsNum, ParentsEmail FROM student WHERE Username = ?";
            preparedStatement = connectDB.prepareStatement(connectStudentQuery);
            preparedStatement.setString(1, childName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int currentParentsNum = resultSet.getInt("ParentsNum");
                String currentParentsEmail = resultSet.getString("ParentsEmail");

                // Update the parent's email and increment the number of parents
                String updatedParentsEmail = currentParentsEmail == null ? parentEmail : currentParentsEmail + "," + parentEmail;
                int updatedParentsNum = currentParentsNum + 1;

                // Update the database with the new parents' information
                String updateStudentQuery = "UPDATE student SET ParentsNum = ?, ParentsEmail = ? WHERE Username = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateStudentQuery);
                updateStatement.setInt(1, updatedParentsNum);
                updateStatement.setString(2, updatedParentsEmail);
                updateStatement.setString(3, childName);
                updateStatement.executeUpdate();
                updateStatement.close();
                resultSet.close();
                preparedStatement.close();
                connectDB.close();
                JOptionPane.showMessageDialog(null, "New parent is added! ", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public static void addParent(String childName, String parentName) {
        int idParent = getParentIdByParentUsername(parentName);
        if (!isExistingParent(idParent)) {
            System.out.println("The parent name you are trying to add does not exists.");
            JOptionPane.showMessageDialog(null, "The parent name you are trying to add does not exists. ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numberOfParents = getNumberOfParents(childName);
        if (numberOfParents >= 2) {
            System.out.println("Only a maximum of two parents can be added.");
            JOptionPane.showMessageDialog(null, "Only a maximum of two parents can be added. ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();
            String connectParentQuery = "SELECT Children FROM parent WHERE id_parent = ?";

            preparedStatement = connectDB.prepareStatement(connectParentQuery);
            preparedStatement.setInt(1, idParent);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String currentChildren = resultSet.getString("Children");
                if (currentChildren == null) {
                    currentChildren = "";
                }
                String updatedChildren = currentChildren + childName + ",";

                // Update the database with the new children list
                String updateParentQuery = "UPDATE parent SET Children = ? WHERE id_parent = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateParentQuery);
                updateStatement.setString(1, updatedChildren);
                updateStatement.setInt(2, idParent);
                updateStatement.executeUpdate();
                updateStatement.close();

                // update student table: parentNums and parentEmail
                updateStudentInfoAfterAddParent(childName, getParentEmailById(idParent));
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public void updateProfile(String username) {

    }

//    //I temporary comment it because do not have event class yet
//    public void checkClashing(String username) {
//        ArrayList<Event> eventList = new ArrayList<>();
//
//        try {
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connectDB = connectNow.linkDatabase();
//            String connectQuery = "SELECT * FROM event WHERE Username = '" + username + "'";
//            Statement statement = connectDB.createStatement();
//            ResultSet queryOutput = statement.executeQuery(connectQuery);
//
//            while (queryOutput.next()) {
//                // Assuming you have an Event class with appropriate getters
//                Event event = new Event(queryOutput.getString("EventName"), queryOutput.getString("Date"));
//                eventList.add(event);
//            }
//
//            for (Event event : eventList) {
//                if (isEventClashing(event, eventList)) {
//                    System.out.println("Event " + event.getEventName() + " clashes with another event.");
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("SQL query failed.");
//            e.printStackTrace();
//        }
//    }
//    
//    public boolean isEventClashing(Event eventSelected, List<Event> eventsList) {
//        for (Event eventSelected : eventsList) {
//            // Check if the current event is different from the event being compared
//            if (!eventSelected.equals(eventsList)) {
//                // Check if the dates clash
//                if (eventSelected.getDate().equals(eventsList.getDate())) {
//                    return true; // Dates clash
//                }
//            }
//        }
//        return false; // No clashes
//    }
    //ADD
    public static ArrayList<String> getDoneQuizList(String username) {
        ArrayList<String> doneQuizList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT CompletedQuiz FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String completedQuizzes = queryOutput.getString("CompletedQuiz");
                if (completedQuizzes != null && !completedQuizzes.isEmpty()) {
                    String[] quizzesArray = completedQuizzes.split(",");
                    for (String quiz : quizzesArray) {
                        doneQuizList.add(quiz.trim()); // Trim to remove any extra spaces
                    }
                }
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return doneQuizList;
    }

    public static void addDoneQuiz(String username, String doneQuizID) {

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT CompletedQuiz FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentDoneQuiz = queryOutput.getString("CompletedQuiz");
                if (currentDoneQuiz == null) {
                    currentDoneQuiz = "";
                }
                String updatedDoneQuiz = currentDoneQuiz + doneQuizID + ",";

                // Update the database with the new friend list
                String updateQuery = "UPDATE student SET CompletedQuiz = '" + updatedDoneQuiz + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
}
