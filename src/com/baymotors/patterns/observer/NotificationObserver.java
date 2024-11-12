package com.baymotors.patterns.observer;

import com.baymotors.models.Customer;

public class NotificationObserver implements Observer{
    private Customer customer;

    public NotificationObserver(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void update(String message) {
        if (customer.isRegistered()) {
            System.out.println("Notification for " + customer.getName() + ": " + message);
        }
    }
}
