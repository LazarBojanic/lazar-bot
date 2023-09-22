package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sessions")
public class UserSolution implements Serializable {
    @JsonProperty("username")
    @Id
    private String username;
    @JsonProperty("word")
    private String word;
    @JsonProperty("status")
    private String status;
    @JsonProperty("remaining_tries")
    private Integer remaining_tries;
}
