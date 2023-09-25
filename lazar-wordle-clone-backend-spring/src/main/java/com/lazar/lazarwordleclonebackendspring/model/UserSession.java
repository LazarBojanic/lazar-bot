package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_sessions")
public class UserSession implements Serializable {
    @JsonProperty("username")
    @MongoId
    private String username;
    @JsonProperty("word")
    private String word;
    @JsonProperty("status")
    private String status;
    @JsonProperty("remaining_tries")
    private Integer remaining_tries;
}
