package com.baymotors.patterns.factory;

import com.baymotors.models.Customer;

public class RegistrationBenefitNotification extends BaseNotification{
    public RegistrationBenefitNotification(Customer customer) {
        super(customer);
        this.message = "Register with us to receive exclusive benefits: Priority booking, special discounts, and more!";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
