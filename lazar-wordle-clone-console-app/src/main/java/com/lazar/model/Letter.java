package com.lazar.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Letter implements Serializable {
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("letter")
    private String letter;
    @JsonProperty("status")
    private String status;
}
