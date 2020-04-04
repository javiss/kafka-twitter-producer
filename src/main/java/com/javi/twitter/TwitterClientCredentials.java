package com.javi.twitter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Data
@Component
@ConfigurationProperties(prefix = "twitter.config")
public class TwitterClientCredentials {

    @NotEmpty
    String consumerKey;

    @NotEmpty
    String consumerSecret;

    @NotEmpty
    String token;

    @NotEmpty
    String secret;
}
