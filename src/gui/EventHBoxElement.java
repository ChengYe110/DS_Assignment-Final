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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author enjye
 */
// Enum to represent the event time slots
enum EventTime {
    MORNING_8_10("8 am - 10 am"),
    MORNING_10_12("10 am - 12 pm"),
    AFTERNOON_12_2("12 pm - 2 pm"),
    AFTERNOON_2_4("2 pm - 4 pm"),
    EVENING_4_6("4 pm - 6 pm"),
    EVENING_6_8("6 pm - 8 pm");

    private final String timeRange;

    EventTime(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getTimeRange() {
        return timeRange;
    }

    @Override
    public String toString() {
        return timeRange;
    }

    public static EventTime fromString(String text) {
        for (EventTime eventTime : EventTime.values()) {
            if (eventTime.timeRange.equalsIgnoreCase(text)) {
                return eventTime;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}

public class EventHBoxElement {

    private String eventTitle, eventDescription, eventVenue, eventDateS;
    private LocalDate eventDate;
    private EventTime eventTime;

    public EventHBoxElement(String eventTitle, String eventDescription, String eventVenue, String eventDate, String eventTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventVenue = eventVenue;

        try {
            this.eventDate = LocalDate.parse(eventDate, dateFormatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + eventDate);
            this.eventDate = null; // or handle it in a different way
        }

        try {
            this.eventTime = EventTime.fromString(eventTime);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid time slot: " + eventTime);
            this.eventTime = null; // or handle it in a different way
        }

        this.eventDateS = eventDate;
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

//    public String getEventTime() {
//        return eventTime.getTimeRange();
//    }

    public void setEventTime(String eventTime) {
        this.eventTime = EventTime.fromString(eventTime);
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