/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Students extends User{
//    @FXML
//    private Label showUsernameLabel;
    private int points;
    private String location;
    
    public Students (String email, String username, String password, String role,int points,String location){
        super(email, username, password, role);
        this.points=0;
        this.location="";
    }
    
    public int getPoints() {
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
        if (!InfoCheck()){
        Connection connection = null;
        try {
            // Database connection details
            connection=dbConnect.linkDatabase();
            
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
    }   else
            System.out.println("Username or password already exists. Please choose a different username or password.");
            
    }
    
    
    public void displayStudentInfo(){
        
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
                this.username = queryOutput.getString("Username");
                this.email = queryOutput.getString("Email");
                this.role = queryOutput.getString("Role");
                this.location = queryOutput.getString("Location");
                this.points = queryOutput.getInt("Points");
                
            }
            
        }catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    //uselesss
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
    //useless
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
    
    public ArrayList<String> getParentUsernameList() {
        ArrayList<String> parentUsernameList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT parent.Username FROM parent "
                    + "JOIN Student ON student.ParentsEmail = parent.Email";
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            ResultSet queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                String parentUsername = queryOutput.getString("Username");
                parentUsernameList.add(parentUsername);
            }

            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return parentUsernameList;
    }

    
    //get the friend list of a student
    public ArrayList<String> getFriendList(String username){
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
    public ArrayList<String> addFriendList(String username){
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
    public void addFriend(String username, String newFriend) {
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
    
    public boolean isDuplicate(String username, String newFriend) {
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
    public void deleteFriend(String username, String friendToRemove){
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
    public ArrayList<String> getFriendRequestList(String username){
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
    public void acceptFriendRequest(String username, String friendToAccept){
        
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
    
    //reject a friend request of a student
    public void rejectFriendRequest(String username, String friendToReject) {

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT FriendRequest FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentRequests = queryOutput.getString("FriendRequest");
                String updatedRequests = currentRequests.replace(friendToReject + ",", "");

                // Update the database with the new friend request list
                String updateQuery = "UPDATE student SET FriendRequest = '" + updatedRequests + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    
    public void sendFriendRequest(String senderUsername, String receiverUsername) {
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
    
    public void checkParentEvent(String username){
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
    
    public ArrayList<String> getDateEvent(String username){
        ArrayList<String> dateEventList = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Date FROM event WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                dateEventList.add(queryOutput.getString("Date"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return dateEventList;
    }
    
    public ArrayList<String> getTitleEvent(String username){
        ArrayList<String> titleEventList = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Title FROM event WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                titleEventList.add(queryOutput.getString("Title"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return titleEventList;
    }
    
    public ArrayList<String> getVenueEvent(String username){
        ArrayList<String> venueEventList = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Venue FROM event WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                venueEventList.add(queryOutput.getString("Venue"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return venueEventList;
    }
    
    public ArrayList<String> getTimeEvent(String username){
        ArrayList<String> timeEventList = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Time FROM event WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                timeEventList.add(queryOutput.getString("Time"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return timeEventList;
    }
    
    public ArrayList<String> getBookedVenue(String username){
        ArrayList<String> bookedVenueList = new ArrayList<>();
        
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Place FROM booking WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                bookedVenueList.add(queryOutput.getString("Place"));
            }
            
            statement.close();
            connectDB.close();
        }
        catch(Exception e){
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return bookedVenueList;
    }
    
    public void updateProfile(String username){
        
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
}