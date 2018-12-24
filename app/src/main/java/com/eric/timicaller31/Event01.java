package com.eric.timicaller31;

public class Event01 {
    public static final String REF_EVENTS = "Events";
    String name;
    int hour;
    int min;

    public Event01(String name, int hour, int min) {
        this.name = name;
        this.hour = hour;
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
