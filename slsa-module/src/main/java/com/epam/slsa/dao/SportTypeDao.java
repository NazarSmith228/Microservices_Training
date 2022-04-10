package com.epam.slsa.dao;

import com.epam.slsa.model.SportType;

import java.util.List;

public interface SportTypeDao extends CrudDao<SportType> {

    List<SportType> getAll();

}
