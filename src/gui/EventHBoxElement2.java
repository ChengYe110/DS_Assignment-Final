/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import ds.assignment.Quiz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author enjye
 */
public class EventHBoxElement2 {

//    private String ID, title, description, venue;
//    private Date date;
//    private String time;
//
//    public EventHBoxElement2(String title, String description, String venue, Date date, String time) {
//        this.title = title;
//        this.description = description;
//        this.venue = venue;
//        this.date = date;
//        this.time = time;
//    }
//
//    public EventHBoxElement2(String ID, String title, String description, String venue, Date date, String time) {
//        this.ID = ID;
//        this.title = title;
//        this.description = description;
//        this.venue = venue;
//        this.date = date;
//        this.time = time;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public String getVenue() {
//        return venue;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public String getTime() {
//        return time;
//    }
//    
//    public String getID(){
//        return ID;
//    }
//
//    public static Quiz getQuizFromDatabase(String quizId) {
//        try {
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connectDB = connectNow.linkDatabase();
//
//            String query = "SELECT * FROM quiz WHERE quiz_id = ?";
//            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
//            preparedStatement.setString(1, quizId);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                String title = resultSet.getString("Title");
//                String description = resultSet.getString("Description");
//                String theme = resultSet.getString("Theme");
//                String content = resultSet.getString("Content");
//                return new Quiz(title, description, theme, content);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public void saveEvent(String educatorUsername) {
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.linkDatabase();
//
//        try {
//
//            String query = "INSERT INTO event (Title, Description, Venue, Date, Time) VALUES (?,?,?,?,?)";
//            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
//            preparedStatement.setString(1, this.title);
//            preparedStatement.setString(2, this.description);
//            preparedStatement.setString(3, this.venue);
//           
//            // Format the date and time to match DATETIME format in SQL
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String formattedDate = sdf.format(this.date);
//
//            // Create a Timestamp object from the formatted date string
//            Timestamp currentTimestamp = Timestamp.valueOf(formattedDate);
//
//            // Set parameters in the prepared statement
//            preparedStatement.setTimestamp(4, currentTimestamp);
//            preparedStatement.setString(5, this.time);
//            
//            preparedStatement.executeUpdate(); //delete after execute next
//            System.out.println("haha");
//
////            if (preparedStatement.executeUpdate() == 0) {
////                String query2 = "UPDATE educator SET NumQuiz=NumQuiz+1 WHERE Username=?";
////                PreparedStatement preparedStatement2 = connectDB.prepareStatement(query2);
////                preparedStatement2.setString(1, educatorUsername);
////                preparedStatement2.executeUpdate();
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connectNow.endDatabase();
//        }
//    }
//
//    public static List<EventHBoxElement2> getEventHBoxElement2List() {
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.linkDatabase();
//
//        List<EventHBoxElement2> res = new ArrayList<>();
//
//        try {
//
//            String query = "SELECT * FROM event";
//            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                String ID = resultSet.getString("id_event");
//                String title = resultSet.getString("Title");
//                String description = resultSet.getString("Description");
//                String venue = resultSet.getString("Venue");
//                Date date = resultSet.getDate("Date");
//                String time = resultSet.getString("Time");
//                res.add(new EventHBoxElement2(ID, title, description, venue, date, time));
//            }
//
//            return res;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            connectNow.endDatabase();
//        }
//    }
//    
//    public static ArrayList<String> getJoinedEventList(String username) {
//        ArrayList<String> joinedEventList = new ArrayList<>();
//
//        try {
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connectDB = connectNow.linkDatabase();
//            String connectQuery = "SELECT RegisteredEvent FROM student WHERE Username = '" + username + "'";
//            Statement statement = connectDB.createStatement();
//            ResultSet queryOutput = statement.executeQuery(connectQuery);
//
//            if (queryOutput.next()) {
//                String registeredEvent = queryOutput.getString("RegisteredEvent");
//                if (registeredEvent != null && !registeredEvent.isEmpty()) {
//                    String[] quizzesArray = registeredEvent.split(",");
//                    for (String quiz : quizzesArray) {
//                        joinedEventList.add(quiz.trim()); // Trim to remove any extra spaces
//                    }
//                }
//            }
//
//            statement.close();
//            connectDB.close();
//        } catch (Exception e) {
//            System.out.println("SQL query failed.");
//            e.printStackTrace();
//        }
//
//        return joinedEventList;
//    }
//
//    public static void addDoneQuiz(String username, String doneQuizID) {
//
//        try {
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connectDB = connectNow.linkDatabase();
//            String connectQuery = "SELECT CompletedQuiz FROM student WHERE Username = '" + username + "'";
//            Statement statement = connectDB.createStatement();
//            ResultSet queryOutput = statement.executeQuery(connectQuery);
//
//            if (queryOutput.next()) {
//                String currentDoneQuiz = queryOutput.getString("CompletedQuiz");
//                if (currentDoneQuiz == null) {
//                    currentDoneQuiz = "";
//                }
//                String updatedDoneQuiz = currentDoneQuiz + doneQuizID + ",";
//
//                // Update the database with the new friend list
//                String updateQuery = "UPDATE student SET CompletedQuiz = '" + updatedDoneQuiz + "' WHERE Username = '" + username + "'";
//                statement.executeUpdate(updateQuery);
//            }
//            statement.close();
//            connectDB.close();
//        } catch (Exception e) {
//            System.out.println("SQL query failed.");
//            e.printStackTrace();
//        }
//    }
    
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
            System.out.println("Title Event: " + event.getEventTitle() + " " + event.getEventDateS() + " " + event.getEventTimeS());
        }

        return liveEventList;
    }
    
}
