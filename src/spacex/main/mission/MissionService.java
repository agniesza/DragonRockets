package spacex.main.mission;

import lombok.NoArgsConstructor;
import spacex.main.rocket.RocketService;
import spacex.main.rocket.RocketStatus;

import java.util.Optional;

@NoArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository = new MissionRepository();
    private RocketService rocketService;

    public void setRocketService(RocketService rocketService) {
        this.rocketService = rocketService;
    }

    public Mission addMission(String name) {
        Mission mission = new Mission(name);
        missionRepository.add(mission);
        return mission;
    }

    public void changeStatus(String missionId, MissionStatus status) {
        Mission mission = missionRepository.findById(missionId).orElseThrow();
        mission.setStatus(status);
    }

    public Optional<Mission> findMission(String id) {
        return missionRepository.findById(id);
    }

    public void updateMissionStatusBasedOnRockets(Mission mission) {
        if (mission.getRockets().isEmpty()) {
            mission.setStatus(MissionStatus.SCHEDULED);
        } else {
            boolean anyInRepair = mission.getRockets().stream()
                    .anyMatch(r -> r.getStatus() == RocketStatus.IN_REPAIR);

            mission.setStatus(anyInRepair ? MissionStatus.PENDING : MissionStatus.IN_PROGRESS);
        }
    }

    public void endMission(String missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow();
        if (MissionStatus.ENDED.equals(mission.getStatus())) return;

        mission.getRockets().forEach(
                rocket -> rocketService.detachFromMission(rocket.getId())
        );

        mission.getRockets().clear();
        mission.setStatus(MissionStatus.ENDED);
    }
}
