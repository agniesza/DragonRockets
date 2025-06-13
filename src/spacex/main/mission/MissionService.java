package spacex.main.mission;

import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository = new MissionRepository();

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
}
