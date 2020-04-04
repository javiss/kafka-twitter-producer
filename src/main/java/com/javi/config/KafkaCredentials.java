package com.javi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.config")
public class KafkaCredentials {

    @NotEmpty
    private String bootstrapServers;

    @NotEmpty
    private String clientId;
}

