/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author enjye
 */
public class Event {
    public String date,title,venue,time;

    public Event(String date, String title, String venue, String time) {
        this.date = date;
        this.title = title;
        this.venue = venue;
        this.time = time;
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
    
}
