package com.example.luxevista_resort_app;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Reservation {

    private String userId;
    private String serviceName;
    private String status;
    @ServerTimestamp
    private Date reservationDate;

    public Reservation() {}

    public Reservation(String userId, String serviceName, String status) {
        this.userId = userId;
        this.serviceName = serviceName;
        this.status = status;
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
}