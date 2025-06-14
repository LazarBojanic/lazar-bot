package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "userSessions")
public class UserSession implements Serializable {
    @MongoId
    @JsonProperty("username")
    private String username;
    @JsonProperty("word")
    private String word;
    @JsonProperty("status")
    private String status;
    @JsonProperty("remainingTries")
    private Integer remainingTries;
}
