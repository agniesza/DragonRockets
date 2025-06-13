package spacex.main.mission;

public enum MissionStatus {
    /*
    initial status, where no rockets are assigned
     */
    SCHEDULED,
    /*
    at least one rocket is assigned and one or more assigned rockets are in
repair
     */
    PENDING,
    /*
    at least one rocket is assigned and none of them is in repair
     */
    IN_PROGRESS,
    /*
    the final stage of the mission, at this point rockets should not be assigned
anymore to a mission
     */
    ENDED
}
