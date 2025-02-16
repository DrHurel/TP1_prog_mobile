package com.example.tp1.calendar;

public class Event {
    private final String date;
    private final String time;
    private final String description;
    private final String type;

    public Event(String date, String time, String description, String type) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.type = type;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }
    public String getType() { return type; }
}
