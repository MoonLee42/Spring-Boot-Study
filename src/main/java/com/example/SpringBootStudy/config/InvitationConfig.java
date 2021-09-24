package com.example.SpringBootStudy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@ConfigurationProperties(prefix = "message.invitation")
@Getter
@Setter
public class InvitationConfig {
    private Date date;
    private String place;
}
