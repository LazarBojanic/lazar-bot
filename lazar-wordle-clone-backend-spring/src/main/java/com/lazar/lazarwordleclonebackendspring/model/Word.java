package com.lazar.lazarwordleclonebackendspring.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Word {
	@JsonProperty("letters")
	private List<Letter> letters;
	@JsonProperty("word")
	private String word;
}
