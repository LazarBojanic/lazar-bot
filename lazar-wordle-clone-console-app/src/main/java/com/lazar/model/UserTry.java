package com.lazar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTry implements Serializable{
    @JsonProperty("username")
	private String username;
    @JsonProperty("validated_word")
	private Word validatedWord;
}
