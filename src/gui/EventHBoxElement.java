/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author enjye
 */
public class EventHBoxElement {

    private String eventTitle, eventDescription, eventVenue, eventDateS, eventTimeS;
    private LocalDate eventDate;
    private LocalTime eventTime;


    public EventHBoxElement(String eventTitle, String eventDescription, String eventVenue, String eventDate, String eventTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventVenue = eventVenue;
        this.eventDate = LocalDate.parse(eventDate, dateFormatter);
        this.eventTime = LocalTime.parse(eventTime, timeFormatter);
        this.eventDateS = eventDate;
        this.eventTimeS = eventTime;
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

    public String getEventTimeS() {
        return eventTimeS;
    }

    public void setEventTime(String eventTime) {
        this.eventTimeS = eventTime;
    }
    
    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }
    
    public static ArrayList<LocalDate> getEventDateList(){
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
