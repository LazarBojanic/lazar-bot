package com.lazar.lazarwordleclonebackendspring.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lazar.lazarwordleclonebackendspring.model.*;
import com.lazar.lazarwordleclonebackendspring.repository.GuessRepository;
import com.lazar.lazarwordleclonebackendspring.request.UserTryRequest;
@Service
public class GuessService {
	@Autowired
	private GuessRepository guessRepository;
	
	public Boolean isValidGuess(String guessedWord) {
		Optional<Guess> existingGuess = guessRepository.findByWord(guessedWord);
		if(existingGuess.isPresent()) {
			System.out.println("Checking if guess: " + existingGuess.get() + " is valid.");
			return true;
		}
		else {
			return false;
		}
	}
}
