package com.lazar.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lazar.model.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTryResponse {
    @JsonProperty("solution_is_valid")
    private Boolean solutionIsValid;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("validated_word")
    private Word validatedWord;
    @JsonProperty("word")
    private String word;
}
