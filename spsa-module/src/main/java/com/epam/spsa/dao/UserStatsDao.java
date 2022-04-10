package com.epam.spsa.dao;

import com.epam.spsa.model.UserStats;

import java.util.List;

public interface UserStatsDao extends MainDao<UserStats> {

    List<UserStats> getAllByUserId(int id);

    UserStats getLastStats(int userId);

    void deleteByUserId(int userId);

    UserStats updateStats(UserStats editedStatsDto);

}
