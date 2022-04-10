package com.epam.spsa.mapper;

import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.model.UserStats;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserStatsMapper {

    public PropertyMap<UserStatsDto, UserStats> toStats = new PropertyMap<UserStatsDto, UserStats>() {
        @Override
        protected void configure() {
            skip().setId(0);
            skip().setUser(null);
            skip().setInsertionDate(null);
        }
    };

}
