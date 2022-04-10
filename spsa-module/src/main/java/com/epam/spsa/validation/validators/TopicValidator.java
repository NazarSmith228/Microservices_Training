package com.epam.spsa.validation.validators;

import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.model.Article;
import com.epam.spsa.validation.TopicValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
@Slf4j
public class TopicValidator implements ConstraintValidator<TopicValue, String> {

    private final ArticleDao articleDao;

    @Override
    public boolean isValid(String topic, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(constraintValidatorContext);
        try {
            log.info("Validating Article by topic: {}", topic);
            Article article = articleDao.getArticleByTopic(topic);
            return false;
        } catch (NoResultException | EmptyResultDataAccessException e) {
            return true;
        }
    }

}
