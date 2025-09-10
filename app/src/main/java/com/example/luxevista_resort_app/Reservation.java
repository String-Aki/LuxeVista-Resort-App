package com.example.luxevista_resort_app;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Reservation {

    private String userId;
    private String serviceName;
    private String status;
    private int imageResId;
    @ServerTimestamp
    private Date reservationDate;

    public Reservation() {}

    public Reservation(String userId, String serviceName, String status, int imageResId) {
        this.userId = userId;
        this.serviceName = serviceName;
        this.status = status;
        this.imageResId = imageResId;
    }

    public String getUserId() {
        return userId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getStatus() {
        return status;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public int getImageResId() {
        return imageResId;
    }
}