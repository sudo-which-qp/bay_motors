package com.baymotors.models;

public class Part {

    private int id;
    private String name;
    private String code;
    private double price;
    private Manufacturer manufacturer;
    private Supplier supplier;

    public Part(int id, String name, String code, double price,
                Manufacturer manufacturer, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.manufacturer = manufacturer;
        this.supplier = supplier;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Manufacturer getManufacturer() { return manufacturer; }
    public Supplier getSupplier() { return supplier; }
}
