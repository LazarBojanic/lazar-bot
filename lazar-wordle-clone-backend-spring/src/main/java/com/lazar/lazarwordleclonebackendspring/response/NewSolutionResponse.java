package com.lazar.lazarwordleclonebackendspring.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewSolutionResponse {
    @JsonProperty("status")
    private String status;
}
