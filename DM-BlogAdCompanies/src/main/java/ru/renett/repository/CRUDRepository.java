package ru.renett.repository;

import ru.renett.exceptions.DataBaseException;

import java.util.List;

public interface CRUDRepository<T> {
    void save(T entity) throws DataBaseException;
    void update(T entity) throws DataBaseException;
    void delete(T entity) throws DataBaseException;
    List<T> findAll() throws DataBaseException;
}
