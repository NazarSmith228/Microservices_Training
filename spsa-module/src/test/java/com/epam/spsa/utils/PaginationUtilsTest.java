package com.epam.spsa.utils;

import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.exception.PaginationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class PaginationUtilsTest {

    @Test
    public void incorrectPageNumberTest() {
        List<Object> list = getList();

        assertThrows(PaginationException.class,
                () -> PaginationUtils.paginate(list, -1, 0));
    }

    @Test
    public void incorrectPageSizeTest() {
        List<Object> list = getList();

        assertThrows(PaginationException.class,
                () -> PaginationUtils.paginate(list, -1, 0));
    }

    @Test
    public void pageNumberAndSizeOutOfBoundTest() {
        List<Object> list = getList();

        assertThrows(PaginationException.class,
                () -> PaginationUtils.paginate(list, 5, 10));
    }

    @Test
    public void successfulPaginationTest() {
        List<Object> list = getList();
        int pageNumber = 1;
        int pageSize = 5;

        PaginationDto<Object> paginate = PaginationUtils.paginate(list, pageNumber, pageSize);

        assertEquals(pageSize, paginate.getEntities().size());
        assertEquals(list.size(), paginate.getQuantity());
        assertEquals(list.size() - paginate.getEntities().size(),
                paginate.getEntitiesLeft());
    }

    private static List<Object> getList() {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            objects.add(new Object());
        }
        return objects;
    }

}
