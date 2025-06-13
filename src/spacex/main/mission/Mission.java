package spacex.main.mission;

import spacex.main.rocket.Rocket;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Mission {
    private final String id = UUID.randomUUID().toString();
    private String name;
    private MissionStatus status = MissionStatus.SCHEDULED;
    private final Set<Rocket> rockets = new HashSet<>();

    public Mission(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    void setStatus(MissionStatus status) {
        this.status = status;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }
}
