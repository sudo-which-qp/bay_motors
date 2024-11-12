package com.baymotors.models;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private int id;
    private String registration;
    private Manufacturer manufacturer;
    private String model;
    private int year;
    private Customer owner;  // Customer relationship
    private List<Task> taskHistory;
    private List<Part> installedParts;

    public Vehicle(int id, String registration, Manufacturer manufacturer,
                   String model, int year) {
        this.id = id;
        this.registration = registration;
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.taskHistory = new ArrayList<>();
        this.installedParts = new ArrayList<>();
    }

    // Fixed the bi-directional relationship
    public void setOwner(Customer owner) {
        if (owner != null) {
            this.owner = owner;
            if (!owner.getVehicles().contains(this)) {
                owner.addVehicle(this);
            }
        }
    }

    public Customer getOwner() {
        return owner;
    }

    public void addTask(Task task) {
        if (task != null) {
            taskHistory.add(task);
        }
    }

    public void addPart(Part part) {
        if (part != null) {
            installedParts.add(part);
        }
    }

    // Getters
    public int getId() { return id; }
    public String getRegistration() { return registration; }
    public Manufacturer getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public List<Task> getTaskHistory() { return new ArrayList<>(taskHistory); }
    public List<Part> getInstalledParts() { return new ArrayList<>(installedParts); }
}
