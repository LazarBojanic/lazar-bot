package com.lazar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKeyboard implements Serializable {
    @JsonProperty("username")
	private String username;
	@JsonProperty("letter_status_list")
	private List<LetterStatus> letterStatusList;
}
