package com.epam.spsa.feign.dto.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainLinkDto {

    private int id;
    private String url;
    private String type;

}
