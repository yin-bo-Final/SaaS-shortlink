package com.yin_bo_.shortlink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yin_bo_.shortlink.project.dao.mapper")
@SpringBootApplication
public class ShortLinkProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortLinkProjectApplication.class, args);
    }
}
