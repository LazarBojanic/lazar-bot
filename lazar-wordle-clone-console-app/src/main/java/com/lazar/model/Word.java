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
public class Word implements Serializable {
    @JsonProperty("letters")
    private List<Letter> letters;
    @JsonProperty("word")
    private String word;
}
