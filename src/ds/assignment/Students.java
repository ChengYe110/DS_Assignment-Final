/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Students {
//    @FXML
//    private Label showUsernameLabel;
    
    public void displayInfo(){
        
        try{
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Create SQL query
            String connectQuery = "SELECT Username, Email, Role, Location, Points FROM student";
        
            // Execute query
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            // Get info student r from database
            while(queryOutput.next()){
                
                String username = queryOutput.getString("Username");
                String email = queryOutput.getString("Email");
                String role = queryOutput.getString("Role");
                String location = queryOutput.getString("Location");
                int points = queryOutput.getInt("Points");
                
                //showUsernameLabel.setText(username);
            }
            
        }catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public void displayFriend(){
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends, FriendRequest FROM student ";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                
                String friends = queryOutput.getString("Friends");
                String friendRequest = queryOutput.getString("FriendRequest");
                System.out.println("Friend: " + friends);
                System.out.println("Friend request: "+ friendRequest);
                
                //showParentUsernameLabel.setText(parentUsername);
            }
            
        }catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public void displayParent(){
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT parent.Username FROM parent "
                        + "JOIN Student ON parent.Email = student.ParentsEmail OR student.ParentsNum";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                
                String parentUsername = queryOutput.getString("Username");
                System.out.println("Parent: " + parentUsername);
                
                //showParentUsernameLabel.setText(parentUsername);
            }
            
        }catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    //get the friend list of a student
    public static ArrayList<String> getFriendList(String username){
        ArrayList<String> friendLists = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                friendLists.add(queryOutput.getString("Friends"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        
        return friendLists;
    }
    
    //add a friend to a student's friend list
    public static ArrayList<String> addFriendList(String username){
        ArrayList<String> friendLists = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                friendLists.add(queryOutput.getString("Friends"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        
        return friendLists;
    }
    
    //add a friend to a student's friend list
    public static void addFriend(String username, String newFriend) {
        if (isDuplicate(username, newFriend)) {
            System.out.println("Friend already in the list.");
            return;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends == null) {
                    currentFriends = "";
                }
                String updatedFriends = currentFriends + newFriend + ",";

                // Update the database with the new friend list
                String updateQuery = "UPDATE student SET Friends = '" + updatedFriends + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public static boolean isDuplicate(String username, String newFriend) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            Statement statement = connectDB.createStatement();
            String connectQuery = "SELECT Friends FROM student WHERE Username = '" + username + "'";
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentFriends = queryOutput.getString("Friends");
                if (currentFriends != null && currentFriends.contains(newFriend)) {
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

    //delete a friend from a student's friend list
    public static void deleteFriend(String username, String friendToRemove){
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Friends FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if(queryOutput.next()){
                String currentFriends = queryOutput.getString("Friends");
                String updatedFriends = currentFriends.replace(friendToRemove + ",", "");

                // Update the database with the new friend list
                String updateQuery = "UPDATE student SET Friends = '" + updatedFriends + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    //get the friend request list of a student
    public static ArrayList<String> getFriendRequestList(String username){
        ArrayList<String> friendRequestLists = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                friendRequestLists.add(queryOutput.getString("FriendRequest"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        
        return friendRequestLists;
    }
    
    //accept a friend request of a student
    public static void acceptFriendRequest(String username, String friendToAccept){
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if(queryOutput.next()){
                String currentRequests = queryOutput.getString("FriendRequest");
                String updatedRequests = currentRequests.replace(friendToAccept + ",", "");

                // Update the database with the new friend request list
                String updateQuery = "UPDATE student SET FriendRequest = '" + updatedRequests + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);

                // Add the friend to the friend list
                addFriend(username, friendToAccept);
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public static void sendFriendRequest(String senderUsername, String receiverUsername) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the receiver username exists in the student table
            String checkReceiverQuery = "SELECT * FROM student WHERE Username = '" + receiverUsername + "'";
            Statement checkReceiverStatement = connectDB.createStatement();
            ResultSet checkReceiverResult = checkReceiverStatement.executeQuery(checkReceiverQuery);

            if (!checkReceiverResult.next()) {
                System.out.println("Receiver username does not exist.");
                return;
            }

            // Check if the sender already sent a friend request to the receiver
            String checkRequestQuery = "SELECT FriendRequest FROM student WHERE Username = '" + receiverUsername + "'";
            Statement checkRequestStatement = connectDB.createStatement();
            ResultSet checkRequestResult = checkRequestStatement.executeQuery(checkRequestQuery);

            if (checkRequestResult.next()) {
                String currentRequests = checkRequestResult.getString("FriendRequest");
                if (currentRequests != null && currentRequests.contains(senderUsername)) {
                    System.out.println("Friend request already sent to this user.");
                    return;
                }
            }

            // Update the receiver's friend request list with the new request
            String updateQuery = "UPDATE student SET FriendRequest = CONCAT(IFNULL(FriendRequest,''), '" + senderUsername + ",') WHERE Username = '" + receiverUsername + "'";
            Statement updateStatement = connectDB.createStatement();
            updateStatement.executeUpdate(updateQuery);

            System.out.println("Friend request sent successfully.");
            
            checkReceiverStatement.close();
            checkRequestStatement.close();
            connectDB.close();
            
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public static void checkParentEvent(String username){
        ArrayList<String> eventList = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Date FROM event WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                eventList.add(queryOutput.getString("Date"));
            }
            
            // Print out the events
            if (eventList.isEmpty()) {
                System.out.println("No events registered by parents.");
            } 
            else {
                System.out.println("Events registered by parents for " + username + ":");
                    for (String eventDate : eventList) {
                        System.out.println(eventDate);
                    }
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
//    //I temporary comment it because do not have event class yet
//    public static void checkClashing(String username) {
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
}