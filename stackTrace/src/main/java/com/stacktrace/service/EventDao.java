package com.stacktrace.service;
import com.stacktrace.exception.DatabaseException;

import java.util.List;

public interface EventDao<T> {
    void create(T item) throws DatabaseException;
    T readOne(Integer Id) throws DatabaseException;
    List<T> readAll() throws DatabaseException;
    void updateOne(T item) throws DatabaseException;
    void deleteOne(Integer id) throws DatabaseException;
    void deleteAll() throws DatabaseException;

}
