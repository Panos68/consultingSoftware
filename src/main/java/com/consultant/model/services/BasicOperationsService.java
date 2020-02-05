package com.consultant.model.services;

import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface BasicOperationsService<T> {
    Set<?> getAll();

    void create(T objectDTO) throws NoMatchException;

    void edit(T objectDTO) throws NoMatchException;

    void delete(Long id) throws NoMatchException;
}
