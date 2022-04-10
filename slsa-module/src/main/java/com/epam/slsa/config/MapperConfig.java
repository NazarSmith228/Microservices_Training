package com.epam.slsa.config;

import com.epam.slsa.converter.*;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.mapper.CoachDtoMapper;
import com.epam.slsa.mapper.CommentDtoMapper;
import com.epam.slsa.mapper.DetailedAddressDtoMapper;
import com.epam.slsa.mapper.LocationDtoMapper;
import com.epam.slsa.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MapperConfig {

    private final StringGenderConverter stringGenderConverter;
    private final StringDayConverter stringDayConverter;
    private final StringLocalTimeConverter stringLocalTimeConverter;
    private final LocationTypeConverter locationTypeConverter;
    private final StringPlacingConverter stringPlacingConverter;

    private final CoachDtoMapper coachDtoMapper;
    private final LocationDtoMapper locationDtoMapper;
    private final DetailedAddressDtoMapper detailedAddressDtoMapper;
    private final CommentDtoMapper commentDtoMapper;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(stringDayConverter);
        modelMapper.addConverter(stringLocalTimeConverter);
        modelMapper.addConverter(stringGenderConverter);
        modelMapper.addConverter(stringPlacingConverter);

        modelMapper
                .createTypeMap(LocationDto.class, Location.class)
                .addMappings(locationDtoMapper);

        modelMapper
                .createTypeMap(CoachDto.class, Coach.class)
                .addMappings(coachDtoMapper);

        modelMapper
                .createTypeMap(Address.class, DetailedAddressDto.class)
                .addMappings(detailedAddressDtoMapper);

        modelMapper
                .createTypeMap(Comment.class, MainCommentDto.class)
                .addMappings(commentDtoMapper.toDto);

        modelMapper
                .createTypeMap(LocationTypeDto.class, LocationType.class)
                .setConverter(locationTypeConverter);

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }

}
