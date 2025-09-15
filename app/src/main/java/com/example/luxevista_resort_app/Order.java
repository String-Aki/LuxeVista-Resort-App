package com.example.luxevista_resort_app;

import java.io.Serializable;

public class Order implements Serializable {

    private String documentId;
    private String orderType;
    private final String name;
    private final String price;
    private final int imageResId;
    private final String status;
    private final String details;

    public Order(String name, String details, String price, int imageResId, String status) {
        this.name = name;
        this.details = details;
        this.price = price;
        this.imageResId = imageResId;
        this.status = status;
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public String getDocumentId() {
        return documentId;
    }
    public String getOrderType() {
        return orderType;
    }

    //Setters
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
}