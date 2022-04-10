package com.epam.slsa.dao;

import com.epam.slsa.model.Link;

public interface LinkDao extends CrudDao<Link> {

    Link getByUrl(String url);

}
