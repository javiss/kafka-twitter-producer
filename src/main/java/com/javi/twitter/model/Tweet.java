package com.javi.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Tweet {

    @JsonProperty("created_at")
    private String created;

    @JsonProperty("text")
    private String text;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("user")
    private User user;
}
