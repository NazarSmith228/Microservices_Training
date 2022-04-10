package com.epam.slsa.dto.pagination;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationDto<T> {

    private List<T> entities;
    private long quantity;
    private long entitiesLeft;

}
