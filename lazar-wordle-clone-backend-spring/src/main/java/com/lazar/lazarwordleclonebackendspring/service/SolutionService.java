package com.lazar.lazarwordleclonebackendspring.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.lazar.lazarwordleclonebackendspring.model.*;
import com.lazar.lazarwordleclonebackendspring.repository.SolutionRepository;
import com.lazar.lazarwordleclonebackendspring.request.UserTryRequest;
import com.lazar.lazarwordleclonebackendspring.response.UserTryResponse;
@Service
public class SolutionService {
	@Autowired
	private GuessService guessService;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private UserSolutionService userSolutionService;

	public UserTryResponse checkSolution(UserTryRequest userTry) {
		String guessedWord = userTry.getWord().toUpperCase();
		String solutionWord = userSolutionService.getCurrentSolutionForUser(userTry.getUsername()).getWord();
		Word validatedWord = getValidatedWord(guessedWord, solutionWord);
		if(guessedWord.equals(solutionWord)){
			userSolutionService.decrementRemainingTriesForUser(userTry.getUsername());
			userSolutionService.setStatusForUser(userTry.getUsername(), "solved");
			System.out.println("Solution: " + guessedWord + ", correct.");
			return new UserTryResponse(true, "solution_correct", validatedWord, guessedWord);
		}
		else {
			if(guessService.isValidGuess(guessedWord)) {
				System.out.println("Solution: " + guessedWord + ", incorrect.");
				return new UserTryResponse(false, "solution_incorrect", validatedWord, guessedWord);
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

			Letter currentGuessedLetter = new Letter(i, String.valueOf(guessedLetter), LetterStatus.GREY);
			Letter currentSolutionLetter = new Letter(i, String.valueOf(solutionLetter), LetterStatus.GREY);

			if (guessedLetter == solutionLetter) {
				currentGuessedLetter.setStatus(LetterStatus.GREEN);
			} else {
				count.put(solutionLetter, count.getOrDefault(solutionLetter, 0) + 1);
			}

			letters.add(currentGuessedLetter); // Add every letter, including GREY ones.
		}

		for (int i = 0; i < guessedWord.length(); i++) {
			char guessedLetter = guessedWord.charAt(i);
			char solutionLetter = solutionWord.charAt(i);

			if (guessedLetter == solutionLetter || count.getOrDefault(guessedLetter, 0) == 0) {
				continue;
			}

			count.put(guessedLetter, count.get(guessedLetter) - 1);
			letters.get(i).setStatus(LetterStatus.YELLOW);
		}

		// Sort the letters list by position in ascending order
		letters.sort(Comparator.comparingInt(Letter::getPosition));

		return new Word(letters, solutionWord);
	}
	public Solution getSolutionByWord(String word) {
		return solutionRepository.findByWord(word).get();
	}

	public String getRandomSolutionWord() {
		return solutionRepository.findRandomSolution().get().getWord();
	}
}
