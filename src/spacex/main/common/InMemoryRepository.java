package spacex.main.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<T> implements Repository<T> {
    private final Map<String, T> storage = new HashMap<>();

    protected abstract String getId(T entity);

    @Override
    public void add(T item) {
        storage.put(getId(item), item);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Collection<T> findAll() {
        return storage.values();
    }
}
