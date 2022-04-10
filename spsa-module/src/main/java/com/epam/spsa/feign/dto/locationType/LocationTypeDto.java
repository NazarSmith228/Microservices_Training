package com.epam.spsa.feign.dto.locationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationTypeDto {

    private int id;
    private String name;
    private String placing;

}
