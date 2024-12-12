package com.baymotors.utils;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baymotors.models.*;

public class DataStore {
    private static DataStore instance;

    private Map<Integer, User> users;
    private Map<Integer, Customer> customers;
    private Map<Integer, Vehicle> vehicles;
    private Map<Integer, Task> tasks;
    private Map<Integer, Manufacturer> manufacturers;
    private Map<Integer, Supplier> suppliers;
    private Map<Integer, Part> parts;

    private int nextUserId = 1;
    private int nextCustomerId = 1;
    private int nextVehicleId = 1;
    private int nextTaskId = 1;
    private int nextManufacturerId = 1;
    private int nextSupplierId = 1;
    private int nextPartId = 1;

    private DataStore() {
        users = new HashMap<>();
        customers = new HashMap<>();
        vehicles = new HashMap<>();
        tasks = new HashMap<>();
        manufacturers = new HashMap<>();
        suppliers = new HashMap<>();
        parts = new HashMap<>();
        initializeDefaultData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initializeDefaultData() {
        try {
            // Create default manager
            Manager defaultManager = new Manager(
                    getNextUserId(),
                    "Admin Manager",
                    "admin@baymotors.com",
                    "admin123"
            );
            addUser(defaultManager);

            // Create default mechanic
            Mechanic defaultMechanic = new Mechanic(
                    getNextUserId(),
                    "John Mechanic",
                    "john@baymotors.com",
                    "mech123"
            );
            addUser(defaultMechanic);
            defaultManager.addTeamMember(defaultMechanic);

            // Create some default manufacturers
            Manufacturer toyota = new Manufacturer(getNextManufacturerId(), "Toyota");
            Manufacturer ford = new Manufacturer(getNextManufacturerId(), "Ford");
            addManufacturer(toyota);
            addManufacturer(ford);

            // Create default supplier
            Supplier defaultSupplier = new Supplier(
                    getNextSupplierId(),
                    "AutoParts Ltd",
                    "John Smith",
                    "supplier@autoparts.com",
                    "02920123456",
                    "123 Parts Street, Cardiff"
            );
            addSupplier(defaultSupplier);

            // Create some default parts
            Part oilFilter = new Part(
                    getNextPartId(),
                    "Oil Filter",
                    "OF001",
                    15.99,
                    toyota,
                    defaultSupplier
            );
            Part airFilter = new Part(
                    getNextPartId(),
                    "Air Filter",
                    "AF001",
                    25.99,
                    ford,
                    defaultSupplier
            );
            addPart(oilFilter);
            addPart(airFilter);

            // Add parts to supplier with initial quantity
            defaultSupplier.addPart(oilFilter, 50);
            defaultSupplier.addPart(airFilter, 30);

            // Create a default customer
            Customer defaultCustomer = new Customer(
                    getNextCustomerId(),
                    "John Doe",
                    "john.doe@email.com",
                    "02920789123"
            );
            defaultCustomer.register();
            addCustomer(defaultCustomer);

            // Create a default vehicle for the customer
            Vehicle defaultVehicle = new Vehicle(
                    getNextVehicleId(),
                    "AB12 CDE",
                    toyota,
                    "Corolla",
                    2020
            );
            defaultVehicle.setOwner(defaultCustomer);
            addVehicle(defaultVehicle);

            // Create a default task
            Task defaultTask = new Task(
                    getNextTaskId(),
                    "Regular Service",
                    2,  // Medium priority
                    defaultVehicle
            );
            addTask(defaultTask);

            System.out.println("Default data initialized successfully!");

        } catch (Exception e) {
            System.err.println("Error initializing default data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to print current state (useful for debugging)
    public void printCurrentState() {
        System.out.println("\n=== Current System State ===");
        System.out.println("Users: " + users.size());
        System.out.println("Customers: " + customers.size());
        System.out.println("Vehicles: " + vehicles.size());
        System.out.println("Tasks: " + tasks.size());
        System.out.println("Manufacturers: " + manufacturers.size());
        System.out.println("Suppliers: " + suppliers.size());
        System.out.println("Parts: " + parts.size());
    }

    // Method to validate if system is properly initialized
    public boolean isInitialized() {
        return !users.isEmpty() &&
                users.values().stream().anyMatch(u -> u instanceof Manager) &&
                users.values().stream().anyMatch(u -> u instanceof Mechanic);
    }

    // Add methods
    public void addUser(User user) {
        if (user != null) {
            users.put(user.getId(), user);
        }
    }

    public void addCustomer(Customer customer) {
        if (customer != null) {
            customers.put(customer.getId(), customer);
        }
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicles.put(vehicle.getId(), vehicle);
        }
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.put(task.getId(), task);
        }
    }

    public void addManufacturer(Manufacturer manufacturer) {
        if (manufacturer != null) {
            manufacturers.put(manufacturer.getId(), manufacturer);
        }
    }

    public void addSupplier(Supplier supplier) {
        if (supplier != null) {
            suppliers.put(supplier.getId(), supplier);
        }
    }

    public void addPart(Part part) {
        if (part != null) {
            parts.put(part.getId(), part);
        }
    }

    // Get by ID methods
    public User getUser(int id) {
        return users.get(id);
    }

    public Customer getCustomer(int id) {
        return customers.get(id);
    }

    public Vehicle getVehicle(int id) {
        return vehicles.get(id);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Manufacturer getManufacturer(int id) {
        return manufacturers.get(id);
    }

    public Supplier getSupplier(int id) {
        return suppliers.get(id);
    }

    public Part getPart(int id) {
        return parts.get(id);
    }

    // Get by other fields methods
    public User getUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Manufacturer getManufacturerByName(String name) {
        return manufacturers.values().stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Part getPartByCode(String code) {
        return parts.values().stream()
                .filter(p -> p.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    // Get all methods
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles.values());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Manufacturer> getAllManufacturers() {
        return new ArrayList<>(manufacturers.values());
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public List<Part> getAllParts() {
        return new ArrayList<>(parts.values());
    }

    // ID generators
    public int getNextUserId() { return nextUserId++; }
    public int getNextCustomerId() { return nextCustomerId++; }
    public int getNextVehicleId() { return nextVehicleId++; }
    public int getNextTaskId() { return nextTaskId++; }
    public int getNextManufacturerId() { return nextManufacturerId++; }
    public int getNextSupplierId() { return nextSupplierId++; }
    public int getNextPartId() { return nextPartId++; }

    // Remove methods (if needed)
    public void removeUser(int id) {
        users.remove(id);
    }

    public void removeCustomer(int id) {
        customers.remove(id);
    }

    public void removeVehicle(int id) {
        vehicles.remove(id);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeManufacturer(int id) {
        manufacturers.remove(id);
    }

    public void removeSupplier(int id) {
        suppliers.remove(id);
    }

    public void removePart(int id) {
        parts.remove(id);
    }

    // Clear methods (for testing)
    public void clearAll() {
        users.clear();
        customers.clear();
        vehicles.clear();
        tasks.clear();
        manufacturers.clear();
        suppliers.clear();
        parts.clear();
        resetIds();
    }

    private void resetIds() {
        nextUserId = 1;
        nextCustomerId = 1;
        nextVehicleId = 1;
        nextTaskId = 1;
        nextManufacturerId = 1;
        nextSupplierId = 1;
        nextPartId = 1;
    }
}
