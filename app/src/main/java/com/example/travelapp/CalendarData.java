package com.example.travelapp;

import java.util.Calendar;

public class CalendarData {
    private String calendar_detail;
    private String calendar_place;
    private String calendar_memo;
    private String calendar_category;
    private String calendar_year;
    private String calendar_month;
    private String calendar_day;
    private String date;
    private boolean visible = false;

    // 생성자
    public CalendarData(String date, String calendar_detail, String calendar_place, String calendar_memo, String calendar_category, String calendar_year, String calendar_month, String calendar_day) {
        this.date = date;
        this.calendar_detail = calendar_detail;
        this.calendar_place = calendar_place;
        this.calendar_memo = calendar_memo;
        this.calendar_category = calendar_category;
        this.calendar_year = calendar_year;
        this.calendar_month = calendar_month;
        this.calendar_day = calendar_day;
    }

    public String getDate() { return date ; }

    public String getCalendar_detail() {
        return calendar_detail;
    }

    public void setCalendar_detail(String calendar_detail) {
        this.calendar_detail = calendar_detail;
    }

    public String getCalendar_place() {
        return calendar_place;
    }

    public void setCalendar_place(String calendar_place) {
        this.calendar_place = calendar_place;
    }

    public String getCalendar_memo() {
        return calendar_memo;
    }

    public void setCalendar_memo(String calendar_memo) {
        this.calendar_memo = calendar_memo;
    }

    public String getCalendar_category() {
        return calendar_category;
    }

    public void setCalendar_category(String calendar_category) {
        this.calendar_category = calendar_category;
    }

    public String getCalendar_year() { return calendar_year; };

    public String getCalendar_month() { return calendar_month; };

    public String getCalendar_day() { return calendar_day; };

    public void setCalender_visible(boolean visible) { this.visible = visible; };
    public boolean getCalendar_visible() { return visible; };

}
