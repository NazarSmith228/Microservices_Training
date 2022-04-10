package com.epam.spsa.service.impl;

import com.epam.spsa.dao.CriteriaDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.EntityNotFullException;
import com.epam.spsa.googleapi.GeocodingApi;
import com.epam.spsa.match.Matcher;
import com.epam.spsa.model.Criteria;
import com.epam.spsa.model.User;
import com.epam.spsa.service.PartnerService;
import com.epam.spsa.utils.PaginationUtils;
import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerServiceImpl implements PartnerService {

    private final Matcher matcher;

    private final CriteriaDao criteriaDao;

    private final ModelMapper modelMapper;

    private final UserDao userDao;

    private final GeocodingApi geocodingApi;

    @Value("${user.exception.notfound}")
    private String userExceptionMessage;

    @Value("${criteria.user.exception.empty}")
    private String noCriteriaFromUserMessage;

    @Value("${criteria.user.exception.nomatches}")
    private String noUsersMatchException;

    @Value("${criteria.user.exception.notfull}")
    private String notFullUserException;

    @Value("${criteria.exception.found}")
    private String criteriaAlreadyExistsException;

    @Override
    public PaginationDto<CriteriaUserDto> getSuitablePartner(CriteriaDto criteriaDto, int id, int pageNumber, int pageSize) {
        log.info("Getting list of MainUserDto by Criteria");
        log.debug("\tCriteriaDto: maturity={}", criteriaDto.getMaturity());
        log.debug("\tCriteriaDto: gender={}", criteriaDto.getGender());
        log.debug("\tCriteriaDto: sport type={}", criteriaDto.getSportType());
        Criteria criteria = modelMapper.map(criteriaDto, Criteria.class);
        User mainUser = getUserById(id);
        if (mainUser.getAddress() == null || mainUser.getName() == null || mainUser.getEmail() == null) {
            throw new EntityNotFullException(notFullUserException + id);
        }
        criteria.setUser(mainUser);

        log.debug("\tList of suitable partners");
        List<CriteriaUserDto> criteriaUserDtoList = matcher.getSuitablePartner(criteria, pageNumber, pageSize);

        PaginationDto<CriteriaUserDto> paginate = PaginationUtils.paginate(criteriaUserDtoList, pageNumber, pageSize);

        log.debug("\tAdding city to each user");
        criteriaUserDtoList = paginate.getEntities();
        criteriaUserDtoList.forEach(user -> {
            try {
                user.setCity(getCity(user.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (criteriaUserDtoList.size() == 0) {
            throw new EntityNotFoundException(noUsersMatchException);
        }

        return PaginationDto.<CriteriaUserDto>builder()
                .entities(criteriaUserDtoList)
                .quantity(paginate.getQuantity())
                .entitiesLeft(paginate.getEntitiesLeft())
                .build();
    }

    private List<CriteriaDto> getCriteriaList(int userId) {
        return getCriteriaByUserId(userId).stream()
                .map(criteriaMain -> modelMapper.map(criteriaMain, CriteriaDto.class))
                .collect(Collectors.toList());
    }

    private String getCity(int id) throws InterruptedException, ApiException, IOException {
        double latitude = userDao.getById(id).getAddress().getLatitude();
        double longitude = userDao.getById(id).getAddress().getLongitude();
        log.debug("\tAddress: latitude={}", latitude);
        log.debug("\tAddress: longitude={}", longitude);
        return geocodingApi.getCity(latitude, longitude);
    }

    @Override
    public int save(CriteriaDto criteriaDto, int id) {
        log.info("Saving Criteria");
        log.debug("\tCriteriaDto: maturity={}", criteriaDto.getMaturity());
        log.debug("\tCriteriaDto: gender={}", criteriaDto.getGender());
        log.debug("\tCriteriaDto: sport type={}", criteriaDto.getSportType());

        Criteria criteria = modelMapper.map(criteriaDto, Criteria.class);

        List<CriteriaDto> criteriaDtoList = getCriteriaByUserId(id).stream()
                .map(criteriaMain -> modelMapper.map(criteriaMain, CriteriaDto.class))
                .collect(Collectors.toList());

        if (criteriaDtoList.stream().anyMatch(criteriaDto::equals)) {
            throw new EntityAlreadyExistsException(criteriaAlreadyExistsException);
        }

        User mainUser = getUserById(id);
        criteria.setUser(mainUser);

        criteriaDao.save(criteria);
        return criteria.getId();
    }

    @Override
    public List<MainCriteriaDto> getAll() {
        log.info("Getting List of MainCriteriaDto");
        return criteriaDao.getAll().stream()
                .map(criteria -> modelMapper.map(criteria, MainCriteriaDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int userId, int criteriaId) {
        log.info("Deleting Criteria by id: {}", criteriaId);
        Criteria criteria = criteriaDao.getById(criteriaId);
        if (criteria.getUser().getId() == userId) {
            criteriaDao.delete(criteriaDao.getById(criteriaId));
        } else {
            throw new EntityNotFoundException("You can not delete this criteria");
        }
    }

    @Override
    public List<MainCriteriaDto> getCriteriaByUserId(int id) {
        log.info("Getting all Criteria by User id: {}", id);
        log.info("Check if user with id {} exists", id);
        getUserById(id);
        List<MainCriteriaDto> criteriaDtoList = criteriaDao.getByUserId(id).stream()
                .map(criteria -> modelMapper.map(criteria, MainCriteriaDto.class))
                .collect(Collectors.toList());
        log.info("Check if user with id {} has any criteria", id);
        return criteriaDtoList;
    }

    private User getUserById(int id) {
        User user = userDao.getById(id);
        log.debug("Getting MainUser by id: {}", id);
        if (user == null) {
            log.error("Main User wasn't found. id: {}", id);
            throw new EntityNotFoundException(userExceptionMessage + id);
        }
        log.debug("Main user from Dto: {}", user);
        return user;
    }

}