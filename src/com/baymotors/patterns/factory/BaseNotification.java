package com.baymotors.patterns.factory;

import com.baymotors.models.Customer;

public abstract class BaseNotification implements Notification {
    protected Customer customer;
    protected String message;

    public BaseNotification(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean send() {
        if (customer != null && customer.getEmail() != null) {
            System.out.println("Sending to " + customer.getEmail() + ": " + getMessage());
            return true;
        }
        return false;
    }
}
