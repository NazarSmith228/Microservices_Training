package com.epam.spsa.dao;

import java.util.List;

public interface MainDao<V> {

    V save(V v);

    V getById(int id);

    List<V> getAll();

}
