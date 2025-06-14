package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKeyboard {
    @JsonProperty("username")
	private String username;
	@JsonProperty("letterStatusList")
	private List<LetterStatus> letterStatusList;
}
