package com.lazar.lazarwordleclonebackendspring.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lazar.lazarwordleclonebackendspring.model.DictionaryWord.Definition;
import com.lazar.lazarwordleclonebackendspring.model.DictionaryWord.Meaning;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDictionaryWord {
    @JsonProperty("id")
    private String id;
    @JsonProperty("word")
    private String word;
    @JsonProperty("meanings")
    private List<SimpleMeaning> meanings;
    public SimpleDictionaryWord(DictionaryWord dictionaryWord){
        this.id = dictionaryWord.get_id();
        this.word = dictionaryWord.getWord();
        List<SimpleMeaning> simpleMeanings = new ArrayList<>();
        for(Meaning meaning : dictionaryWord.getMeanings()){
            List<String> simpleDefinitions = new ArrayList<>();
            for(Definition definition : meaning.getDefinitions()){
                simpleDefinitions.add(definition.getDefinition());
            }
            simpleMeanings.add(new SimpleMeaning(meaning.getPartOfSpeech(), simpleDefinitions));
        }
        this.meanings = simpleMeanings;
    }
}
