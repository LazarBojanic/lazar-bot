package com.lazar.lazarwordleclonebackendspring.repository;

import com.lazar.lazarwordleclonebackendspring.model.Guess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface GuessRepository extends JpaRepository<Guess, Long>{
	public Optional<Guess> findByWord(String word);
}
