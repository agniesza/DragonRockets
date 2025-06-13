package spacex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacex.main.mission.Mission;
import spacex.main.mission.MissionService;
import spacex.main.mission.MissionStatus;
import spacex.main.rocket.Rocket;
import spacex.main.rocket.RocketService;
import spacex.main.rocket.RocketStatus;

import java.util.List;

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

    @Test
    void shouldReturnFormattedMissionsSummarySortedByRocketCountAndName() {
        Mission transit = missionService.addMission("Transit");
        Rocket redDragon = rocketService.addRocket("Red Dragon");
        Rocket dragonXL = rocketService.addRocket("Dragon XL");
        Rocket falconHeavy = rocketService.addRocket("Falcon Heavy");

        transit.getRockets().add(redDragon);
        rocketService.assignToMission(redDragon.getId(), transit.getId());
        rocketService.changeStatus(redDragon.getId(), RocketStatus.ON_GROUND);

        transit.getRockets().add(dragonXL);
        rocketService.assignToMission(dragonXL.getId(), transit.getId());
        rocketService.changeStatus(dragonXL.getId(), RocketStatus.IN_SPACE);

        transit.getRockets().add(falconHeavy);
        rocketService.assignToMission(falconHeavy.getId(), transit.getId());
        rocketService.changeStatus(falconHeavy.getId(), RocketStatus.IN_SPACE);

        missionService.updateMissionStatusBasedOnRockets(transit);

        Mission luna1 = missionService.addMission("Luna1");
        Rocket dragon1 = rocketService.addRocket("Dragon 1");
        Rocket dragon2 = rocketService.addRocket("Dragon 2");

        rocketService.assignToMission(dragon1.getId(), luna1.getId());
        rocketService.assignToMission(dragon2.getId(), luna1.getId());
        rocketService.changeStatus(dragon1.getId(), RocketStatus.IN_SPACE);

        luna1.getRockets().add(dragon2);
        rocketService.assignToMission(dragon2.getId(), luna1.getId());
        rocketService.changeStatus(dragon2.getId(), RocketStatus.IN_REPAIR);

        missionService.updateMissionStatusBasedOnRockets(luna1);

        missionService.addMission("Mars");
        missionService.addMission("Luna2");

        Mission verticalLanding = missionService.addMission("Vertical Landing");
        missionService.changeStatus(verticalLanding.getId(), MissionStatus.ENDED);

        Rocket dragon3 = rocketService.addRocket("Dragon 3");

        Mission doubleLanding = missionService.addMission("Double Landing");
        missionService.changeStatus(doubleLanding.getId(), MissionStatus.ENDED);

        List<String> summary = missionService.getMissionsSummary();

        assertEquals(6, summary.size());

        assertEquals("""
                \n• Transit – IN_PROGRESS – Dragons: 3
                   o Dragon XL – IN_SPACE
                   o Falcon Heavy – IN_SPACE
                   o Red Dragon – ON_GROUND""", summary.get(0));

        assertEquals("""
                \n• Luna1 – PENDING – Dragons: 2
                   o Dragon 1 – IN_SPACE
                   o Dragon 2 – IN_REPAIR""", summary.get(1));

        assertEquals("\n• Vertical Landing – ENDED – Dragons: 0", summary.get(2));
        assertEquals("\n• Mars – SCHEDULED – Dragons: 0", summary.get(3));
        assertEquals("\n• Luna2 – SCHEDULED – Dragons: 0", summary.get(4));
        assertEquals("\n• Double Landing – ENDED – Dragons: 0", summary.get(5));
    }

    @Test
    void shouldChangeRocketStatusAndUpdateMission() {
        Rocket rocket = rocketService.addRocket("Falcon Heavy");
        Mission mission = missionService.addMission("Orbit Gum");

        rocketService.assignToMission(rocket.getId(), mission.getId());

        rocketService.changeStatus(rocket.getId(), RocketStatus.IN_REPAIR);

        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());
        assertEquals(MissionStatus.PENDING, mission.getStatus());

        rocketService.changeStatus(rocket.getId(), RocketStatus.IN_SPACE);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

    @Test
    void shouldAssignRocketToMissionAndUpdateStatusesStatus() {
        Rocket rocket = rocketService.addRocket("Dragon 4");
        Mission mission = missionService.addMission("LaLuna");

        boolean result = rocketService.assignToMission(rocket.getId(), mission.getId());

        assertTrue(result);
        assertEquals(mission.getId(), rocket.getAssignedMissionId());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
        assertEquals(1, mission.getRockets().size());
        assertTrue(mission.getRockets().contains(rocket));
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

    @Test
    void shouldAssignManyRocketsToMissionAndUpdateStatusesStatus() {
        Rocket rocket1 = rocketService.addRocket("Dragon 4");
        Rocket rocket2 = rocketService.addRocket("Dragon 8");
        Mission mission = missionService.addMission("LaLuna");

        boolean result1 = rocketService.assignToMission(rocket1.getId(), mission.getId());
        boolean result2 = rocketService.assignToMission(rocket2.getId(), mission.getId());

        assertTrue(result1);
        assertTrue(result2);
        assertEquals(mission.getId(), rocket1.getAssignedMissionId());
        assertEquals(mission.getId(), rocket2.getAssignedMissionId());
        assertEquals(RocketStatus.IN_SPACE, rocket1.getStatus());
        assertEquals(RocketStatus.IN_SPACE, rocket2.getStatus());
        assertEquals(2, mission.getRockets().size());
        assertTrue(mission.getRockets().contains(rocket1));
        assertTrue(mission.getRockets().contains(rocket2));
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

    @Test
    void shouldSetStatusToPendingIfOneRocketIsInRepair() {
        Mission mission = missionService.addMission("Mission A");
        Rocket r1 = rocketService.addRocket("Rocket 1");
        Rocket r2 = rocketService.addRocket("Rocket 2");

        rocketService.changeStatus(r1.getId(), RocketStatus.IN_SPACE);
        rocketService.changeStatus(r2.getId(), RocketStatus.IN_REPAIR);

        rocketService.assignToMission(r1.getId(), mission.getId());
        rocketService.assignToMission(r2.getId(), mission.getId());

        missionService.updateMissionStatusBasedOnRockets(mission);

        assertEquals(MissionStatus.PENDING, mission.getStatus());
    }

    @Test
    void shouldSetStatusToPendingIfAllRocketIsInRepair() {
        Mission mission = missionService.addMission("Mission A");
        Rocket r1 = rocketService.addRocket("Rocket 1");
        Rocket r2 = rocketService.addRocket("Rocket 2");

        rocketService.changeStatus(r1.getId(), RocketStatus.IN_REPAIR);
        rocketService.changeStatus(r2.getId(), RocketStatus.IN_REPAIR);

        rocketService.assignToMission(r1.getId(), mission.getId());
        rocketService.assignToMission(r2.getId(), mission.getId());

        missionService.updateMissionStatusBasedOnRockets(mission);

        assertEquals(MissionStatus.PENDING, mission.getStatus());
    }

    @Test
    void shouldSetStatusToInProgressIfNoRocketsInRepair() {
        Mission mission = missionService.addMission("Mission B");
        Rocket r1 = rocketService.addRocket("Rocket A");
        Rocket r2 = rocketService.addRocket("Rocket B");

        rocketService.changeStatus(r1.getId(), RocketStatus.IN_SPACE);
        rocketService.changeStatus(r2.getId(), RocketStatus.ON_GROUND);

        rocketService.assignToMission(r1.getId(), mission.getId());
        rocketService.assignToMission(r2.getId(), mission.getId());

        missionService.updateMissionStatusBasedOnRockets(mission);

        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

}
