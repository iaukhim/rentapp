package org.example.rentapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RentappApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentappApplication.class, args);
    }

}
