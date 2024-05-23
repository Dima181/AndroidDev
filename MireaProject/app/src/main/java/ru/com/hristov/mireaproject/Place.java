package ru.com.hristov.mireaproject;

import org.osmdroid.util.GeoPoint;

public class Place {
    String name;
    String description;
    String address; // Новое поле для адреса
    GeoPoint location;

    // Обновленный конструктор
    public Place(String name, String description, String address, GeoPoint location) {
        this.name = name;
        this.description = description;
        this.address = address; // Инициализация адреса
        this.location = location;
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address; // Геттер для адреса
    }

    public GeoPoint getLocation() {
        return location;
    }
}
