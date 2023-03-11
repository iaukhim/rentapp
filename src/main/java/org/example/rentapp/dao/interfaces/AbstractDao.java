package org.example.rentapp.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface AbstractDao<T, PK> {

    Optional<T> loadById(PK id);

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    List<T> loadAll();
}
