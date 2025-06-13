package spacex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacex.main.rocket.Rocket;
import spacex.main.rocket.RocketService;
import spacex.main.rocket.RocketStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RocketServiceTest {

    private RocketService rocketService;

    @BeforeEach
    void setUp() {
        rocketService = new RocketService();
    }

    @Test
    void shouldAddNewRocketWithOnGroundStatus() {
        Rocket rocket = rocketService.addRocket("Rocket 99");

        Optional<Rocket> updated = rocketService.findRocket(rocket.getId());

        assertNotNull(updated);
        assertEquals("Rocket 99", updated.get().getName());
        assertEquals(RocketStatus.ON_GROUND, updated.get().getStatus());
        assertNull(updated.get().getAssignedMissionId());
    }

    @Test
    void shouldChangeRocketStatus() {
        Rocket rocket = rocketService.addRocket("Falcon Heavy");

        rocketService.changeStatus(rocket.getId(), RocketStatus.IN_REPAIR);

        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());
    }

}
