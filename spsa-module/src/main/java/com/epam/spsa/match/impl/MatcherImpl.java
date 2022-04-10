package com.epam.spsa.match.impl;


import com.epam.spsa.dao.CriteriaDao;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.match.CriteriaFilter;
import com.epam.spsa.match.Matcher;
import com.epam.spsa.model.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatcherImpl implements Matcher {

    private final CriteriaDao criteriaDao;

    private final CriteriaFilter criteriaFilter;

    @Override
    public List<CriteriaUserDto> getSuitablePartner(Criteria criteria, int pageNumber, int pageSize) {
        log.info("Getting suitable partner by Criteria: {}", criteria);
        List<Criteria> criteriaList = criteriaDao.getAll();

        criteriaList = criteriaFilter.removeMainUserFromList(criteriaList, criteria.getUser().getId());
        log.debug("Main User removed");

        if (criteria.getActivityTime() != null) {
            criteriaList = criteriaFilter.getFilteredUsersByDayTime(criteriaList, criteria.getActivityTime().getDayPart());
            log.debug("Filtered by Activity time: {}", criteria.getActivityTime());
        }

        if (criteria.getGender() != null) {
            criteriaList = criteriaFilter.getFilteredUsersByGender(criteriaList, criteria);
            log.debug("Filtered by Gender: {}", criteria.getGender());
        }

        criteriaList = criteriaFilter.getFilteredUsersByLocation(criteriaList, criteria.getUser().getAddress());
        log.debug("Filtered by User Address: {}", criteria.getUser().getAddress());

        if (criteria.getMaturity() != null) {
            criteriaList = criteriaFilter.getFilteredUsersByMaturity(criteriaList, criteria.getMaturity().getMaturity());
            log.debug("Filtered by Maturity: {}", criteria.getMaturity());
        }

        if (criteria.getSportType() != null) {
            criteriaList = criteriaFilter.getFilteredUsersBySportType(criteriaList, criteria);
            log.debug("Filtered by Sport type: {}", criteria.getSportType());
        }

        return criteriaFilter.getUsers(criteriaList, pageNumber, pageSize);
    }

}
