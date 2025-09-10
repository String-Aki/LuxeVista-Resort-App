package com.example.luxevista_resort_app;

import java.io.Serializable;
import java.util.List;

public class Suite implements Serializable {
    private final String name;
    private final int imageResId;
    private final String price;
    private final List<String> features;

    public Suite(String name, int imageResId,String price, List<String> features) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getFeatures() {
        return features;
    }
}

