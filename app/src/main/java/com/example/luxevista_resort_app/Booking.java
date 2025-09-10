package com.example.luxevista_resort_app;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Booking {

    private String userId;
    private String suiteName;
    private Date checkInDate;
    private Date checkOutDate;
    private int adultsCount;
    private int childrenCount;
    private String status;
    @ServerTimestamp
    private Date bookingDate;
    public Booking() {}

    public Booking(String userId, String suiteName, Date checkInDate, Date checkOutDate, int adultsCount, int childrenCount, String status) {
        this.userId = userId;
        this.suiteName = suiteName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.adultsCount = adultsCount;
        this.childrenCount = childrenCount;
        this.status = status;
    }

    // --- Getters ---
    public String getUserId() { return userId; }
    public String getSuiteName() { return suiteName; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public int getAdultsCount() { return adultsCount; }
    public int getChildrenCount() { return childrenCount; }
    public String getStatus() { return status; }
    public Date getBookingDate() { return bookingDate; }
}