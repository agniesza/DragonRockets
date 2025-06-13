package spacex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacex.main.mission.Mission;
import spacex.main.mission.MissionService;
import spacex.main.mission.MissionStatus;
import spacex.main.rocket.Rocket;
import spacex.main.rocket.RocketService;
import spacex.main.rocket.RocketStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MissionIntegrationTest {

    private RocketService rocketService;
    private MissionService missionService;

    @BeforeEach
    void setUp() {
        missionService = new MissionService();
        rocketService = new RocketService(missionService);
        missionService.setRocketService(rocketService);
    }


    @Test
    void shouldEndMissionAndResetRockets() {
        Mission mission = missionService.addMission("Mission X");

        Rocket r1 = rocketService.addRocket("R1");
        rocketService.assignToMission(r1.getId(), mission.getId());
        rocketService.changeStatus(r1.getId(), RocketStatus.IN_SPACE);

        Rocket r2 = rocketService.addRocket("R2");
        rocketService.assignToMission(r2.getId(), mission.getId());
        rocketService.changeStatus(r2.getId(), RocketStatus.IN_REPAIR);

        rocketService.assignToMission(r1.getId(), mission.getId());
        rocketService.assignToMission(r2.getId(), mission.getId());

        missionService.endMission(mission.getId());

        assertEquals(MissionStatus.ENDED, mission.getStatus());
        assertTrue(mission.getRockets().isEmpty());
        assertNull(r1.getAssignedMissionId());
        assertEquals(RocketStatus.ON_GROUND, r1.getStatus());
        assertNull(r2.getAssignedMissionId());
        assertEquals(RocketStatus.ON_GROUND, r2.getStatus());
    }

    @Test
    void shouldNotAssignRocketToEndedMission() {
        Rocket rocket = rocketService.addRocket("Dragon 00");
        Mission mission = missionService.addMission("Final");

        missionService.changeStatus(mission.getId(), MissionStatus.ENDED);

        boolean result = rocketService.assignToMission(rocket.getId(), mission.getId());

        assertFalse(result);
        assertNull(rocket.getAssignedMissionId());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
    }

}
