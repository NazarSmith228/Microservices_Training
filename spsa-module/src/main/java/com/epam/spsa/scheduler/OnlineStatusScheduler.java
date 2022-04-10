package com.epam.spsa.scheduler;

import com.epam.spsa.dao.UserDao;
import com.epam.spsa.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OnlineStatusScheduler {

    private final UserDao userDao;

    @Scheduled(fixedDelay = 180000)
    public void updateOfflineStatus() {
        List<User> all = userDao.getAll();
        for (User user : all) {
            if (user.isOnline()) {
                if (user.getLastSeen().until(LocalDateTime.now(), ChronoUnit.MINUTES) > 5) {
                    user.setOnline(false);
                    userDao.update(user);
                }
            }
        }
    }

}
