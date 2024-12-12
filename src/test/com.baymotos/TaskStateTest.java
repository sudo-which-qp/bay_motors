package test.com.baymotos;

import com.baymotors.models.Manufacturer;
import com.baymotors.models.Task;
import com.baymotors.models.Vehicle;
import com.baymotors.patterns.state.CompletedState;
import com.baymotors.patterns.state.InProgressState;
import com.baymotors.patterns.state.WaitingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStateTest {

    private Task task;

    @BeforeEach
    void setUp() {
        Vehicle vehicle = new Vehicle(1, "ABC123", new Manufacturer(1, "Toyota"), "Corolla", 2020);
        task = new Task(1, "Oil Change", 1, vehicle);
    }

    @Test
    void testTaskStateTransitions() {
        // Initial state should be Waiting
        assertTrue(task.getState() instanceof WaitingState);

        // Transition to In Progress
        task.getState().next(task);
        assertTrue(task.getState() instanceof InProgressState);

        // Transition to Completed
        task.getState().next(task);
        assertTrue(task.getState() instanceof CompletedState);
    }
}
