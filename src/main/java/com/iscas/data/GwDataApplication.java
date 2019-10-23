package com.iscas.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class GwDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(GwDataApplication.class, args);
    }

}
