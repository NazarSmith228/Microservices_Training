package com.epam.spsa.mapper;

import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.model.Form;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class FormDtoMapper {

    public PropertyMap<FormDto, Form> toModel = new PropertyMap<FormDto, Form>() {
        @Override
        protected void configure() {
            map(source.getAddress().getLatitude(), destination.getLatitude());
            map(source.getAddress().getLongitude(), destination.getLongitude());
        }
    };

    public PropertyMap<Form, MainFormDto> toMainDto = new PropertyMap<Form, MainFormDto>() {
        @Override
        protected void configure() {
            map(source.getLatitude(), destination.getAddress().getLatitude());
            map(source.getLongitude(), destination.getAddress().getLongitude());
        }
    };

}
