package com.epam.spsa.dto.message;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Model that represents message`s sender. Accessible for DELETE and UPDATE requests")
public class WebSocketMessageDto {

    private boolean update;
    private boolean delete;
    private MainMessageDto mainMessageDto;

}
