package spacex.main.rocket;

import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
public class RocketService {

    private final RocketRepository rocketRepository = new RocketRepository();

    public Rocket addRocket(String name) {
        Rocket rocket = new Rocket(name);
        rocketRepository.add(rocket);
        return rocket;
    }

    public void changeStatus(String rocketId, RocketStatus status) {
        Rocket rocket = rocketRepository.findById(rocketId).orElseThrow();
        rocket.setStatus(status);
    }

    public Optional<Rocket> findRocket(String id) {
        return rocketRepository.findById(id);
    }
}
