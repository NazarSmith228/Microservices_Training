package com.epam.spsa.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mail-service", url = "${mail-server.connect.url}")
@Component
public interface MailClient {

    @PostMapping(path = "send")
    void sendEmail(@RequestParam String name,
                   @RequestParam String email,
                   @RequestParam String message);

}

