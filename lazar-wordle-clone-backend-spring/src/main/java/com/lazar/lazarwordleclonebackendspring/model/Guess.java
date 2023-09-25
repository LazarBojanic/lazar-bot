package com.lazar.lazarwordleclonebackendspring.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "guesses")
public class Guess{
	@MongoId
	@JsonProperty("id")
	@NotNull
	private Long _id;
	@NotNull
	@Length(max = 5)
	@JsonProperty("word")
	private String word;
}
