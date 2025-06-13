package spacex.main.mission;

import spacex.main.common.InMemoryRepository;

class MissionRepository extends InMemoryRepository<Mission> {

    @Override
    protected String getId(Mission mission) {
        return mission.getId();
    }
}
