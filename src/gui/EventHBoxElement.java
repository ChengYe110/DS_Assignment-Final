/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author enjye
 */
public class EventHBoxElement {

    private String ID, eventTitle, eventDescription, eventVenue, eventDateS, eventTime;
    private LocalDate eventDate;

    public EventHBoxElement(String eventTitle, String eventDescription, String eventVenue, LocalDate eventDate, String eventTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventVenue = eventVenue;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventDateS = eventDate.format(dateFormatter);
    }

    public EventHBoxElement(String ID, String eventTitle, String eventDescription, String eventVenue, LocalDate eventDate, String eventTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.ID = ID;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventVenue = eventVenue;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventDateS = eventDate.format(dateFormatter);
    }

    public String getId() {
        return ID;
    }
    
    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventDateS() {
        return eventDateS;
    }

    public void setEventDate(String eventDate) {
        this.eventDateS = eventDate;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void saveEvent(String educatorUsername) {
        System.out.println("hi");
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        try {

            String query = "INSERT INTO event (Title, Description, Venue, Date, Time) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, this.eventTitle);
            preparedStatement.setString(2, this.eventDescription);
            preparedStatement.setString(3, this.eventVenue);
            Date a = Date.valueOf(eventDate);
            preparedStatement.setDate(4, a);
            preparedStatement.setString(5, this.eventTime);

            preparedStatement.executeUpdate(); //delete after execute next
            System.out.println("haha");

//            if (preparedStatement.executeUpdate() == 0) {
//                String query2 = "UPDATE educator SET NumQuiz=NumQuiz+1 WHERE Username=?";
//                PreparedStatement preparedStatement2 = connectDB.prepareStatement(query2);
//                preparedStatement2.setString(1, educatorUsername);
//                preparedStatement2.executeUpdate();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectNow.endDatabase();
        }
    }

    public static ArrayList<LocalDate> getEventDateList() {
        ArrayList<LocalDate> eventDateList = new ArrayList<>();
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String query = "SELECT Date FROM event";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                eventDateList.add(date);
            }
            Collections.sort(eventDateList);

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return eventDateList;
    }

    public List<String> getJoinedEventList(String username) {
        ArrayList<String> joinedEventList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT RegisteredEvent FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String registeredEvent = queryOutput.getString("RegisteredEvent");
                if (registeredEvent != null && !registeredEvent.isEmpty()) {
                    String[] quizzesArray = registeredEvent.split(",");
                    for (String quiz : quizzesArray) {
                        joinedEventList.add(quiz.trim()); // Trim to remove any extra spaces
                    }
                }
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return joinedEventList;
    }
    
    public void addJoinedEvent(String username, String doneQuizID) {

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT RegisteredEvent FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentDoneQuiz = queryOutput.getString("RegisteredEvent");
                if (currentDoneQuiz == null) {
                    currentDoneQuiz = "";
                }
                String updatedDoneQuiz = currentDoneQuiz + doneQuizID + ",";

                // Update the database with the new friend list
                String updateQuery = "UPDATE student SET RegisteredEvent = '" + updatedDoneQuiz + "' WHERE Username = '" + username + "'";
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