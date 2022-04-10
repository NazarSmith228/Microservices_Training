package com.epam.spsa.match;

import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.model.Criteria;

import java.util.List;

public interface Matcher {

    List<CriteriaUserDto> getSuitablePartner(Criteria criteria, int pageNumber, int pageSize);

}
