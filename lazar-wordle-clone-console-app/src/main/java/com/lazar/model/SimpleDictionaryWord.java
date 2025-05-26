package com.lazar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDictionaryWord implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("word")
    private String word;
    @JsonProperty("meanings")
    private List<SimpleMeaning> meanings;
    public SimpleDictionaryWord(DictionaryWord dictionaryWord){
        this.id = dictionaryWord.getId();
        this.word = dictionaryWord.getWord();
        List<SimpleMeaning> simpleMeanings = new ArrayList<>();
        for(DictionaryWord.Meaning meaning : dictionaryWord.getMeanings()){
            List<String> simpleDefinitions = new ArrayList<>();
            for(DictionaryWord.Definition definition : meaning.getDefinitions()){
                simpleDefinitions.add(definition.getDefinition());
            }
            simpleMeanings.add(new SimpleMeaning(meaning.getPartOfSpeech(), simpleDefinitions));
        }
        this.meanings = simpleMeanings;
    }
}
