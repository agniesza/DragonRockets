package spacex.main.rocket;

import spacex.main.common.InMemoryRepository;

class RocketRepository extends InMemoryRepository<Rocket> {

    @Override
    protected String getId(Rocket rocket) {
        return rocket.getId();
    }
}
