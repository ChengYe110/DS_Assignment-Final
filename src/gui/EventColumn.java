/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author enjye
 */
public class EventColumn implements Comparable<EventColumn>{
    public String date,title,venue,time;
    private LocalDate localDate;

    public EventColumn(String date, String title, String venue, String time) {
        this.date = date;
        this.title = title;
        this.venue = venue;
        this.time = time;
        //this.localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }
    
    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(EventColumn o) {
        return this.localDate.compareTo(o.localDate);
    }
    
}
