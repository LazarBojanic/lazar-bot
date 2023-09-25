package com.lazar.lazarwordleclonebackendspring.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dictionary_words")
public class DictionaryWord implements Serializable{
    @MongoId
    private String _id;
    private String word;
    private String phonetic;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;
    private License license;
    private List<String> sourceUrls;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Phonetic {
        private String text;
        private String audio;
        private String sourceUrl;
        private License license;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meaning {
        private String partOfSpeech;
        private List<Definition> definitions;
        private List<String> synonyms;
        private List<String> antonyms;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Definition {
        private String definition;
        private List<String> synonyms;
        private List<String> antonyms;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class License {
        private String name;
        private String url;
    }
}
