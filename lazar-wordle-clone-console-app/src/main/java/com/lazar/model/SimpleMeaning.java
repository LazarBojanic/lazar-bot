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
public class SimpleMeaning implements Serializable {
    @JsonProperty("partOfSpeech")
    private String partOfSpeech;
    @JsonProperty("definitions")
    private List<String> definitions;
}
