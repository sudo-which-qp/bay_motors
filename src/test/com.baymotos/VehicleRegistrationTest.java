package test.com.baymotos;

import com.baymotors.models.Customer;
import com.baymotors.models.Manufacturer;
import com.baymotors.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleRegistrationTest {
    private Customer customer;
    private Vehicle vehicle;
    private Manufacturer manufacturer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "John Doe", "john@test.com", "1234567890");
        manufacturer = new Manufacturer(1, "Toyota");
        vehicle = new Vehicle(1, "ABC123", manufacturer, "Corolla", 2020);
    }

    @Test
    void testVehicleRegistration() {
        // Test vehicle assignment to customer
        customer.addVehicle(vehicle);

        // Verify bi-directional relationship
        assertTrue(customer.getVehicles().contains(vehicle), "Customer should have the vehicle");
        assertEquals(customer, vehicle.getOwner(), "Vehicle should have the customer as owner");
    }
}
