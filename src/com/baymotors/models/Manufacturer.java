package com.baymotors.models;

import java.util.ArrayList;
import java.util.List;

public class Manufacturer {
    private int id;
    private String name;
    private List<Supplier> suppliers;
    private List<Part> parts;

    public Manufacturer(int id, String name) {
        this.id = id;
        this.name = name;
        this.suppliers = new ArrayList<>();
        this.parts = new ArrayList<>();
    }

    public void addSupplier(Supplier supplier) {
        if (supplier != null && !suppliers.contains(supplier)) {
            suppliers.add(supplier);
        }
    }

    public void addPart(Part part) {
        if (part != null && !parts.contains(part)) {
            parts.add(part);
        }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public List<Supplier> getSuppliers() { return new ArrayList<>(suppliers); }
    public List<Part> getParts() { return new ArrayList<>(parts); }
}
