package com.example.SpringBootStudy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "message")
@Getter
@Setter
public class MessageConfig {

    private Greeting greeting;
    private Introduce introduce;
    private Bye bye;

    @Getter
    @Setter
    public static class Greeting {
        private String pattern;
        private String defaultSentence;
        private List<String> alsoTo;
    }

    @Getter
    @Setter
    public static class Bye {
        private String defaultFarewell;
        private List<String> alsoWho;
    }
}
