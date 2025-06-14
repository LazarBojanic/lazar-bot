package com.lazar.lazarwordleclonebackendspring.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMeaning {
    @JsonProperty("partOfSpeech")
    private String partOfSpeech;
    @JsonProperty("definitions")
    private List<String> definitions;
}
