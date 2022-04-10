package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.UserStatsDao;
import com.epam.spsa.model.UserStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class UserStatsDaoImpl implements UserStatsDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public UserStats save(UserStats userStats) {
        log.info("Saving stats for user id: {}", userStats.getUser().getId());
        entityManager.persist(userStats);
        return userStats;
    }

    @Override
    public UserStats getById(int userId) {
        log.info("Getting stats by id: {}", userId);
        return entityManager.find(UserStats.class, userId);
    }

    @Override
    public List<UserStats> getAll() {
        log.debug("Getting list of UserStats");
        return entityManager
                .createQuery("SELECT us FROM UserStats us", UserStats.class)
                .getResultList();
    }

    @Override
    public List<UserStats> getAllByUserId(int userId) {
        log.debug("Getting list of UserStats for user id: {}", userId);
        return entityManager.createQuery("SELECT us FROM UserStats us WHERE user_id =: id", UserStats.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    public UserStats getLastStats(int userId) {
        log.debug("Getting last UserStats for user id: {}", userId);
        return entityManager.createQuery("SELECT us FROM UserStats us WHERE user_id =: id ORDER BY insert_date DESC", UserStats.class)
                .setParameter("id", userId)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Transactional
    @Override
    public void deleteByUserId(int userId) {
        log.info("Deleting stats where userId={}", userId);
        entityManager.createQuery("DELETE FROM UserStats WHERE user_id =: id")
                .setParameter("id", userId).executeUpdate();
    }

    @Transactional
    @Override
    public UserStats updateStats(UserStats editedStatsDto) {
        log.info("Updating last stats where userId={}", editedStatsDto.getUser().getId());
        UserStats last = getLastStats(editedStatsDto.getUser().getId());
        log.debug("last stats.id: {}", last.getId());

        editedStatsDto.setId(last.getId());

        entityManager.merge(editedStatsDto);
        return editedStatsDto;
    }
}
