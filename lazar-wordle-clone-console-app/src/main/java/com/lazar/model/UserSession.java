package com.lazar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {
    @JsonProperty("username")
    private String username;
    @JsonProperty("word")
    private String word;
    @JsonProperty("status")
    private String status;
    @JsonProperty("remainingTries")
    private Integer remainingTries;
}
