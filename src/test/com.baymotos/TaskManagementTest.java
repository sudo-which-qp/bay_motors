package test.com.baymotos;

import com.baymotors.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagementTest {
    private Manager manager;
    private Mechanic mechanic;
    private Task task;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        manager = new Manager(1, "Test Manager", "manager@test.com", "pass123");
        mechanic = new Mechanic(2, "Test Mechanic", "mechanic@test.com", "pass123");
        vehicle = new Vehicle(1, "ABC123", new Manufacturer(1, "Toyota"), "Corolla", 2020);
        task = new Task(1, "Oil Change", 1, vehicle);
    }

    @Test
    void testTaskAllocation() {
        // Add mechanic to manager's team
        manager.addTeamMember(mechanic);

        // Test task allocation
        assertDoesNotThrow(() -> {
            manager.allocateTask(task, mechanic);
        });

        // Verify task is assigned to mechanic
        assertTrue(mechanic.getAssignedTasks().contains(task));
    }
}
