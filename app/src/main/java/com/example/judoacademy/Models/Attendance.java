package com.example.judoacademy.Models;

import java.util.HashMap;
import java.util.Map;

public class Attendance {
    String date,dayofweek,time;
    Map<String,String> attendance;

    public Attendance(String date, String dayofweek, String time, Map<String, String> attendance) {
        this.date = date;
        this.dayofweek = dayofweek;
        this.time = time;
        this.attendance = attendance;
    }

    public Attendance() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, String> getAttendance() {
        return attendance;
    }

    public void setAttendance(Map<String, String> attendance) {
        this.attendance = attendance;
    }
}
