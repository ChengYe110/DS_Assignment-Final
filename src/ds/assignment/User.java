/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import ds.assignment.DatabaseConnection;
import ds.assignment.UserRepository;
import gui.EventHBoxElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        recordToUserCSV(this.username,this.email,this.role,this.location);
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
    
    public static ArrayList<EventHBoxElement> getEventList() {
        ArrayList<EventHBoxElement> eventList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String query = "SELECT id_event, Title, Description, Venue, Date, Time FROM event";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id_event");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String venue = resultSet.getString("Venue");
                Date date = resultSet.getDate("Date");
                LocalDate localdate = date.toLocalDate();
                String time = resultSet.getString("Time");

                EventHBoxElement event = new EventHBoxElement(id,title, description, venue, localdate, time);
                eventList.add(event);
            }
            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return eventList;
    }
    
    public static ArrayList<EventHBoxElement> getLatestEventList() {
        ArrayList<EventHBoxElement> eventList = getEventList(); // get the event list
        LocalDate currentDate = LocalDate.now();  // get the current date
        System.out.println("Today's date: " + currentDate);

        // Filter the events to get only upcoming events
        ArrayList<EventHBoxElement> upcomingEventList = new ArrayList<>();
        for (EventHBoxElement event : eventList) {
            if (!event.getEventDate().isBefore(currentDate) && !event.getEventDate().equals(currentDate)) {
                upcomingEventList.add(event);
            }
        }

        // Sort the upcoming events by date and time
        Collections.sort(upcomingEventList, new Comparator<EventHBoxElement>() {
            @Override
            public int compare(EventHBoxElement event1, EventHBoxElement event2) {
                if (!event1.getEventDate().equals(event2.getEventDate())) {
                    return event1.getEventDate().compareTo(event2.getEventDate());
                } 
                return -1;
            }
        });

        // Get the closest three upcoming events
        ArrayList<EventHBoxElement> closestThreeUpcomingEvents = new ArrayList<>();
        for (int i = 0; i < Math.min(3, upcomingEventList.size()); i++) {
            closestThreeUpcomingEvents.add(upcomingEventList.get(i));
        }

        // Display the closest events
        System.out.println("Closest upcoming events to " + currentDate + " are:");
        for (EventHBoxElement event : closestThreeUpcomingEvents) {
        long daysUntilEvent = ChronoUnit.DAYS.between(currentDate, event.getEventDate());
            System.out.println("Title Event: " + event.getEventTitle() + " " + event.getEventDateS() + " " + event.getEventTime() + " (in " + daysUntilEvent + " days)");
        }

        return closestThreeUpcomingEvents;
    }
    
    public static ArrayList<EventHBoxElement> getLiveEventList() {
        ArrayList<EventHBoxElement> eventList = getEventList(); // get the event list
        LocalDate currentDate = LocalDate.now();  // get the current date
        System.out.println("Today's date: " + currentDate);

        // get live events 
        ArrayList<EventHBoxElement> liveEventList = new ArrayList<>();
        for (EventHBoxElement event : eventList) {
            if (event.getEventDate().isEqual(currentDate)) {
                liveEventList.add(event);
            }
        }

        // Display the live events
        System.out.println("Events happening today, " + currentDate + " are:");
        for (EventHBoxElement event : liveEventList) {
            System.out.println("Title Event: " + event.getEventTitle() + " " + event.getEventDateS() + " " + event.getEventTime());
        }

        return liveEventList;
    }
    
    public static void recordToUserCSV(String username, String email, String role, String location) {
        String fileName = "Users.csv";
        File file = new File(fileName);
        boolean fileExists = file.exists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write the header if the file is newly created
            if (!fileExists) {
                bw.write("Username,Email,Role,Location");
                bw.newLine();
            }
            // Write the user data
            bw.write(escapeCSV(username) + "," + escapeCSV(email) + "," + role + "," + escapeCSV(location));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            value = "\"" + value + "\"";
        }
        return value;
    }
    
}



