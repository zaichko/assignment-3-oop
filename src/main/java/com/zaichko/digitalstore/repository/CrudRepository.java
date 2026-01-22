package com.zaichko.digitalstore.repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository <T> {

    void create(T entity) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(Integer id) throws SQLException;

    void update(Integer id, T entity) throws SQLException;

    void delete(Integer id) throws SQLException;

}
