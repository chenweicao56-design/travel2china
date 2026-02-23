package com.gklx.travel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gklx.travel.mapper")
public class Travel2chinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Travel2chinaApplication.class, args);
    }

}
