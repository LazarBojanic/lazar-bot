package com.lazar.lazarwordleclonebackendspring.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.lazar.lazarwordleclonebackendspring.model.*;
import com.lazar.lazarwordleclonebackendspring.repository.SolutionRepository;
import com.lazar.lazarwordleclonebackendspring.request.UserTryRequest;
import com.lazar.lazarwordleclonebackendspring.response.UserTryResponse;
import com.lazar.lazarwordleclonebackendspring.util.Util;
@Service
public class SolutionService {
	@Autowired
	private GuessService guessService;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private UserSessionService userSessionService;
	@Autowired
	private UserTryService userTryService;

	public UserTryResponse guess(UserTryRequest userTryRequest) {
		String username = userTryRequest.getUsername();
		String guessedWord = userTryRequest.getWord().toUpperCase();
		if(guessedWord.length() != 5){
			return new UserTryResponse(false, "solutionNotValid", new Word(), guessedWord);
		}
		String solutionWord = userSessionService.getCurrentSolution(username).getWord();
		Word validatedWord = getValidatedWord(guessedWord, solutionWord);
		if(userSessionService.getStatus(username).equalsIgnoreCase("gameOver") || userSessionService.getStatus(username).equalsIgnoreCase("solved")){
			return new UserTryResponse(false, "gameEnded", validatedWord, guessedWord);
		}
		if(guessedWord.equals(solutionWord)){
			userTryService.addTryForUser(username, validatedWord);
			userSessionService.decrementRemainingTries(username);
			userSessionService.setStatus(username, "solved");
			System.out.println("Solution: " + guessedWord + ", correct.");
			return new UserTryResponse(true, "solutionCorrect", validatedWord, guessedWord);
		}
		else {
			if(guessService.isValidGuess(guessedWord)) {
				userTryService.addTryForUser(username, validatedWord);
				userSessionService.decrementRemainingTries(username);
				if(userSessionService.getRemainingTries(username) <= 0){
					userSessionService.setStatus(username, "gameOver");
				}
				System.out.println("Solution: " + guessedWord + ", incorrect.");
				return new UserTryResponse(true, "solutionIncorrect", validatedWord, guessedWord);
			}
			else {
				System.out.println("Solution: " + guessedWord + ", not valid.");
				return new UserTryResponse(false, "solutionNotValid", validatedWord, guessedWord);
			}
		}
	}
	public Word getValidatedWord(String guessedWord, String solutionWord) {
		List<Letter> letters = new ArrayList<>();
		Map<Character, Integer> count = new HashMap<>();
		for (int i = 0; i < guessedWord.length(); i++) {
			char guessedLetter = guessedWord.charAt(i);
			char solutionLetter = solutionWord.charAt(i);
			Letter currentGuessedLetter = new Letter(i, String.valueOf(guessedLetter), Util.RED);
			Letter currentSolutionLetter = new Letter(i, String.valueOf(solutionLetter), Util.RED);
			if (guessedLetter == solutionLetter) {
				currentGuessedLetter.setStatus(Util.GREEN);
			}
			else {
				count.put(solutionLetter, count.getOrDefault(solutionLetter, 0) + 1);
			}
			letters.add(currentGuessedLetter);
		}
		for (int i = 0; i < guessedWord.length(); i++) {
			char guessedLetter = guessedWord.charAt(i);
			char solutionLetter = solutionWord.charAt(i);
			if (guessedLetter == solutionLetter || count.getOrDefault(guessedLetter, 0) == 0) {
				continue;
			}
			count.put(guessedLetter, count.get(guessedLetter) - 1);
			letters.get(i).setStatus(Util.YELLOW);
		}
		letters.sort(Comparator.comparingInt(Letter::getPosition));
		return new Word(letters, guessedWord);
	}
	public Solution getSolutionByWord(String word) {
		Optional<Solution> solutionOptional = solutionRepository.findByWord(word);
        return solutionOptional.orElseGet(Solution::new);
    }
	public String getRandomSolutionWord() {
		Optional<Solution> solutionOptional = solutionRepository.findRandomSolution();
		if(solutionOptional.isPresent()){
			return solutionOptional.get().getWord();
		}
		return "";
	}

}
