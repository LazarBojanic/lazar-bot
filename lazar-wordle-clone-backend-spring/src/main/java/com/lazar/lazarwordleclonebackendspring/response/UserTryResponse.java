package com.lazar.lazarwordleclonebackendspring.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lazar.lazarwordleclonebackendspring.model.Word;

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
public class UserTryResponse {
	@JsonProperty("solutionIsValid")
	private Boolean solutionIsValid;
	@JsonProperty("reason")
	private String reason;
	@JsonProperty("validatedWord")
	private Word validatedWord;
	@JsonProperty("word")
	private String word;
}
