package com.epam.spsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class SpsaApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpsaApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}