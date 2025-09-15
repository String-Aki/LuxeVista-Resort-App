package com.example.luxevista_resort_app;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Notification {
    private String title;
    private String message;
    @ServerTimestamp
    private Date timestamp;

    public Notification() {}

    //Getters
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}