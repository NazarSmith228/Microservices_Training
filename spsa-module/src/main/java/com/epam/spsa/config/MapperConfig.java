package com.epam.spsa.config;

import com.epam.spsa.converter.*;
import com.epam.spsa.dto.address.DetailedAddressDto;
import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.mapper.*;
import com.epam.spsa.model.*;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MapperConfig {

    private final StringGenderConverter stringGenderConverter;
    private final StringMaturityConverter stringMaturityConverter;
    private final StringActivityTimeConverter activityTimeConverter;
    private final StringLocalDateConverter stringLocalDateConverter;
    private final StringAuthProviderConverter stringAuthProviderConverter;
    private final StringTimeConverter stringTimeConverter;
    private final StringDurationConverter stringDurationConverter;

    private final UserDtoMapper userDtoMapper;
    private final MainCriteriaDtoMapper mainCriteriaDtoMapper;
    private final DetailedAddressDtoMapper detailedAddressDtoMapper;
    private final CriteriaDtoMapper criteriaDtoMapper;
    private final UserPrincipalMapper userPrincipalMapper;
    private final UserStatsMapper userStatsMapper;
    private final TagMapper tagMapper;
    private final FormDtoMapper formDtoMapper;
    private final EventDtoMapper eventDtoMapper;
    private final MainEventDtoMapper mainEventDtoMapper;
    private final MainArticleCommentDtoMapper mainArticleCommentDtoMapper;

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(stringGenderConverter);
        modelMapper.addConverter(stringMaturityConverter);
        modelMapper.addConverter(activityTimeConverter);
        modelMapper.addConverter(stringLocalDateConverter);
        modelMapper.addConverter(stringAuthProviderConverter);
        modelMapper.addConverter(stringTimeConverter);
        modelMapper.addConverter(stringDurationConverter);

        modelMapper
                .createTypeMap(UserDto.class, User.class)
                .addMappings(userDtoMapper);

        modelMapper
                .createTypeMap(Criteria.class, MainCriteriaDto.class)
                .addMappings(mainCriteriaDtoMapper.toDto);

        modelMapper
                .createTypeMap(Address.class, DetailedAddressDto.class)
                .addMappings(detailedAddressDtoMapper);

        modelMapper
                .createTypeMap(CriteriaDto.class, Criteria.class)
                .addMappings(criteriaDtoMapper.toModel);

        modelMapper
                .createTypeMap(User.class, UserPrincipal.class)
                .addMappings(userPrincipalMapper.toPrincipal);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        modelMapper
                .createTypeMap(UserStatsDto.class, UserStats.class)
                .addMappings(userStatsMapper.toStats);
        modelMapper.getConfiguration().setAmbiguityIgnored(false);

        modelMapper
                .createTypeMap(ArticleDto.class, Article.class)
                .addMappings(tagMapper.toTag);

        modelMapper
                .createTypeMap(FormDto.class, Form.class)
                .addMappings(formDtoMapper.toModel);

        modelMapper
                .createTypeMap(Form.class, MainFormDto.class)
                .addMappings(formDtoMapper.toMainDto);

        modelMapper
                .createTypeMap(EventDto.class, Event.class)
                .addMappings(eventDtoMapper.toModel);

        modelMapper
                .createTypeMap(Event.class, MainEventDto.class)
                .addMappings(mainEventDtoMapper);

        modelMapper
                .createTypeMap(ArticleComment.class, MainArticleCommentDto.class)
                .addMappings(mainArticleCommentDtoMapper.toMainDto);


        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }

}