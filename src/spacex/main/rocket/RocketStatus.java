package spacex.main.rocket;

public enum RocketStatus {
    /*
    Initial status, where the rocket is not assigned to any mission
     */
    ON_GROUND,
    /*
    The rocket was assigned to the mission
     */
    IN_SPACE,
    /*
    The rocket is due to repair, it implies “Pending” status of the mission
     */
    IN_REPAIR
}
