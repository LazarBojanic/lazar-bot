package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Letter {
	@JsonProperty("position")
	private Integer position;
	@JsonProperty("letter")
	private String letter;
	@JsonProperty("status")
	private String status;
}
