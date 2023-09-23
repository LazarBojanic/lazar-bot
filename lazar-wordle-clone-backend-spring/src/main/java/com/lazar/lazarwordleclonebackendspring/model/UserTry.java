package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_tries")
public class UserTry implements Serializable{
    @JsonProperty("username")
	private String username;
    @JsonProperty("validated_word")
	private Word validated_word;
}
