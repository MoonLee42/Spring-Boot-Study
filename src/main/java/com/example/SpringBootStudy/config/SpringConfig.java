package com.example.SpringBootStudy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SpringConfig {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${JAVA_HOME}")
    private String javaHome;
}
