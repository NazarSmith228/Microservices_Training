package com.epam.spsa.dao;

import com.epam.spsa.model.Form;

import java.util.List;

public interface FormDao extends MainDao<Form> {

    void delete(Form form);

    List<Form> getByUserId(int userId);

}
