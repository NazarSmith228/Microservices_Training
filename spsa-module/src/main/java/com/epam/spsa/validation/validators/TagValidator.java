package com.epam.spsa.validation.validators;

import com.epam.spsa.dao.TagDao;
import com.epam.spsa.model.Tag;
import com.epam.spsa.validation.TagValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TagValidator implements ConstraintValidator<TagValue, String> {

    private final TagDao tagDao;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name != null) {
            log.info("Validating Tag with value: {}", name);
            List<Tag> allTags = tagDao.getAll();
            return allTags.stream().map(Tag::getName).noneMatch(tag -> tag.equalsIgnoreCase(name));
        }
        return true;
    }

}
