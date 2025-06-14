package com.lazar.lazarwordleclonebackendspring.repository;

import com.lazar.lazarwordleclonebackendspring.model.Guess;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GuessRepository extends MongoRepository<Guess, Long>{
	Optional<Guess> findByWord(String word);
}
