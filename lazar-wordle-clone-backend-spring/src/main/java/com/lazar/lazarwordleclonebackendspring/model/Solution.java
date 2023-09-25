package com.lazar.lazarwordleclonebackendspring.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "solutions")
public class Solution{
	@MongoId
	@JsonProperty("id")
	@NotNull
	private Long _id;
	@NotNull
	@Length(max = 5)
	@JsonProperty("word")
	private String word;
}
