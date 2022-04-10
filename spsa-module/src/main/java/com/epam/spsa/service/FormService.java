package com.epam.spsa.service;

import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.dto.pagination.PaginationDto;

public interface FormService {

    int save(FormDto newForm);

    MainFormDto getById(int id);

    PaginationDto<MainFormDto> getAll(int pageNumber, int pageSize);

    void approve(int id);

    void delete(int id);

}
