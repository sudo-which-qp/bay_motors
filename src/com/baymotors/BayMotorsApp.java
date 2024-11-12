package com.baymotors;

import com.baymotors.utils.MenuSystem;

public class BayMotorsApp {
    public static void main(String[] args) {
        System.out.println("Starting Bay Motors Management System...");
        System.out.println("Initializing data...");

        try {
            // Create and start the menu system
            MenuSystem menuSystem = new MenuSystem();

            // Display welcome message and credentials
            System.out.println("\nWelcome to Bay Motors Management System!");
            System.out.println("----------------------------------------");
            System.out.println("Default Manager Login:");
            System.out.println("Email: admin@baymotors.com");
            System.out.println("Password: admin123");
            System.out.println("\nDefault Mechanic Login:");
            System.out.println("Email: john@baymotors.com");
            System.out.println("Password: mech123");
            System.out.println("----------------------------------------");

            // Start the menu system
            menuSystem.start();

        } catch (Exception e) {
            System.err.println("Error starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
