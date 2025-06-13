package spacex.main.rocket;

import java.util.UUID;

public class Rocket {
    private final String id = UUID.randomUUID().toString();
    private String name;
    private RocketStatus status = RocketStatus.ON_GROUND;
    private String assignedMissionId;

    public Rocket(String name) {
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

    public RocketStatus getStatus() {
        return status;
    }

    void setStatus(RocketStatus status) {
        this.status = status;
    }

    public String getAssignedMissionId() {
        return assignedMissionId;
    }

    void setAssignedMissionId(String assignedMissionId) {
        this.assignedMissionId = assignedMissionId;
    }
}

