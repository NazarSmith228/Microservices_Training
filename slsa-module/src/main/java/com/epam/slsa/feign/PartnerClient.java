package com.epam.slsa.feign;

import com.epam.slsa.config.FeignConfig;
import com.epam.slsa.feign.dto.MainUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sport-partner-service-api", url = "${spsa.connect.url}", configuration = FeignConfig.class)
@Component
public interface PartnerClient {

    @GetMapping("api/v1/users/{id}")
    MainUserDto getUserById(@PathVariable int id);

}
