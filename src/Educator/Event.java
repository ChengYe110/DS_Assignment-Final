package Educator;

import ds.assignment.DatabaseConnection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Event {

    private String title;
    private String description;
    private String venue;
    private LocalDate date;
    private LocalTime time;

    public Event() {
    }

    public Event(String title, String description, String venue, LocalDate date, LocalTime time) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    // Retrieve events from the database
     public static ArrayList<Event> getEventList() {
        ArrayList<Event> eventList = new ArrayList<>();
        
        String query = "SELECT Title, Description, Venue, Date, Time FROM event";

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String venue = resultSet.getString("Venue");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                LocalTime time = resultSet.getTime("Time").toLocalTime();

                Event event = new Event(title, description, venue, date, time);
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
     
    
}
