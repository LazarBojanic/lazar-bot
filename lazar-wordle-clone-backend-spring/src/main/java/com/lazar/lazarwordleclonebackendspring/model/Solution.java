package com.lazar.lazarwordleclonebackendspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="solution")
public class Solution{
	@Id
	@JsonProperty("id")
	@NotNull
	private Long id;
	@NotNull
	@Column(name="word", length = 5)
	@JsonProperty("word")
	private String word;
}
