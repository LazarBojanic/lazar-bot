package com.lazar.lazarwordleclonebackendspring.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMeaning {
    @JsonProperty("part_of_speech")
    private String part_of_speech;
    @JsonProperty("definitions")
    private List<String> definitions;
}
