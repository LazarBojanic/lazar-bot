package com.lazar.lazarwordleclonebackendspring.controller;

import com.lazar.lazarwordleclonebackendspring.response.NewSolutionResponse;
import com.lazar.lazarwordleclonebackendspring.model.UserSolution;
import com.lazar.lazarwordleclonebackendspring.service.UserSolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lazar.lazarwordleclonebackendspring.request.UserTryRequest;
import com.lazar.lazarwordleclonebackendspring.response.UserTryResponse;
import com.lazar.lazarwordleclonebackendspring.service.SolutionService;

@RestController
@RequestMapping("/solutions")
public class SolutionController {
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private UserSolutionService userSolutionService;
	@PostMapping("/check")
	public ResponseEntity<UserTryResponse> checkSolution(@RequestBody UserTryRequest userTry) {
		return ResponseEntity.ok(solutionService.checkSolution(userTry));
	}
	@GetMapping("/getCurrent")
	public ResponseEntity<UserSolution> getCurrentSolution(@RequestParam String username) {
		return ResponseEntity.ok(userSolutionService.getCurrentSolutionForUser(username));
	}
	@GetMapping("/new")
	public ResponseEntity<NewSolutionResponse> newSolution(@RequestParam String username) {
		UserSolution userSolution = userSolutionService.createNewSolutionForUser(username);
		if(userSolution != null){
			return ResponseEntity.ok(new NewSolutionResponse("success"));
		}
		else{
			return ResponseEntity.internalServerError().body(new NewSolutionResponse("error"));
		}
	}
}
