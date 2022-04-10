package com.epam.spsa.dao;

import com.epam.spsa.model.Estimation;

public interface EstimationDao {

    void delete(Estimation event);

    Estimation update(Estimation event);

}
