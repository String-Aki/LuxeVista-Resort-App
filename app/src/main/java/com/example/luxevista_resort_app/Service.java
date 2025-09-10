package com.example.luxevista_resort_app;

import java.io.Serializable;
import java.util.List;

public class Service implements Serializable {
    private final String name;
    private final int imageResId;
    private final List<String> descriptions;
    private final List<MenuSection> menuSections;
    private final String serviceType;

    public Service(String name, int imageResId, List<MenuSection> menuSections, String serviceType) {
        this.name = name;
        this.imageResId = imageResId;
        this.menuSections = menuSections;
        this.serviceType = serviceType;
        this.descriptions = null;
    }

    public Service(String name, int imageResId, List<String> descriptions) {
        this.name = name;
        this.imageResId = imageResId;
        this.descriptions = descriptions;
        this.serviceType = "simple";
        this.menuSections = null;
    }

    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public List<String> getDescriptions() { return descriptions; }
    public List<MenuSection> getMenuSections() { return menuSections; }
    public String getServiceType() { return serviceType; }
}
