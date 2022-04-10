package com.epam.spsa.error;

import com.epam.spsa.controller.TagController;
import com.epam.spsa.error.exception.PaginationException;
import com.epam.spsa.service.TagService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TagController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class PaginationExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Value("${pagination.exception}")
    private String paginationException;

    @Test
    public void matchNotFullUserTest() throws Exception {
        String name = "Yoga";
        String pageNumber = "1";
        String pageSize = "-2";

        when(tagService.getAllArticlesByTagName(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new PaginationException(paginationException));

        mockMvc.perform(get("/api/v1/tags/articles")
                .param("name", name)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
