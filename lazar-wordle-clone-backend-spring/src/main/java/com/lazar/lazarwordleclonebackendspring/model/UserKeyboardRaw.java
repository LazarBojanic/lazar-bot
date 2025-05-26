package com.lazar.lazarwordleclonebackendspring.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKeyboardRaw{
    @JsonProperty("username")
	private String username;
	@JsonProperty("letter_status_list")
	private List<LetterStatus> letterStatusList;
}
