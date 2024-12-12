package test.com.baymotos;

import com.baymotors.models.Manager;
import com.baymotors.models.Mechanic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private Manager manager;
    private Mechanic mechanic;

    @BeforeEach
    void setUp() {
        manager = new Manager(1, "Test Manager", "manager@test.com", "pass123");
        mechanic = new Mechanic(2, "Test Mechanic", "mechanic@test.com", "pass123");
    }

//    @Test
    void testUserAuthentication() {
        // Test valid credentials
        assertTrue(manager.authenticate("pass123"), "Manager should authenticate with correct password");
        assertTrue(mechanic.authenticate("pass123"), "Mechanic should authenticate with correct password");

        // Test invalid credentials
        assertFalse(manager.authenticate("wrongpass"), "Manager should not authenticate with wrong password");
        assertFalse(mechanic.authenticate("wrongpass"), "Mechanic should not authenticate with wrong password");
    }
}
