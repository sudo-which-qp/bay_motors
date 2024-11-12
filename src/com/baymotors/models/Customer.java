package com.baymotors.models;

import java.util.ArrayList;
import java.util.List;
import com.baymotors.patterns.observer.Observer;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private boolean isRegistered;
    private List<Vehicle> vehicles;
    private List<Observer> observers;

    public Customer(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isRegistered = false;
        this.vehicles = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void register() {
        this.isRegistered = true;
        notifyObservers("Customer registered: " + this.name);
    }

    // Fixed the bi-directional relationship
    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null && !vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
            if (vehicle.getOwner() != this) {
                vehicle.setOwner(this);
            }
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        if (vehicles.contains(vehicle)) {
            vehicles.remove(vehicle);
            if (vehicle.getOwner() == this) {
                vehicle.setOwner(null);
            }
        }
    }

    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isRegistered() { return isRegistered; }
    public List<Vehicle> getVehicles() { return new ArrayList<>(vehicles); }
}
