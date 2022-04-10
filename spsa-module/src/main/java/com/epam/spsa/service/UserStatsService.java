package com.epam.spsa.service;

import com.epam.spsa.dto.user.CommonUserStatDto;
import com.epam.spsa.dto.user.MainCommonUserStatDto;
import com.epam.spsa.dto.user.MainUserStatsDto;
import com.epam.spsa.dto.user.UserStatsDto;

import java.util.List;

public interface UserStatsService {

    MainUserStatsDto saveUserStats(UserStatsDto userStat, int userId);

    List<MainUserStatsDto> getAllUserStats(int id);

    MainUserStatsDto getLastUserStats(int userId);

    MainUserStatsDto updateLastUserStats(UserStatsDto statsDto, int userId);

    void deleteUserStats(int userId);

    MainCommonUserStatDto getCommonStat(CommonUserStatDto statDto, int userId);

}
