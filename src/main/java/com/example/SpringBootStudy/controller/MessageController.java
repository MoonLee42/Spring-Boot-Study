package com.example.SpringBootStudy.controller;

import com.example.SpringBootStudy.config.InvitationConfig;
import com.example.SpringBootStudy.config.MessageConfig;
import com.example.SpringBootStudy.config.SpringConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageConfig messageConfig;
    private final InvitationConfig invitationConfig;
    private final SpringConfig springConfig;

    @GetMapping("/greet")
    public String greet(@RequestParam(name = "to", required = false) String target) {
        String greetingPattern = messageConfig.getGreeting().getPattern();
        if (Objects.nonNull(target) && Objects.nonNull(greetingPattern)) {
            StringBuilder result = new StringBuilder();
            result.append(String.format(greetingPattern, target));
            List<String> alsoTo = messageConfig.getGreeting().getAlsoTo();
            if (Objects.nonNull(alsoTo) && !alsoTo.isEmpty()) {
                for (String addition : alsoTo) {
                    result.append(", ")
                          .append(addition);
                }
            }
            return result.append('.').toString();
        } else {
            return messageConfig.getGreeting().getDefaultSentence();
        }
    }

    @GetMapping("/introduce")
    public String introduce() {
        List<Map<String, String>> celestialObjects = messageConfig.getIntroduce().getCelestialObjects();
        if (Objects.nonNull(celestialObjects) && !celestialObjects.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append('[');
            for (Map<String, String> celestialObject : celestialObjects) {
                if (result.length() > 1) {
                    result.append(", ");
                }
                String name = celestialObject.get("name");
                String description = celestialObject.get("description");
                result.append(String.format("{\"name\":\"%s\", \"description\":\"%s\"}", name, description));
            }
            result.append(']');
            return result.toString();
        }
        return "{\"error\":\"introduce not found\"}";
    }

    @GetMapping("/invitation")
    public String invitation() {
        Date date = invitationConfig.getDate();
        String place = invitationConfig.getPlace();
        if (Objects.nonNull(date) && Objects.nonNull(place)) {
            StringBuilder result = new StringBuilder();
            result.append('{')
                  .append(String.format("\"date\":\"%s\"", date)).append(", ")
                  .append(String.format("\"place\":\"%s\"", place))
                  .append('}');
            return result.toString();
        }
        return "{\"error\":\"invitation not found\"}";
    }

    @GetMapping("/who")
    public String askWho() {
        String applicationName = springConfig.getApplicationName();
        if (Objects.nonNull(applicationName)) {
            return applicationName;
        }
        return "appName not found";
    }

    @GetMapping("/javaHome")
    public String javaHome() {
        String javaHome = springConfig.getJavaHome();
        if (StringUtils.isNotBlank(javaHome)) {
            return javaHome;
        }
        return "maven home not found";
    }

    @GetMapping("/bye")
    public String bye(@RequestParam(name = "who", required = false) String target) {
        if (Objects.nonNull(target) ) {
            StringBuilder result = new StringBuilder();
            result.append(target)
                    .append(",");
            List<String> alsoWho = messageConfig.getBye().getAlsoWho();
            if (Objects.nonNull(alsoWho) && !alsoWho.isEmpty()) {
                for (String addition : alsoWho) {
                    result.append(" ")
                            .append(addition);
                }
            }
            return result.append('!').toString();
        } else {
            return messageConfig.getBye().getDefaultFarewell();
        }
    }

}
