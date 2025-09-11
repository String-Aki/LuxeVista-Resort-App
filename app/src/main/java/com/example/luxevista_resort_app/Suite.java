package com.example.luxevista_resort_app;

import java.io.Serializable;
import java.util.List;

public class Suite implements Serializable {
    private final String name;
    private final int imageResId;
    private final int price;
    private final List<String> features;
    private final String type;
    private final boolean isAvailable;

    public Suite(String name, int imageResId, int price, List<String> features, String type, boolean isAvailable) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.features = features;
        this.type = type;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getFeatures() {
        return features;
    }

    public String getType() { return type; }

    public boolean isAvailable() { return isAvailable; }
}

