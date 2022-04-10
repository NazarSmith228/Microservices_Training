package com.epam.slsa.dao;

public interface CrudDao<T> {

    T save(T newElement);

    T update(T updatedElement);

    void delete(T deletedElement);

    T getById(int id);

}
