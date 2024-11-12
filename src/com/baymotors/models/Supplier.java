package com.baymotors.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supplier {
    private int id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private Map<String, Integer> partsInventory; // code -> quantity
    private List<Part> suppliedParts;
    private List<Manufacturer> associatedManufacturers;

    public Supplier(int id, String name, String contactPerson, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.partsInventory = new HashMap<>();
        this.suppliedParts = new ArrayList<>();
        this.associatedManufacturers = new ArrayList<>();
    }

    public void addPart(Part part, int initialQuantity) {
        if (part != null) {
            suppliedParts.add(part);
            partsInventory.put(part.getCode(), initialQuantity);
        }
    }

    public boolean updatePartQuantity(String partCode, int quantity) {
        if (partsInventory.containsKey(partCode)) {
            partsInventory.put(partCode, quantity);
            return true;
        }
        return false;
    }

    public boolean order(String partCode, int quantity) {
        Integer currentQuantity = partsInventory.get(partCode);
        if (currentQuantity != null && currentQuantity >= quantity) {
            partsInventory.put(partCode, currentQuantity - quantity);
            return true;
        }
        return false;
    }

    public void addManufacturer(Manufacturer manufacturer) {
        if (manufacturer != null && !associatedManufacturers.contains(manufacturer)) {
            associatedManufacturers.add(manufacturer);
            manufacturer.addSupplier(this);
        }
    }

    public int getPartQuantity(String partCode) {
        return partsInventory.getOrDefault(partCode, 0);
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactPerson() { return contactPerson; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public List<Part> getSuppliedParts() { return new ArrayList<>(suppliedParts); }
    public List<Manufacturer> getAssociatedManufacturers() { return new ArrayList<>(associatedManufacturers); }

    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
}
