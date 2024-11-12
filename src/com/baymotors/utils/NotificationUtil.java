package com.baymotors.utils;

import com.baymotors.models.Customer;
import com.baymotors.patterns.factory.Notification;
import com.baymotors.patterns.factory.NotificationFactory;
import com.baymotors.patterns.factory.NotificationType;

import java.util.List;

public class NotificationUtil {
    public static void sendNotification(NotificationType type, Customer customer) {
        try {
            Notification notification = NotificationFactory.createNotification(type, customer);
            if (!notification.send()) {
                System.out.println("Failed to send notification to customer: " + customer.getName());
            }
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }
    }

    public static void sendOfferToAllCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            if (customer.isRegistered()) {
                sendNotification(NotificationType.OFFER, customer);
            }
        }
    }

    public static void sendRegistrationBenefits(Customer customer) {
        if (!customer.isRegistered()) {
            sendNotification(NotificationType.REGISTRATION_BENEFIT, customer);
        }
    }
}
