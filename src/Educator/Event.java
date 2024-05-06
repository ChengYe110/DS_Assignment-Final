package Educator;

import java.time.format.DateTimeFormatter;

public class Event {

    private String title, description, venue;
    private DateTimeFormatter date, time;

    public Event() {
    }

    public Event(String title, String description, String venue, DateTimeFormatter date, DateTimeFormatter time) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
    }
}
