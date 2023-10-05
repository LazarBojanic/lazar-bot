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

	public UserTryResponse guessSolution(UserTryRequest userTryRequest) {
		String username = userTryRequest.getUsername();
		String guessedWord = userTryRequest.getWord().toUpperCase();
		if(guessedWord.length() != 5){
			return new UserTryResponse(false, "solution_not_valid", new Word(), guessedWord);
		}
		String solutionWord = userSessionService.getCurrentSolutionForUser(username).getWord();
		Word validatedWord = getValidatedWord(guessedWord, solutionWord);

		if(userSessionService.getStatusForUser(username).equalsIgnoreCase("game_over") || userSessionService.getStatusForUser(username).equalsIgnoreCase("solved")){
			return new UserTryResponse(false, "game_ended", validatedWord, guessedWord);
		}
		if(guessedWord.equals(solutionWord)){
			userTryService.addTryForUser(username, validatedWord);
			userSessionService.decrementRemainingTriesForUser(username);
			userSessionService.setStatusForUser(username, "solved");
			System.out.println("Solution: " + guessedWord + ", correct.");
			return new UserTryResponse(true, "solution_correct", validatedWord, guessedWord);
		}
		else {
			if(guessService.isValidGuess(guessedWord)) {
				userTryService.addTryForUser(username, validatedWord);
				userSessionService.decrementRemainingTriesForUser(username);
				if(userSessionService.getRemainingTriesForUser(username) <= 0){
					userSessionService.setStatusForUser(username, "game_over");
				}
				System.out.println("Solution: " + guessedWord + ", incorrect.");
				return new UserTryResponse(true, "solution_incorrect", validatedWord, guessedWord);
			}
			else {
				System.out.println("Solution: " + guessedWord + ", not valid.");
				return new UserTryResponse(false, "solution_not_valid", validatedWord, guessedWord);
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
			} else {
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
		return solutionRepository.findByWord(word).get();
	}

	public String getRandomSolutionWord() {
		return solutionRepository.findRandomSolution().get().getWord();
	}

}
