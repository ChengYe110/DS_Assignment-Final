/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;

/**
 *
 * @author enjye
 */
public class EventHBoxElement {

    private String eventTitle, eventDescription, eventVenue, eventDateS, eventTime;
    private LocalDate eventDate;

    public EventHBoxElement(String eventTitle, String eventDescription, String eventVenue, LocalDate eventDate, String eventTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventVenue = eventVenue;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(this.eventDate);
        // Create a Timestamp object from the formatted date string
        Timestamp currentTimestamp = Timestamp.valueOf(formattedDate); 
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventDateS = currentTimestamp.toString();
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
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        try {

            String query = "INSERT INTO event (Title, Description, Venue, Date, Time) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, this.eventTitle);
            preparedStatement.setString(2, this.eventDescription);
            preparedStatement.setString(3, this.eventVenue);

            // Format the date and time to match DATETIME format in SQL
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(this.eventDate);
            // Create a Timestamp object from the formatted date string
            Timestamp currentTimestamp = Timestamp.valueOf(formattedDate);
            // Set parameters in the prepared statement
            
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
}
