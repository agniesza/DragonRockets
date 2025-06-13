package spacex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacex.main.mission.Mission;
import spacex.main.mission.MissionService;
import spacex.main.rocket.Rocket;
import spacex.main.rocket.RocketService;
import spacex.main.rocket.RocketStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RocketServiceTest {

    private RocketService rocketService;
    private MissionService missionService;

    @BeforeEach
    void setUp() {
        missionService = new MissionService();
        rocketService = new RocketService(missionService);
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

    @Test
    void shouldAssignRocketToMissionAndSetInSpaceStatus() {
        Rocket rocket = rocketService.addRocket("Dragon 4");
        Mission mission = missionService.addMission("LaLuna");

        boolean result = rocketService.assignToMission(rocket.getId(), mission.getId());

        assertTrue(result);
        assertEquals(mission.getId(), rocket.getAssignedMissionId());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    void shouldNotAssignRocketThatIsAlreadyAssigned() {
        Rocket rocket = rocketService.addRocket("Green Dragon");
        Mission m1 = missionService.addMission("Mission 1");
        Mission m2 = missionService.addMission("Mission 2");

        assertTrue(rocketService.assignToMission(rocket.getId(), m1.getId()));
        boolean result = rocketService.assignToMission(rocket.getId(), m2.getId());

        assertFalse(result);
        assertEquals(m1.getId(), rocket.getAssignedMissionId());
    }

    @Test
    void shouldChangeRocketStatusAndUpdateMission() {
        Rocket rocket = rocketService.addRocket("Falcon Heavy");
        Mission mission = missionService.addMission("Orbit Gum");

        rocketService.assignToMission(rocket.getId(), mission.getId());

        rocketService.changeStatus(rocket.getId(), RocketStatus.IN_REPAIR);

        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());
    }

}
