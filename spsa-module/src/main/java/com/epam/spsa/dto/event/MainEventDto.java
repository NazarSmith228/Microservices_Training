package com.epam.spsa.dto.event;

import com.epam.spsa.model.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model that represent calendar event")
public class MainEventDto {

    private int id;
    private String date;
    private String time;
    private String description;
    private int location_id;
    private int owner_id;
    private List<Integer> userIdList;

}
