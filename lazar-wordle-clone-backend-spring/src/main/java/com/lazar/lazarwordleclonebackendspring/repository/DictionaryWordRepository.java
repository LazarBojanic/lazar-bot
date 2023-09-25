package com.lazar.lazarwordleclonebackendspring.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lazar.lazarwordleclonebackendspring.model.DictionaryWord;

@Repository
public interface DictionaryWordRepository extends MongoRepository<DictionaryWord, String>{
	public Optional<DictionaryWord> findByWord(String word);
}
