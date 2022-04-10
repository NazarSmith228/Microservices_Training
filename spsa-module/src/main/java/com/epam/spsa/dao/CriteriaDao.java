package com.epam.spsa.dao;

import com.epam.spsa.model.Criteria;

import java.util.List;

public interface CriteriaDao extends MainDao<Criteria> {

    List<Criteria> getByUserId(int id);

    void delete(Criteria criteria);

}
