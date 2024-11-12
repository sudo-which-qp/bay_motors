/*
 * MenuSystem.java
 * Main class responsible for handling the garage management system's user interface
 * and menu operations.
 */

package com.baymotors.utils;

import com.baymotors.exceptions.BayMotorsException;
import com.baymotors.exceptions.TaskException;
import com.baymotors.models.*;
import com.baymotors.patterns.factory.NotificationFactory;
import com.baymotors.patterns.factory.NotificationType;
import com.baymotors.patterns.state.InProgressState;
import com.baymotors.patterns.state.WaitingState;

import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;

public class MenuSystem {
    // Core system components
    private Scanner scanner;
    private User currentUser;
    private DataStore dataStore;

    /**
     * Constructor initializes the menu system with required components
     */
    public MenuSystem() {
        this.scanner = new Scanner(System.in);
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Main entry point for the menu system
     * Handles the main program loop and user role-based menu display
     */
    public void start() {
        while (true) {
            try {
                if (currentUser == null) {
                    showLoginMenu();
                } else if (currentUser instanceof Manager) {
                    showManagerMenu();
                } else if (currentUser instanceof Mechanic) {
                    showMechanicMenu();
                }
            } catch (BayMotorsException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Error Code: " + e.getErrorCode());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ===== Authentication and Login Methods =====

    /**
     * Displays the initial login menu and handles user authentication
     */
    private void showLoginMenu() throws BayMotorsException {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Exit");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                System.out.println("Thank you for using Bay Motors System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Processes user login attempt and authenticates credentials
     */
    private void handleLogin() throws BayMotorsException {
        String email = getStringInput("Enter email: ");
        String password = getStringInput("Enter password: ");

        User user = dataStore.getUserByEmail(email);
        if (user != null && user.authenticate(password)) {
            currentUser = user;
            System.out.println("Welcome, " + user.getName() + "!");
        } else {
            throw new BayMotorsException("Invalid credentials",
                    BayMotorsException.ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    /**
     * Handles user logout by clearing the current user session
     */
    private void handleLogout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    // ===== Utility Methods for Input Handling =====

    /**
     * Gets a string input from the user with the specified prompt
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Gets an integer input from the user with error handling
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    /**
     * Gets a double input from the user with error handling
     */
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    // ===== Manager Menu and Core Management Methods =====

    /**
     * Displays and handles the main manager menu options
     * Provides access to all manager-specific functionality
     */
    private void showManagerMenu() throws BayMotorsException {
        Manager manager = (Manager) currentUser;

        while (true) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. Add New Mechanic");
            System.out.println("2. Add New Customer");
            System.out.println("3. Register Vehicle");
            System.out.println("4. Create Task");
            System.out.println("5. Allocate Task");
            System.out.println("6. View All Tasks");
            System.out.println("7. View Team Members");
            System.out.println("8. Send Notification");
            System.out.println("9. Manage Parts/Suppliers");
            System.out.println("10. Logout");

            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1:
                    handleAddMechanic(manager);
                    break;
                case 2:
                    handleAddCustomer();
                    break;
                case 3:
                    handleRegisterVehicle();
                    break;
                case 4:
                    handleCreateTask();
                    break;
                case 5:
                    handleAllocateTask(manager);
                    break;
                case 6:
                    handleViewAllTasks();
                    break;
                case 7:
                    handleViewTeam(manager);
                    break;
                case 8:
                    handleSendNotification(manager);
                    break;
                case 9:
                    handlePartsManagement();
                    break;
                case 10:
                    handleLogout();
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    // ===== Team Management Methods =====

    /**
     * Handles the process of adding a new mechanic to the system
     * Creates a new mechanic account and adds them to the manager's team
     */
    private void handleAddMechanic(Manager manager) throws BayMotorsException {
        System.out.println("\n=== Add New Mechanic ===");
        String name = getStringInput("Enter name: ");
        String email = getStringInput("Enter email: ");
        String password = getStringInput("Enter password: ");

        try {
            Mechanic mechanic = new Mechanic(
                    dataStore.getNextUserId(),
                    name,
                    email,
                    password
            );
            dataStore.addUser(mechanic);
            manager.addTeamMember(mechanic);
            System.out.println("Mechanic added successfully!");
        } catch (IllegalArgumentException e) {
            throw new BayMotorsException("Invalid mechanic data: " + e.getMessage(),
                    BayMotorsException.ErrorCode.INVALID_INPUT);
        }
    }

    /**
     * Displays all team members and their current task assignments
     */
    private void handleViewTeam(Manager manager) {
        List<Mechanic> teamMembers = manager.getTeamMembers();
        if (teamMembers.isEmpty()) {
            System.out.println("No team members found.");
            return;
        }

        System.out.println("\n=== Team Members ===");
        for (Mechanic mechanic : teamMembers) {
            System.out.println("\nMechanic ID: " + mechanic.getId());
            System.out.println("Name: " + mechanic.getName());
            System.out.println("Email: " + mechanic.getEmail());

            List<Task> assignedTasks = mechanic.getAssignedTasks();
            System.out.println("Current Tasks: " + assignedTasks.size());

            if (!assignedTasks.isEmpty()) {
                System.out.println("Assigned Tasks:");
                for (Task task : assignedTasks) {
                    System.out.printf("- %s (Priority: %d, Status: %s)\n",
                            task.getDescription(),
                            task.getPriority(),
                            task.getState().getStatus());
                }
            }
            System.out.println("------------------------");
        }
    }

    // ===== Notification Management Methods =====

    /**
     * Handles the notification sending menu and options
     */
    private void handleSendNotification(Manager manager) {
        System.out.println("\n=== Send Notification ===");
        System.out.println("1. Send to All Registered Customers");
        System.out.println("2. Send to Specific Customer");
        System.out.println("3. Back");

        int choice = getIntInput("Choose option: ");

        try {
            switch (choice) {
                case 1:
                    sendNotificationToAllCustomers();
                    break;
                case 2:
                    sendNotificationToSpecificCustomer();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }
    }

    /**
     * Sends notifications to all registered customers in the system
     */
    private void sendNotificationToAllCustomers() {
        List<Customer> registeredCustomers = dataStore.getAllCustomers().stream()
                .filter(Customer::isRegistered)
                .collect(Collectors.toList());

        if (registeredCustomers.isEmpty()) {
            System.out.println("No registered customers found.");
            return;
        }

        System.out.println("\nSending notifications to " + registeredCustomers.size() + " customers");
        for (Customer customer : registeredCustomers) {
            NotificationFactory.createNotification(
                    NotificationType.OFFER,
                    customer
            ).send();
        }
        System.out.println("Notifications sent successfully!");
    }

    /**
     * Sends a notification to a specific customer with chosen notification type
     */
    private void sendNotificationToSpecificCustomer() {
        Customer customer = selectCustomer();
        if (customer == null) return;

        System.out.println("\nSelect notification type:");
        System.out.println("1. Offer");
        System.out.println("2. Registration Benefit");

        int choice = getIntInput("Choose type: ");
        NotificationType type;

        switch (choice) {
            case 1:
                type = NotificationType.OFFER;
                break;
            case 2:
                type = NotificationType.REGISTRATION_BENEFIT;
                break;
            default:
                System.out.println("Invalid option");
                return;
        }

        NotificationFactory.createNotification(type, customer).send();
        System.out.println("Notification sent successfully!");
    }

    /**
     * Handles task allocation to mechanics by the manager
     * Displays unallocated tasks and available mechanics for selection
     */
    private void handleAllocateTask(Manager manager) throws BayMotorsException {
        System.out.println("\n=== Allocate Task ===");

        // Get list of unallocated tasks
        List<Task> unallocatedTasks = dataStore.getAllTasks().stream()
                .filter(t -> t.getAssignedMechanic() == null)
                .collect(Collectors.toList());

        if (unallocatedTasks.isEmpty()) {
            System.out.println("No unallocated tasks available.");
            return;
        }

        // Display unallocated tasks
        System.out.println("\nUnallocated Tasks:");
        for (int i = 0; i < unallocatedTasks.size(); i++) {
            Task task = unallocatedTasks.get(i);
            System.out.printf("%d. %s (Priority: %d)\n",
                    i + 1, task.getDescription(), task.getPriority());
        }

        // Get task selection
        int taskChoice = getIntInput("Select task number (0 to cancel): ");
        if (taskChoice == 0 || taskChoice > unallocatedTasks.size()) return;

        // Display available mechanics
        List<Mechanic> mechanics = manager.getTeamMembers();
        System.out.println("\nAvailable Mechanics:");
        for (int i = 0; i < mechanics.size(); i++) {
            Mechanic mechanic = mechanics.get(i);
            System.out.printf("%d. %s (%d current tasks)\n",
                    i + 1, mechanic.getName(), mechanic.getAssignedTasks().size());
        }

        // Get mechanic selection
        int mechanicChoice = getIntInput("Select mechanic number (0 to cancel): ");
        if (mechanicChoice == 0 || mechanicChoice > mechanics.size()) return;

        try {
            Task selectedTask = unallocatedTasks.get(taskChoice - 1);
            Mechanic selectedMechanic = mechanics.get(mechanicChoice - 1);
            manager.allocateTask(selectedTask, selectedMechanic);
            System.out.println("Task allocated successfully!");
        } catch (TaskException e) {
            throw new BayMotorsException("Failed to allocate task: " + e.getMessage(),
                    BayMotorsException.ErrorCode.TASK_ERROR);
        }
    }

    // ===== Mechanic Menu and Related Methods =====

    /**
     * Displays and handles the main mechanic menu options
     * Provides access to all mechanic-specific functionality
     */
    private void showMechanicMenu() throws BayMotorsException {
        Mechanic mechanic = (Mechanic) currentUser;

        while (true) {
            System.out.println("\n=== Mechanic Menu ===");
            System.out.println("1. View My Tasks");
            System.out.println("2. Complete Task");
            System.out.println("3. Update Task Status");
            System.out.println("4. View Vehicle Details");
            System.out.println("5. Logout");

            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1:
                    handleViewMyTasks(mechanic);
                    break;
                case 2:
                    handleCompleteTask(mechanic);
                    break;
                case 3:
                    handleUpdateTaskStatus(mechanic);
                    break;
                case 4:
                    handleViewVehicleDetails();
                    break;
                case 5:
                    handleLogout();
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    /**
     * Displays all tasks assigned to the current mechanic
     * Includes task details, priority, and vehicle information
     */
    private void handleViewMyTasks(Mechanic mechanic) {
        List<Task> assignedTasks = mechanic.getAssignedTasks();
        if (assignedTasks.isEmpty()) {
            System.out.println("No tasks currently assigned to you.");
            return;
        }

        System.out.println("\n=== Your Assigned Tasks ===");
        for (Task task : assignedTasks) {
            System.out.printf("\nTask ID: %d\n", task.getId());
            System.out.printf("Description: %s\n", task.getDescription());
            System.out.printf("Priority: %d\n", task.getPriority());
            System.out.printf("Status: %s\n", task.getState().getStatus());
            System.out.printf("Created: %s\n", task.getCreatedDate());

            Vehicle vehicle = task.getVehicle();
            if (vehicle != null) {
                System.out.printf("Vehicle: %s %s (%s)\n",
                        vehicle.getManufacturer().getName(),
                        vehicle.getModel(),
                        vehicle.getRegistration());
            }
            System.out.println("------------------------");
        }
    }

    /**
     * Handles the process of completing a task
     * Only shows active (non-completed) tasks for selection
     */
    private void handleCompleteTask(Mechanic mechanic) throws BayMotorsException {
        List<Task> activeTasks = mechanic.getAssignedTasks().stream()
                .filter(t -> t.getCompletedDate() == null)
                .collect(Collectors.toList());

        if (activeTasks.isEmpty()) {
            System.out.println("No active tasks to complete.");
            return;
        }

        System.out.println("\n=== Complete Task ===");
        for (int i = 0; i < activeTasks.size(); i++) {
            Task task = activeTasks.get(i);
            System.out.printf("%d. %s (Priority: %d)\n",
                    i + 1,
                    task.getDescription(),
                    task.getPriority());
        }

        int choice = getIntInput("Select task to complete (0 to cancel): ");
        if (choice > 0 && choice <= activeTasks.size()) {
            Task selectedTask = activeTasks.get(choice - 1);
            try {
                selectedTask.complete();
                System.out.println("Task completed successfully!");
            } catch (Exception e) {
                throw new BayMotorsException("Failed to complete task: " + e.getMessage(),
                        BayMotorsException.ErrorCode.TASK_ERROR);
            }
        }
    }

    /**
     * Handles updating the status of a task
     * Allows mechanics to change task states and provide updates
     */
    private void handleUpdateTaskStatus(Mechanic mechanic) throws BayMotorsException {
        // Get mechanic's assigned tasks
        List<Task> assignedTasks = mechanic.getAssignedTasks();
        if (assignedTasks.isEmpty()) {
            System.out.println("No tasks assigned to you.");
            return;
        }

        // Display tasks
        System.out.println("\n=== Your Assigned Tasks ===");
        for (int i = 0; i < assignedTasks.size(); i++) {
            Task task = assignedTasks.get(i);
            System.out.printf("%d. %s (Current Status: %s)\n",
                    i + 1,
                    task.getDescription(),
                    task.getState().getStatus());
        }

        // Select task to update
        int taskChoice = getIntInput("Select task to update (0 to cancel): ");
        if (taskChoice == 0 || taskChoice > assignedTasks.size()) return;

        Task selectedTask = assignedTasks.get(taskChoice - 1);

        // Show status options
        System.out.println("\nUpdate Status Options:");
        System.out.println("1. Mark as In Progress");
        System.out.println("2. Move to Next State");
        System.out.println("3. Mark as Complete");
        System.out.println("4. Cancel");

        int statusChoice = getIntInput("Choose new status: ");

        try {
            switch (statusChoice) {
                case 1:
                    selectedTask.setState(new WaitingState());
                    System.out.println("Task returned to Waiting state");
                    break;
                case 2:
                    selectedTask.getState().next(selectedTask);
                    System.out.println("Task moved to next state");
                    break;
                case 3:
                    selectedTask.complete();
                    System.out.println("Task marked as Complete");
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        } catch (Exception e) {
            throw new BayMotorsException("Failed to update task status: " + e.getMessage(),
                    BayMotorsException.ErrorCode.TASK_ERROR);
        }
    }

    /**
     * Displays detailed information about a selected vehicle
     * Including owner details and task history
     */
    private void handleViewVehicleDetails() throws BayMotorsException {
        Vehicle vehicle = selectVehicle();
        if (vehicle == null) return;

        System.out.println("\n=== Vehicle Details ===");
        System.out.println("Registration: " + vehicle.getRegistration());
        System.out.println("Make: " + vehicle.getManufacturer().getName());
        System.out.println("Model: " + vehicle.getModel());
        System.out.println("Year: " + vehicle.getYear());

        // Owner details
        if (vehicle.getOwner() != null) {
            Customer owner = vehicle.getOwner();
            System.out.println("\nOwner Details:");
            System.out.println("Name: " + owner.getName());
            System.out.println("Email: " + owner.getEmail());
            System.out.println("Phone: " + owner.getPhone());
            System.out.println("Status: " + (owner.isRegistered() ? "Registered" : "Unregistered"));
        }

        // Task history
        List<Task> taskHistory = vehicle.getTaskHistory();
        if (!taskHistory.isEmpty()) {
            System.out.println("\nTask History:");
            for (Task task : taskHistory) {
                System.out.printf("- %s (Status: %s, Priority: %d)\n",
                        task.getDescription(),
                        task.getState().getStatus(),
                        task.getPriority());
            }
        } else {
            System.out.println("\nNo task history available.");
        }
    }

    // ===== Vehicle Management Methods =====

    /**
     * Handles the vehicle registration process
     * Links vehicles with customers and manufacturers
     */
    private void handleRegisterVehicle() throws BayMotorsException {
        System.out.println("\n=== Register Vehicle ===");

        // First, select or create customer
        Customer customer = selectOrCreateCustomer();
        if (customer == null) return;

        // Get vehicle details
        String registration = getStringInput("Enter vehicle registration: ");
        String make = getStringInput("Enter vehicle make: ");
        String model = getStringInput("Enter vehicle model: ");
        int year = getIntInput("Enter vehicle year: ");

        // Get or create manufacturer
        Manufacturer manufacturer = selectOrCreateManufacturer(make);

        try {
            Vehicle vehicle = new Vehicle(
                    dataStore.getNextVehicleId(),
                    registration,
                    manufacturer,
                    model,
                    year
            );
            vehicle.setOwner(customer);
            dataStore.addVehicle(vehicle);
            System.out.println("Vehicle registered successfully!");
        } catch (IllegalArgumentException e) {
            throw new BayMotorsException("Invalid vehicle data: " + e.getMessage(),
                    BayMotorsException.ErrorCode.INVALID_INPUT);
        }
    }

    /**
     * Displays a list of vehicles and allows selection
     *
     * @return Selected vehicle or null if cancelled
     */
    private Vehicle selectVehicle() {
        List<Vehicle> vehicles = dataStore.getAllVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return null;
        }

        System.out.println("\nAvailable Vehicles:");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            String ownerName = (vehicle.getOwner() != null) ? vehicle.getOwner().getName() : "No Owner";
            String manufacturerName = (vehicle.getManufacturer() != null) ?
                    vehicle.getManufacturer().getName() : "Unknown Manufacturer";

            System.out.printf("%d. %s %s (%s) - Owner: %s\n",
                    i + 1,
                    manufacturerName,
                    vehicle.getModel(),
                    vehicle.getRegistration(),
                    ownerName);
        }

        while (true) {
            int choice = getIntInput("Select vehicle number (0 to cancel): ");
            if (choice == 0) {
                return null;
            }
            if (choice > 0 && choice <= vehicles.size()) {
                return vehicles.get(choice - 1);
            }
            System.out.println("Invalid selection. Please try again.");
        }
    }

    // ===== Customer Management Methods =====

    /**
     * Handles the process of adding a new customer
     *
     * @return The newly created customer
     */
    private Customer handleAddCustomer() throws BayMotorsException {
        System.out.println("\n=== Add New Customer ===");
        String name = getStringInput("Enter name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");

        try {
            Customer customer = new Customer(
                    dataStore.getNextCustomerId(),
                    name,
                    email,
                    phone
            );
            dataStore.addCustomer(customer);
            System.out.println("Customer added successfully!");
            return customer;
        } catch (IllegalArgumentException e) {
            throw new BayMotorsException("Invalid customer data: " + e.getMessage(),
                    BayMotorsException.ErrorCode.INVALID_INPUT);
        }
    }

    /**
     * Displays a list of customers and allows selection
     *
     * @return Selected customer or null if cancelled
     */
    private Customer selectCustomer() {
        List<Customer> customers = dataStore.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return null;
        }

        System.out.println("\nAvailable Customers:");
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            System.out.printf("%d. %s (%s) - %s\n",
                    i + 1,
                    customer.getName(),
                    customer.getEmail(),
                    customer.isRegistered() ? "Registered" : "Unregistered");
        }

        int choice = getIntInput("Select customer number (0 to cancel): ");
        if (choice == 0 || choice > customers.size()) {
            return null;
        }
        return customers.get(choice - 1);
    }

    /**
     * Allows selecting an existing customer or creating a new one
     *
     * @return Selected or created customer, or null if cancelled
     */
    private Customer selectOrCreateCustomer() throws BayMotorsException {
        System.out.println("\n1. Select Existing Customer");
        System.out.println("2. Create New Customer");
        System.out.println("3. Cancel");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                return selectCustomer();
            case 2:
                return handleAddCustomer();
            default:
                return null;
        }
    }

    // ===== Manufacturer Management Methods =====

    /**
     * Handles manufacturer selection or creation by name
     *
     * @param name The manufacturer name to find or create
     * @return The selected or created manufacturer
     */
    private Manufacturer selectOrCreateManufacturer(String name) throws BayMotorsException {
        Manufacturer manufacturer = dataStore.getManufacturerByName(name);
        if (manufacturer != null) {
            return manufacturer;
        }

        try {
            manufacturer = new Manufacturer(
                    dataStore.getNextManufacturerId(),
                    name
            );
            dataStore.addManufacturer(manufacturer);
            return manufacturer;
        } catch (IllegalArgumentException e) {
            throw new BayMotorsException("Invalid manufacturer data: " + e.getMessage(),
                    BayMotorsException.ErrorCode.INVALID_INPUT);
        }
    }

    /**
     * Displays a list of manufacturers and allows selection
     *
     * @return Selected manufacturer or null if cancelled
     */
    private Manufacturer selectManufacturer() {
        List<Manufacturer> manufacturers = dataStore.getAllManufacturers();
        if (manufacturers.isEmpty()) {
            System.out.println("No manufacturers found.");
            return null;
        }

        System.out.println("\nAvailable Manufacturers:");
        for (int i = 0; i < manufacturers.size(); i++) {
            Manufacturer manufacturer = manufacturers.get(i);
            System.out.printf("%d. %s\n",
                    i + 1,
                    manufacturer.getName());
        }

        int choice = getIntInput("Select manufacturer number (0 to cancel): ");
        if (choice == 0 || choice > manufacturers.size()) {
            return null;
        }
        return manufacturers.get(choice - 1);
    }

    // ===== Parts and Supplier Management Methods =====

    /**
     * Main menu handler for parts management functionality
     * Provides options for managing parts and suppliers
     */
    private void handlePartsManagement() {
        while (true) {
            System.out.println("\n=== Parts Management ===");
            System.out.println("1. Add New Supplier");
            System.out.println("2. Add New Part");
            System.out.println("3. View All Parts");
            System.out.println("4. View All Suppliers");
            System.out.println("5. Back to Main Menu");

            int choice = getIntInput("Choose option: ");

            try {
                switch (choice) {
                    case 1:
                        handleAddSupplier();
                        break;
                    case 2:
                        handleAddPart();
                        break;
                    case 3:
                        handleViewAllParts();
                        break;
                    case 4:
                        handleViewAllSuppliers();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the process of adding a new supplier to the system
     */
    private void handleAddSupplier() {
        System.out.println("\n=== Add New Supplier ===");
        String name = getStringInput("Enter supplier name: ");
        String contactPerson = getStringInput("Enter contact person name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");
        String address = getStringInput("Enter address: ");

        try {
            Supplier supplier = new Supplier(
                    dataStore.getNextSupplierId(),
                    name,
                    contactPerson,
                    email,
                    phone,
                    address
            );
            dataStore.addSupplier(supplier);
            System.out.println("Supplier added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding supplier: " + e.getMessage());
        }
    }

    /**
     * Handles the process of adding a new part to the system
     * Links parts with manufacturers and suppliers
     */
    private void handleAddPart() {
        System.out.println("\n=== Add New Part ===");

        // Select manufacturer
        Manufacturer manufacturer = selectManufacturer();
        if (manufacturer == null) return;

        // Select supplier
        Supplier supplier = selectSupplier();
        if (supplier == null) return;

        // Get part details
        String name = getStringInput("Enter part name: ");
        String code = getStringInput("Enter part code: ");
        double price = getDoubleInput("Enter part price: ");

        try {
            Part part = new Part(
                    dataStore.getNextPartId(),
                    name,
                    code,
                    price,
                    manufacturer,
                    supplier
            );
            dataStore.addPart(part);
            System.out.println("Part added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding part: " + e.getMessage());
        }
    }

    /**
     * Displays a list of all parts in the system with their details
     */
    private void handleViewAllParts() {
        List<Part> parts = dataStore.getAllParts();
        if (parts.isEmpty()) {
            System.out.println("No parts found.");
            return;
        }

        System.out.println("\n=== All Parts ===");
        for (Part part : parts) {
            System.out.println("\nPart ID: " + part.getId());
            System.out.println("Name: " + part.getName());
            System.out.println("Code: " + part.getCode());
            System.out.println("Price: £" + part.getPrice());
            System.out.println("Manufacturer: " + part.getManufacturer().getName());
            System.out.println("Supplier: " + part.getSupplier().getName());
            System.out.println("------------------------");
        }
    }

    /**
     * Displays a list of all suppliers in the system with their details
     */
    private void handleViewAllSuppliers() {
        List<Supplier> suppliers = dataStore.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return;
        }

        System.out.println("\n=== All Suppliers ===");
        for (Supplier supplier : suppliers) {
            System.out.println("\nSupplier ID: " + supplier.getId());
            System.out.println("Name: " + supplier.getName());
            System.out.println("Contact Person: " + supplier.getContactPerson());
            System.out.println("Email: " + supplier.getEmail());
            System.out.println("Phone: " + supplier.getPhone());
            System.out.println("Address: " + supplier.getAddress());
            System.out.println("------------------------");
        }
    }

    /**
     * Helper method to select a supplier from the list
     *
     * @return Selected supplier or null if cancelled
     */
    private Supplier selectSupplier() {
        List<Supplier> suppliers = dataStore.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return null;
        }

        System.out.println("\nAvailable Suppliers:");
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            System.out.printf("%d. %s (%s)\n",
                    i + 1,
                    supplier.getName(),
                    supplier.getContactPerson());
        }

        int choice = getIntInput("Select supplier number (0 to cancel): ");
        if (choice == 0 || choice > suppliers.size()) {
            return null;
        }
        return suppliers.get(choice - 1);
    }

    /**
     * Helper method to create a new supplier
     *
     * @return Newly created supplier or null if creation fails
     */
    private Supplier createNewSupplier() {
        System.out.println("\n=== Add New Supplier ===");
        String name = getStringInput("Enter supplier name: ");
        String contactPerson = getStringInput("Enter contact person name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");
        String address = getStringInput("Enter address: ");

        try {
            Supplier supplier = new Supplier(
                    dataStore.getNextSupplierId(),
                    name,
                    contactPerson,
                    email,
                    phone,
                    address
            );
            dataStore.addSupplier(supplier);
            System.out.println("Supplier added successfully!");
            return supplier;
        } catch (Exception e) {
            System.out.println("Error adding supplier: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper method that allows selecting an existing supplier or creating a new one
     *
     * @return Selected or created supplier, or null if cancelled
     */
    private Supplier selectOrCreateSupplier() {
        System.out.println("\n1. Select Existing Supplier");
        System.out.println("2. Create New Supplier");
        System.out.println("3. Cancel");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                return selectSupplier();
            case 2:
                return createNewSupplier();
            default:
                return null;
        }
    }

    // ===== Task Management Methods =====

    /**
     * Handles the creation of new tasks
     * Links tasks with vehicles and sets initial properties
     */
    private void handleCreateTask() throws BayMotorsException {
        System.out.println("\n=== Create New Task ===");

        Vehicle vehicle = selectVehicle();
        if (vehicle == null) return;

        String description = getStringInput("Enter task description: ");
        int priority = getIntInput("Enter priority (1-High, 2-Medium, 3-Low): ");

        try {
            Task task = new Task(
                    dataStore.getNextTaskId(),
                    description,
                    priority,
                    vehicle
            );
            dataStore.addTask(task);
            vehicle.addTask(task);
            System.out.println("Task created successfully!");
        } catch (IllegalArgumentException e) {
            throw new BayMotorsException("Invalid task data: " + e.getMessage(),
                    BayMotorsException.ErrorCode.INVALID_INPUT);
        }
    }

    /**
     * Displays all tasks in the system with comprehensive details
     * Including task status, assigned mechanic, and vehicle information
     */
    private void handleViewAllTasks() {
        List<Task> allTasks = dataStore.getAllTasks();
        if (allTasks.isEmpty()) {
            System.out.println("No tasks found in the system.");
            return;
        }

        System.out.println("\n=== All Tasks ===");
        for (Task task : allTasks) {
            // Basic task information
            System.out.println("\nTask ID: " + task.getId());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Priority: " + task.getPriority());
            System.out.println("Status: " + task.getState().getStatus());
            System.out.println("Created: " + task.getCreatedDate());

            // Completion information if available
            if (task.getCompletedDate() != null) {
                System.out.println("Completed: " + task.getCompletedDate());
            }

            // Assigned mechanic information
            if (task.getAssignedMechanic() != null) {
                System.out.println("Assigned to: " + task.getAssignedMechanic().getName());
            }

            // Vehicle and owner information
            if (task.getVehicle() != null) {
                Vehicle vehicle = task.getVehicle();
                System.out.printf("Vehicle: %s %s (%s)\n",
                        vehicle.getManufacturer().getName(),
                        vehicle.getModel(),
                        vehicle.getRegistration());

                if (vehicle.getOwner() != null) {
                    System.out.println("Owner: " + vehicle.getOwner().getName());
                }
            }
            System.out.println("------------------------");
        }
    }

    /**
     * Creates a new instance of a manufacturer
     *
     * @return Newly created manufacturer or null if creation fails
     */
    private Manufacturer createNewManufacturer() {
        System.out.println("\n=== Add New Manufacturer ===");
        String name = getStringInput("Enter manufacturer name: ");

        try {
            Manufacturer manufacturer = new Manufacturer(
                    dataStore.getNextManufacturerId(),
                    name
            );
            dataStore.addManufacturer(manufacturer);
            System.out.println("Manufacturer added successfully!");
            return manufacturer;
        } catch (Exception e) {
            System.out.println("Error adding manufacturer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper method that allows selecting an existing manufacturer or creating a new one
     *
     * @return Selected or created manufacturer, or null if cancelled
     */
    private Manufacturer selectOrCreateManufacturer() {
        System.out.println("\n1. Select Existing Manufacturer");
        System.out.println("2. Create New Manufacturer");
        System.out.println("3. Cancel");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                return selectManufacturer();
            case 2:
                return createNewManufacturer();
            default:
                return null;
        }
    }
}