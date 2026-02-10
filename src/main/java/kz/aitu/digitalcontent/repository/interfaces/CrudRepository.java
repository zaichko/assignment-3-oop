package kz.aitu.digitalcontent.repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    T create(T entity);

    List<T> getAll();

    Optional<T> getById(int id);

    T update(int id, T entity);

    boolean delete(int id);

    boolean exists(int id);
}