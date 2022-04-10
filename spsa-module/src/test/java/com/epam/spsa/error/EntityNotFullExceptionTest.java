package com.epam.spsa.error;

import com.epam.spsa.controller.PartnerController;
import com.epam.spsa.controller.builder.CriteriaDtoBuilder;
import com.epam.spsa.controller.builder.SportTypeDtoBuilder;
import com.epam.spsa.dao.*;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.error.exception.EntityNotFullException;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.model.SportType;
import com.epam.spsa.service.PartnerService;
import com.epam.spsa.validation.validators.SportTypeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PartnerController.class)
@ComponentScan(basePackageClasses = {SportTypeDao.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {ArticleDao.class, TagDao.class, AddressDao.class, UserDao.class, EstimationDao.class,
                        CriteriaDao.class, RoleDao.class, FormDao.class, ArticleCommentDao.class,
                        ChatDao.class, MessageDao.class, EventDao.class, UserStatsDao.class}))
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class EntityNotFullExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PartnerService partnerService;

    @MockBean
    private SlsaClient slsaClient;

    @Value("${criteria.user.exception.notfull}")
    private String notFullUserException;

    @Test
    public void matchNotFullUserTest() throws Exception {
        CriteriaDto criteriaDto = CriteriaDtoBuilder.getCriteriaDto();
        SportType sportType1 = SportType.builder().id(1).name("Running").build();
        SportType sportType2 = SportType.builder().id(2).name("Swimmming").build();
        List<SportType> sportTypeList2 = new ArrayList<>(Arrays.asList(sportType1, sportType2));
        int userId = 1;
        String pageNumber = "1";
        String pageSize = "2";

        when(sportTypeDao.getAll()).thenReturn(sportTypeList2);
        when(modelMapper.map(criteriaDto.getSportType(), SportType.class)).thenReturn(SportTypeDtoBuilder.getSportType());
        when(partnerService.getSuitablePartner(Mockito.any(CriteriaDto.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new EntityNotFullException(notFullUserException + userId));
        new SportTypeValidator(sportTypeDao, modelMapper);

        mockMvc.perform(post("/api/v1/users/{id}/partners", userId)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteriaDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(notFullUserException + userId));
    }

}
