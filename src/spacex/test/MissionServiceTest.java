package spacex.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacex.main.mission.Mission;
import spacex.main.mission.MissionService;
import spacex.main.mission.MissionStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MissionServiceTest {

    private MissionService missionService;

    @BeforeEach
    void setUp() {
        missionService = new MissionService();
    }

    @Test
    void shouldAddNewMissionWithScheduledStatus() {
        Mission mission = missionService.addMission("Pluto");

        assertNotNull(mission);
        assertEquals("Pluto", mission.getName());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
    }

    @Test
    void shouldChangeMissionStatus() {
        Mission mission = missionService.addMission("Alpha");
        missionService.changeStatus(mission.getId(), MissionStatus.IN_PROGRESS);

        Optional<Mission> updated = missionService.findMission(mission.getId());
        assertEquals(MissionStatus.IN_PROGRESS, updated.get().getStatus());
    }

}
