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
public class DictionaryWord implements Serializable{
    @JsonProperty("_id")
    private String id;
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
    @JsonProperty("source_urls")
    private List<String> sourceUrls;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Phonetic {
        @JsonProperty("text")
        private String text;
        @JsonProperty("audio")
        private String audio;
        @JsonProperty("source_url")
        private String sourceUrl;
        @JsonProperty("license")
        private License license;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meaning {
        @JsonProperty("part_of_speech")
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
