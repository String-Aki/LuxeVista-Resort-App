package com.example.luxevista_resort_app;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Notification {
    // Fields are no longer final
    private String title;
    private String message;
    @ServerTimestamp // This will be automatically populated by Firestore
    private Date timestamp;

    // IMPORTANT: A public no-argument constructor is required for Firestore.
    public Notification() {}

    // --- Getters for all the fields ---
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