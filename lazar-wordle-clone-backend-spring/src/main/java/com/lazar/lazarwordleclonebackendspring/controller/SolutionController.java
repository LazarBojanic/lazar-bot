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
	@PostMapping("/guess")
	public ResponseEntity<UserTryResponse> guessSolution(@RequestBody UserTryRequest userTryRequest) {
		return ResponseEntity.ok(solutionService.guessSolution(userTryRequest));
	}
	
	@GetMapping("/getCurrent")
	public ResponseEntity<UserSession> getCurrentSolution(@RequestParam String username) {
		return ResponseEntity.ok(userSessionService.getCurrentSolutionForUser(username));
	}
	
}
