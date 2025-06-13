package spacex.main.common;

import java.util.Collection;
import java.util.Optional;

interface Repository<T> {
    void add(T item);
    Optional<T> findById(String id);
    Collection<T> findAll();
}
