package com.lazar.lazarwordleclonebackendspring.repository;

import com.lazar.lazarwordleclonebackendspring.model.Solution;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SolutionRepository extends MongoRepository<Solution, Long>{
	@Aggregation("{ $sample: { size: 1 } }")
    Optional<Solution> findRandomSolution();
    Optional<Solution> findByWord(String word);
}
