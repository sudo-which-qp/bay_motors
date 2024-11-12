package com.baymotors.patterns.factory;

import com.baymotors.models.Customer;

public class TaskCompleteNotification extends BaseNotification{
    public TaskCompleteNotification(Customer customer) {
        super(customer);
        this.message = "Your vehicle is ready for pickup!";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
