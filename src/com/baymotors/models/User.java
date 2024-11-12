package com.baymotors.models;

public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Abstract method for role-based permissions
    public abstract boolean hasPermission(String action);

    public boolean authenticate(String attemptedPassword) {
        return this.password.equals(attemptedPassword);
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }
}
