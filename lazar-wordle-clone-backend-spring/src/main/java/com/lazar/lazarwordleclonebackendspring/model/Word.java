package com.lazar.lazarwordleclonebackendspring.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
	@JsonProperty("letters")
	private List<Letter> letters;
	@JsonProperty("word")
	private String word;
}
