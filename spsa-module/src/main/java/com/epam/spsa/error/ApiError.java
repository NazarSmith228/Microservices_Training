package com.epam.spsa.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@ApiModel(description = "Main error model. Contains all possible API errors")
public class ApiError {

    @ApiModelProperty(example = "400 BAD REQUEST")
    private HttpStatus status;

    @ApiModelProperty(example = "20-01-2001 20:01:01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @ApiModelProperty(example = "Bad request!")
    private String message;

    @ApiModelProperty(dataType = "List", example = "ApiSubError1, ApiSubError2, ApiSubError3")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ApiSubError> subErrors;

}
