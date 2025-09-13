package com.example.luxevista_resort_app;

// This is a helper class to combine Booking and Reservation data for the admin UI.
public class AdminReservationItem {
    private final String documentId;
    private final String collectionName; // "bookings" or "reservations"
    private final String itemName;
    private final String userFullName;
    private final String dateDetails;
    private String status;

    public AdminReservationItem(String documentId, String collectionName, String itemName, String userFullName, String dateDetails, String status) {
        this.documentId = documentId;
        this.collectionName = collectionName;
        this.itemName = itemName;
        this.userFullName = userFullName;
        this.dateDetails = dateDetails;
        this.status = status;
    }

    // Getters
    public String getDocumentId() { return documentId; }
    public String getCollectionName() { return collectionName; }
    public String getItemName() { return itemName; }
    public String getUserFullName() { return userFullName; }
    public String getDateDetails() { return dateDetails; }
    public String getStatus() { return status; }

    // Setter for status
    public void setStatus(String status) { this.status = status; }
}