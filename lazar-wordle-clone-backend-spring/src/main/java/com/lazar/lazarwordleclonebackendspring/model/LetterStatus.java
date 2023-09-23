package com.lazar.lazarwordleclonebackendspring.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LetterStatus implements Serializable{
	private String letter;
	private String status;
}
