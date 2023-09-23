package com.lazar.lazarwordleclonebackendspring.controller;

import com.lazar.lazarwordleclonebackendspring.response.NewSolutionResponse;
import com.lazar.lazarwordleclonebackendspring.model.UserSession;
import com.lazar.lazarwordleclonebackendspring.service.UserSessionService;
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
	private UserSessionService userSessionService;
	@PostMapping("/check")
	public ResponseEntity<UserTryResponse> checkSolution(@RequestBody UserTryRequest userTryRequest) {
		return ResponseEntity.ok(solutionService.checkSolution(userTryRequest));
	}
	@GetMapping("/checkGameStatus")
	public ResponseEntity<UserSession> checkGameStatus(@RequestParam String username) {
		return ResponseEntity.ok(userSessionService.checkGameStatus(username));
	}
	@GetMapping("/getCurrent")
	public ResponseEntity<UserSession> getCurrentSolution(@RequestParam String username) {
		return ResponseEntity.ok(userSessionService.getCurrentSolutionForUser(username));
	}
	@GetMapping("/new")
	public ResponseEntity<NewSolutionResponse> newSolution(@RequestParam String username) {
		UserSession userSession = userSessionService.createNewSolutionForUser(username);
		if(userSession != null){
			return ResponseEntity.ok(new NewSolutionResponse("success"));
		}
		else{
			return ResponseEntity.internalServerError().body(new NewSolutionResponse("error"));
		}
	}
}
