package ru.corthos.call;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public SignalingService signalingService() {
        return new SignalingService(
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()
        );
    }

}
