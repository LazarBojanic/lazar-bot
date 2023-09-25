package com.lazar.lazarwordleclonebackendspring.model;

import java.io.Serializable;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LetterStatus implements Serializable{
	private String letter;
	private String status;
}
