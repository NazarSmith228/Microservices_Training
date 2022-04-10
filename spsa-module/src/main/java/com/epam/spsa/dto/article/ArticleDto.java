package com.epam.spsa.dto.article;

import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.validation.TagListValue;
import com.epam.spsa.validation.TopicValue;
import com.epam.spsa.validation.validators.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PropertySource("classpath:/ValidationMessages.properties")
public class ArticleDto {

    @NotNull(message = "{blog.authorName.null}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @Pattern(regexp = "^[a-zA-z]{2,20}$", message = "{user.name.regex}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @ApiModelProperty(example = "Carl", notes = "min: 2, max: 20 symbols")
    private String authorName;

    @NotNull(message = "{blog.authorSurname.null}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @Pattern(regexp = "^[a-zA-z]{2,20}$", message = "{user.surname.regex}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @ApiModelProperty(example = "Grimes", notes = "min: 2, max: 20 symbols")
    private String authorSurname;

    @NotNull(message = "{blog.topic.null}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @NotBlank(message = "{blog.topic.blank}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @Size(min = 5, max = 100, message = "{blog.topic.size}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @ApiModelProperty(example = "Here can be your advertisement", notes = "min: 5, max: 100 symbols")
    @TopicValue(groups = {ValidationGroups.ArticleInsert.class})
    private String topic;

    @NotNull(message = "{blog.creationData.null}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @ApiModelProperty(example = "2020-03-17", notes = "year-month-day")
    private LocalDate creationDate;

    @NotNull(message = "{blog.content.null}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @NotBlank(message = "{blog.content.blank}",
            groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    @ApiModelProperty(example = "some text:.", notes = "not null, not blank")
    private String content;

    @TagListValue(groups = {ValidationGroups.ArticleInsert.class, ValidationGroups.ArticleUpdate.class})
    private Set<MainTagDto> tags;

}
