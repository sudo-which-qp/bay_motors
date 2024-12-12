package test.com.baymotos;

import com.baymotors.models.Customer;
import com.baymotors.patterns.factory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "John Doe", "john@test.com", "1234567890");
    }

    @Test
    void testNotificationCreation() {
        // Test different types of notifications
        Notification taskNotification = NotificationFactory.createNotification(
                NotificationType.TASK_COMPLETE,
                customer
        );
        assertNotNull(taskNotification, "Task notification should be created");
        assertTrue(taskNotification instanceof TaskCompleteNotification);

        Notification offerNotification = NotificationFactory.createNotification(
                NotificationType.OFFER,
                customer
        );
        assertNotNull(offerNotification, "Offer notification should be created");
        assertTrue(offerNotification instanceof OfferNotification);

        // Test invalid notification type
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationFactory.createNotification(null, customer);
        });
    }
}
