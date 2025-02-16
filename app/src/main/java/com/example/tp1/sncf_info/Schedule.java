package com.example.tp1.sncf_info;

import java.util.Objects;

public class Schedule {
    private final String departureTime;
    private final String arrivalTime;
    private final String duration;
    private final String details;

    public Schedule(String departureTime, String arrivalTime,
                    String duration, String details) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.details = details;
    }

    // Getters
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDuration() { return duration; }
    public String getDetails() { return details; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(departureTime, schedule.departureTime) &&
                Objects.equals(arrivalTime, schedule.arrivalTime) &&
                Objects.equals(details, schedule.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureTime, arrivalTime, details);
    }
}
