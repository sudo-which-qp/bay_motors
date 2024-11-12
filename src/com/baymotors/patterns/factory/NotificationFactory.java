package com.baymotors.patterns.factory;

import com.baymotors.models.Customer;

public class NotificationFactory {
    public static Notification createNotification(NotificationType type, Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }


        switch (type) {
            case TASK_COMPLETE:
                return new TaskCompleteNotification(customer);
            case OFFER:
                return new OfferNotification(customer);
            case REGISTRATION_BENEFIT:
                return new RegistrationBenefitNotification(customer);
            default:
                throw new IllegalArgumentException("Unknown notification type");
        }
    }
}
