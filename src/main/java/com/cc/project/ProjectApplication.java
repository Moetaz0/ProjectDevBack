package com.cc.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.cc.project.Config.AppProperties;
import com.cc.project.Config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({ JwtProperties.class, AppProperties.class })
@EnableScheduling
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}
