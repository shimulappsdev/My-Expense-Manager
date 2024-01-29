package com.expensemanager.model;

public class Notification {

    private int notification_id;
    private String notification_title;
    private String notification_description;
    private String notification_time;
    private String notification_date;
    private String notification_img;

    public Notification() {
    }

    public Notification(int notification_id, String notification_title, String notification_description, String notification_time, String notification_date, String notification_img) {
        this.notification_id = notification_id;
        this.notification_title = notification_title;
        this.notification_description = notification_description;
        this.notification_time = notification_time;
        this.notification_date = notification_date;
        this.notification_img = notification_img;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_description() {
        return notification_description;
    }

    public void setNotification_description(String notification_description) {
        this.notification_description = notification_description;
    }

    public String getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(String notification_time) {
        this.notification_time = notification_time;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public String getNotification_img() {
        return notification_img;
    }

    public void setNotification_img(String notification_img) {
        this.notification_img = notification_img;
    }
}
