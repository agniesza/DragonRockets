package spacex.main.rocket;

import spacex.main.mission.Mission;
import spacex.main.mission.MissionService;
import spacex.main.mission.MissionStatus;

import java.util.Optional;

public class RocketService {

    private final RocketRepository rocketRepository = new RocketRepository();
    private final MissionService missionService;

    public RocketService(MissionService ms) {
        this.missionService = ms;
    }

    public Rocket addRocket(String name) {
        Rocket rocket = new Rocket(name);
        rocketRepository.add(rocket);
        return rocket;
    }

    public void changeStatus(String rocketId, RocketStatus status) {
        Rocket rocket = rocketRepository.findById(rocketId).orElseThrow();
        rocket.setStatus(status);

        if (rocket.getAssignedMissionId() != null) {
            Mission mission = missionService.findMission(rocket.getAssignedMissionId()).orElseThrow();
            missionService.updateMissionStatusBasedOnRockets(mission);
        }
    }

    public Optional<Rocket> findRocket(String id) {
        return rocketRepository.findById(id);
    }

    public boolean assignToMission(String rocketId, String missionId) {
        Rocket rocket = rocketRepository.findById(rocketId).orElseThrow();
        Mission mission = missionService.findMission(missionId).orElseThrow();

        if (rocket.getAssignedMissionId() != null || MissionStatus.ENDED.equals(mission.getStatus())) {
            return false;
        }

        rocket.setAssignedMissionId(missionId);

        if (RocketStatus.ON_GROUND.equals(rocket.getStatus())) {
            rocket.setStatus(RocketStatus.IN_SPACE);
        }

        mission.getRockets().add(rocket);
        missionService.updateMissionStatusBasedOnRockets(mission);
        return true;
    }

    public void detachFromMission(String rocketId) {
        Rocket rocket = rocketRepository.findById(rocketId).orElseThrow();
        rocket.setAssignedMissionId(null);
        rocket.setStatus(RocketStatus.ON_GROUND);
    }

}
