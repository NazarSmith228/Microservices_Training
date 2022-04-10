package com.epam.spsa.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@AllArgsConstructor
public class ApiValidationError implements ApiSubError {

    private String field;
    private Object rejectedValue;
    private String message;

}
