package com.example.luxevista_resort_app;

public class AdminOption {
    private final String title;
    private final int iconResId;
    private final int colorResId;
    private final String description; // <-- ADD THIS

    public AdminOption(String title, int iconResId, int colorResId, String description) { // <-- UPDATE CONSTRUCTOR
        this.title = title;
        this.iconResId = iconResId;
        this.colorResId = colorResId;
        this.description = description; // <-- ADD THIS
    }

    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
    public int getColorResId() { return colorResId; }
    public String getDescription() { return description; } // <-- ADD THIS
}