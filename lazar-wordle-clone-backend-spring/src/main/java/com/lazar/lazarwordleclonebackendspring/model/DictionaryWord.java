package com.lazar.lazarwordleclonebackendspring.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dictionaryWords")
public class DictionaryWord implements Serializable{
    @MongoId
    @JsonProperty("id")
    private String _id;
    @JsonProperty("word")
    private String word;
    @JsonProperty("phonetic")
    private String phonetic;
    @JsonProperty("phonetics")
    private List<Phonetic> phonetics;
    @JsonProperty("meanings")
    private List<Meaning> meanings;
    @JsonProperty("license")
    private License license;
    @JsonProperty("sourceUrls")
    private List<String> sourceUrls;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Phonetic {
        @JsonProperty("text")
        private String text;
        @JsonProperty("audio")
        private String audio;
        @JsonProperty("sourceUrl")
        private String sourceUrl;
        @JsonProperty("license")
        private License license;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meaning {
        @JsonProperty("partOfSpeech")
        private String partOfSpeech;
        @JsonProperty("definitions")
        private List<Definition> definitions;
        @JsonProperty("synonyms")
        private List<String> synonyms;
        @JsonProperty("antonyms")
        private List<String> antonyms;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Definition {
        @JsonProperty("definition")
        private String definition;
        @JsonProperty("synonyms")
        private List<String> synonyms;
        @JsonProperty("antonyms")
        private List<String> antonyms;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class License {
        @JsonProperty("name")
        private String name;
        @JsonProperty("url")
        private String url;
    }
}
