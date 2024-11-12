package com.baymotors.patterns.factory;

import com.baymotors.models.Customer;

public class OfferNotification extends BaseNotification{
    public OfferNotification(Customer customer) {
        super(customer);
        this.message = "Special offer for our valued customer: 10% off on your next service!";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
