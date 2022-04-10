package com.epam.spsa.validation.validators;

import com.epam.spsa.dao.TagDao;
import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.model.Tag;
import com.epam.spsa.validation.TagListValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TagListValidator implements ConstraintValidator<TagListValue, Set<MainTagDto>> {

    private final TagDao tagDao;

    @Override
    public boolean isValid(Set<MainTagDto> tagList, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating Tag list : {}", tagList);
        List<Tag> allTags = tagDao.getAll();
        List<String> availableTagNames = allTags.stream().map(tag -> tag.getName().toUpperCase()).collect(Collectors.toList());
        List<String> receivedTagNames = tagList.stream().map(tag -> tag.getName().toUpperCase()).collect(Collectors.toList());
        return availableTagNames.containsAll(receivedTagNames);
    }

}
