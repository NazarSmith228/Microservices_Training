package com.epam.spsa.service;

import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.pagination.PaginationDto;

import java.util.List;

public interface PartnerService {

    PaginationDto<CriteriaUserDto> getSuitablePartner(CriteriaDto criteriaDto, int id, int pageNumber, int pageSize);

    int save(CriteriaDto criteriaDto, int id);

    List<MainCriteriaDto> getCriteriaByUserId(int id);

    List<MainCriteriaDto> getAll();

    void delete(int userId, int criteriaId);

}
