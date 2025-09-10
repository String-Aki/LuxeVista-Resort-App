package com.example.luxevista_resort_app;

import java.io.Serializable;
import java.util.List;

public class MenuSection implements Serializable {
    private final String title;
    private final List<String> items;

    public MenuSection(String title, List<String> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getItems() {
        return items;
    }
}