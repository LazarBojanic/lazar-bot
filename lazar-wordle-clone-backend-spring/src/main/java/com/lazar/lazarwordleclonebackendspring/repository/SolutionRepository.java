package com.lazar.lazarwordleclonebackendspring.repository;

import com.lazar.lazarwordleclonebackendspring.model.Solution;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long>{
	@Query(value = "SELECT * FROM solution ORDER BY random() LIMIT 1", nativeQuery = true)
    Optional<Solution> findRandomSolution();
    Optional<Solution> findByWord(String word);
}
