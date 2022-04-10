package com.epam.spsa.dto.criteria;

import com.epam.spsa.model.Gender;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model that represents user. Accessible for POST request")
public class CriteriaUserDto {

    private int id;
    private String name;
    private String photo;
    private String email;
    private String city;
    private Gender gender;
    private CriteriaDto criteriaDto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriteriaUserDto that = (CriteriaUserDto) o;
        return id == that.id
                && Objects.equals(name, that.name)
                && Objects.equals(email, that.email)
                && Objects.equals(city, that.city) &&
                gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, city, gender);
    }

}

