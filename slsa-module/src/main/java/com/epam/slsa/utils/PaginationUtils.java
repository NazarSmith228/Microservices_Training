package com.epam.slsa.utils;

import com.epam.slsa.dto.pagination.PaginationDto;
import com.epam.slsa.error.exception.PaginationException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PaginationUtils {

    public static <T> PaginationDto<T> paginate(List<T> entities, int pageNumber, int pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) {
            throw new PaginationException("Page or size can't be less or equals to 0");
        }

        int start = (pageNumber - 1) * pageSize;
        log.debug("Start position in list of all entities: {}", start);

        int end = start + pageSize;
        log.debug("End position in list of all entities: {}", end);

        int quantity = entities.size();
        int entityLeft = Math.max(quantity - (pageNumber * pageSize), 0);
        log.debug("Size of list of all entities: {}", quantity);
        log.debug("Entities left: {} for page: {}, with size: {}", entityLeft, pageNumber, pageSize);

        if (start < quantity && end > quantity) {
            log.debug("End position is out of bound of list of entities");
            end = quantity;
        }
        if (start > quantity || end > quantity) {
            log.error("Out of bound of list of entities");
            throw new PaginationException("There is no such quantity of entities");
        }
        entities = entities.subList(start, end);
        return PaginationDto.<T>builder()
                .entities(entities)
                .quantity(quantity)
                .entitiesLeft(entityLeft)
                .build();
    }

}

