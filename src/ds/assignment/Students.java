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
            friendList = getNameFriendList(username);
            friendRequestList = getNameFriendRequestList(username);

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

    public static int getTotalFriend(String username) {
        return getNameFriendList(username).size();
    }
    
    public static int getTotalFriendRequest(String username) {
        return getNameFriendRequestList(username).size();
    }
    
    // get the friend list of a student
    public static ArrayList<String> getNameFriendList(String username) {
        String idStudent = getIdStudentFromName(username);
        ArrayList<String> friendList = new ArrayList<>();
        
        if (idStudent == null) {
            return friendList;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = '" + idStudent + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends != null && !currentFriends.isEmpty()) {
                    String[] id_friendsArray = currentFriends.split(","); // Split the currentFriends string into an array of friends
                    for(String id:id_friendsArray){
                        friendList.add(getNameStudentFromId(id)); // Add each friend to the friendList
                    }
                }
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendList;
    }
    
    public static ArrayList<String> getIdFriendList(String username) {
        String idStudent = getIdStudentFromName(username);
        ArrayList<String> friendList = new ArrayList<>();
        
        if (idStudent == null) {
            return friendList;
        }
        
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, idStudent);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends == null) {
                    currentFriends = "";
                }
                if (currentFriends != null && !currentFriends.isEmpty()) {
                    String[] idFriendsArray = currentFriends.split(","); // Split the currentFriends string into an array of friends
                    friendList.addAll(Arrays.asList(idFriendsArray)); // Add each friend ID to the friendList
                }
            }

            queryOutput.close();
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendList;
    }
    
    public static boolean isExistingStudent(String username) {
        String idStudent = getIdStudentFromName(username);
        
        if (idStudent == null) {
            return false;
        }
        
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the student exists
            String selectQuery = "SELECT COUNT(*) FROM student WHERE id_student = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, idStudent);
            ResultSet queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                int count = queryOutput.getInt(1);
                return count > 0; // student exists if count is greater than 0
            }

            queryOutput.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // student does not exist or error occurred
    }
    
    public static boolean isExistingFriend(String username, String friendName) {
        String idStudent = getIdStudentFromName(username);
        String idFriendName = getIdStudentFromName(friendName);
        
        if (idStudent == null || idFriendName == null) {
            return false;
        }
        
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            Statement statement = connectDB.createStatement();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = '" + idStudent + "'";
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends != null && currentFriends.contains(friendName)) {
                    return true; // Friend already in the list
                }
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // Friend not in the list or error occurred
    }

    public static boolean isDuplicateFriend(String username, String newFriend) {
        String idStudent = getIdStudentFromName(username);
        String idNewFriend = getIdStudentFromName(newFriend);
        
        if (idStudent == null || idNewFriend == null) {
            return false;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            Statement statement = connectDB.createStatement();
            String connectQuery = "SELECT Friends FROM student WHERE id_student = '" + idStudent + "'";
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends != null && currentFriends.contains(idNewFriend)) {
                    return true; // Friend already in the list
                }
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // Friend not in the list or error occurred
    }
    
    public static boolean isDuplicateFriendRequest(String username, String newFriend) {
        String idStudent = getIdStudentFromName(username);
        String idNewFriend = getIdStudentFromName(newFriend);
        
        if (idStudent == null || idNewFriend == null) {
            return false;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            Statement statement = connectDB.createStatement();
            String connectQuery = "SELECT FriendRequest FROM student WHERE id_student = '" + idStudent + "'";
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("FriendRequest");
                if (currentFriends != null && currentFriends.contains(idNewFriend)) {
                    return true; // Friend you request already in the request list
                }
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return false; // Friend request not in the list or error occurred
    }

    public static void deleteFriend(String username, String friendToRemove) {
        String idStudent = getIdStudentFromName(username);
        String idFriendToRemove = getIdStudentFromName(friendToRemove);

        if (!isExistingStudent(friendToRemove)) {
            System.out.println("The friend you are trying to remove does not exist.");
            StudentController.showReminderDialog("The friend you are trying to remove does not exist.");
            return;
        }

        if (!isExistingFriend(username, friendToRemove)) {
            System.out.println("The friend you are trying to remove is not in your friend list.");
            StudentController.showReminderDialog("The friend you are trying to remove is not in your friend list.");
            return;
        }

        Connection connectDB = null;
        PreparedStatement preparedStatement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            // Retrieve the current friend list
            ArrayList<String> friendList = getIdFriendList(idStudent);
            if (friendList == null) {
                friendList = new ArrayList<>();
            }

            // Remove the specified friend
            if (friendList.remove(idFriendToRemove)) {
                // Join the friends back into a single string
                String updatedFriends = String.join(",", friendList);

                // Update the database with the new friend list
                String updateQuery = "UPDATE student SET Friends = ? WHERE id_student = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, updatedFriends);
                updateStatement.setString(2, idStudent);
                updateStatement.executeUpdate();

                // Show confirmation dialog
                StudentController.showReminderDialog("Friend removed successfully.");
                System.out.println("Friend removed successfully.");

                updateStatement.close();
            } else {
                System.out.println("Friend not found in the list.");
                StudentController.showReminderDialog("Friend not found in the list.");
            }

            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    //get the friend request list of a student
    public static ArrayList<String> getNameFriendRequestList(String username) {
        String idStudent = getIdStudentFromName(username);
        ArrayList<String> friendRequestList = new ArrayList<>();
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE id_student = '" + idStudent + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String friendRequests = queryOutput.getString("FriendRequest");
                if (friendRequests != null && !friendRequests.isEmpty()) {
                    String[] id_requestsArray = friendRequests.split(","); // id
                    for(String id:id_requestsArray){
                        friendRequestList.add(getNameStudentFromId(id)); // convert from id to name
                    }
                }
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendRequestList;
    }
    
    public static ArrayList<String> getIdFriendRequestList(String username) {
        String idStudent = getIdStudentFromName(username);
        ArrayList<String> friendRequestList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE id_student = ?";
            PreparedStatement statement = connectDB.prepareStatement(connectQuery);
            statement.setString(1, idStudent);
            ResultSet queryOutput = statement.executeQuery();

            if (queryOutput.next()) {
                String friendRequests = queryOutput.getString("FriendRequest");
                if (friendRequests == null) {
                    friendRequests = "";
                }
                if (friendRequests != null && !friendRequests.isEmpty()) {
                    String[] idRequestsArray = friendRequests.split(","); // Split the friendRequests string into an array of friend request IDs
                    friendRequestList.addAll(Arrays.asList(idRequestsArray)); // Add each friend request ID to the friendRequestList
                }
            }

            queryOutput.close();
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return friendRequestList;
    }

    public static void acceptFriendRequest(String username, String friendToAccept) {
        String idStudent = getIdStudentFromName(username);
        String idNewFriend = getIdStudentFromName(friendToAccept);

        if (!isExistingStudent(friendToAccept)) {
            System.out.println("The friend you are trying to add does not exist.");
            StudentController.showReminderDialog("The friend you are trying to add does not exist.");
            return;
        }
        if (isDuplicateFriend(username, friendToAccept)) {
            System.out.println("Friend already in the list.");
            StudentController.showReminderDialog("Friend already in the list.");
            return;
        }

        try {
            // Remove friend from friend request list
            ArrayList<String> idFriendRequestList = getIdFriendRequestList(username);
            idFriendRequestList.remove(idNewFriend);
            System.out.println("You have remove "+getNameStudentFromId(idNewFriend)+" from friend request list.");
            String updatedRequests = String.join(",", idFriendRequestList);

            // Add friend to friend list
            ArrayList<String> idFriendList = getIdFriendList(username);
            idFriendList.add(idNewFriend);
            System.out.println("You have add "+getNameStudentFromId(idNewFriend)+" to friend list.");
            String updatedFriends = String.join(",", idFriendList);

            // Update the database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String updateQuery = "UPDATE student SET FriendRequest = ?, Friends = ? WHERE id_student = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery);
            preparedStatement.setString(1, updatedRequests);
            preparedStatement.setString(2, updatedFriends);
            preparedStatement.setString(3, idStudent);
            preparedStatement.executeUpdate();

            // Show confirmation dialog
            StudentController.showReminderDialog("You have a new friend named "+getNameStudentFromId(idNewFriend));

            preparedStatement.close();
            connectDB.close();

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        } 
    }

    public static void rejectFriendRequest(String username, String friendToReject) {
        String idStudent = getIdStudentFromName(username);
        String idRejectFriend = getIdStudentFromName(friendToReject);

        if (!isExistingStudent(friendToReject)) {
            System.out.println("The friend you are trying to reject does not exist.");
            StudentController.showReminderDialog("The friend you are trying to reject does not exist.");
            return;
        }
        
        if (isDuplicateFriend(username, friendToReject)) {
            System.out.println("Friend already in the list.");
            StudentController.showReminderDialog("Friend already in the list.");
            return;
        }
        try {
            // Remove friend from friend request list
            ArrayList<String> idFriendRequestList = getIdFriendRequestList(username);
            if (idFriendRequestList.remove(idRejectFriend)) {
                System.out.println("You have removed " + getNameStudentFromId(idRejectFriend) + " from friend request list.");
                String updatedRequests = String.join(",", idFriendRequestList);

                // Update the database
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.linkDatabase();
                String updateQuery = "UPDATE student SET FriendRequest = ? WHERE id_student = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery);
                preparedStatement.setString(1, updatedRequests);
                preparedStatement.setString(2, idStudent);
                preparedStatement.executeUpdate();

                // Show confirmation dialog
                StudentController.showReminderDialog("You have rejected the friend request from " + getNameStudentFromId(idRejectFriend));

                preparedStatement.close();
                connectDB.close();
            } else {
                System.out.println("Friend request not found in the list.");
                StudentController.showReminderDialog("Friend request not found in the list.");
            }

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public static void sendFriendRequest(String senderUsername, String receiverUsername) {
        String idSender = getIdStudentFromName(senderUsername);
        String idReceiver = getIdStudentFromName(receiverUsername);

        // Check if the receiver username exists in the student table
        if (!isExistingStudent(receiverUsername)) {
            System.out.println(receiverUsername + " does not exist.");
            StudentController.showReminderDialog(receiverUsername + " does not exist.");
            return;
        }
        
        // Check if the sender has same friend
        if (isDuplicateFriend(senderUsername, receiverUsername)) {
            System.out.println("Friend already in the list.");
            StudentController.showReminderDialog("Friend already in the list.");
            return;
        }
        
        // Check if the sender already sent a friend request to the receiver
        if (isDuplicateFriendRequest(senderUsername, receiverUsername)) {
            System.out.println("Friend request already sent to this user.");
            StudentController.showReminderDialog("Friend request already sent to this user.");
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Update the receiver's friend request list with the new request
            String updateQuery = "UPDATE student SET FriendRequest = CONCAT(IFNULL(FriendRequest,''), ?) WHERE id_student = ?";
            PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
            updateStatement.setString(1, idSender + ",");
            updateStatement.setString(2, idReceiver);
            updateStatement.executeUpdate();

            System.out.println("Friend request sent successfully.");
            StudentController.showReminderDialog("Friend request sent successfully.");

            updateStatement.close();
            connectDB.close();

        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
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
                        String date = eventDetailsResultSet.getString("Date");
                        String title = eventDetailsResultSet.getString("Title");
                        String venue = eventDetailsResultSet.getString("Venue");
                        String time = eventDetailsResultSet.getString("Time");

                        EventColumn event = new EventColumn(date, title, venue, time);
                        registeredEventList.add(event);
                    }
                    eventDetailsResultSet.close();  // Close the ResultSet for each event ID
                    getEventDetailsStmt.close();
                }
            }
            registeredEventsResultSet.close();
            getRegisteredEventsStmt.close();
            connectDB.close();

        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
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
                String getBookingDetailsQuery = "SELECT Date, VenueFROM booking WHERE id_booking = ?";
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
                StudentController.showReminderDialog("New parent is added! ");
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
            StudentController.showReminderDialog("The parent name you are trying to add does not exists.");
            return;
        }

        int numberOfParents = getNumberOfParents(childName);
        if (numberOfParents >= 2) {
            System.out.println("Only a maximum of two parents can be added.");
            StudentController.showReminderDialog("Only a maximum of two parents can be added.");
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
