package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "userTries")
public class UserTry implements Serializable{
    @JsonProperty("username")
	private String username;
    @JsonProperty("validatedWord")
	private Word validatedWord;
}
