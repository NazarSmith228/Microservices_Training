package com.epam.spsa.dto.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCommentDto {

    @Size(min = 5, max = 500, message = "{blog.comment.size}")
    @ApiModelProperty(example = "(ノಠ益ಠ)ノ彡┻━┻")
    private String content;

}
