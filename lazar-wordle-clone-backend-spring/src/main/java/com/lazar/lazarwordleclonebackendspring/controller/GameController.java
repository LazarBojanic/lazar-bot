package com.lazar.lazarwordleclonebackendspring.controller;

import com.lazar.lazarwordleclonebackendspring.model.*;
import com.lazar.lazarwordleclonebackendspring.request.UserTryRequest;
import com.lazar.lazarwordleclonebackendspring.response.UserTryResponse;
import com.lazar.lazarwordleclonebackendspring.service.SolutionService;
import com.lazar.lazarwordleclonebackendspring.util.Util;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lazar.lazarwordleclonebackendspring.response.NewSolutionResponse;
import com.lazar.lazarwordleclonebackendspring.service.UserSessionService;
import com.lazar.lazarwordleclonebackendspring.service.UserTryService;
@RestController
@RequestMapping("/game")
public class GameController {
	@Autowired
	private UserSessionService userSessionService;
    @Autowired
	private UserTryService userTryService;
    @Autowired
    private SolutionService solutionService;

    @GetMapping("/check-status")
	public ResponseEntity<UserSession> checkStatus(@RequestParam String username) {
		return ResponseEntity.ok(userSessionService.checkStatus(username));
	}
    @GetMapping("/new-game")
	public ResponseEntity<NewSolutionResponse> newGame(@RequestParam String username) {
		UserSession userSession = userSessionService.createNewSolution(username);
		if(userSession != null){
			return ResponseEntity.ok(new NewSolutionResponse("success"));
		}
		else{
			return ResponseEntity.internalServerError().body(new NewSolutionResponse("error"));
		}
	}

    @GetMapping("/get-current-solution")
    public ResponseEntity<UserSession> getCurrentSolution(@RequestParam String username) {
        return ResponseEntity.ok(userSessionService.getCurrentSolution(username));
    }
    @GetMapping("/set-solution")
	public ResponseEntity<NewSolutionResponse> setSolution(@RequestParam String username, @RequestParam String word) {
		UserSession userSession = userSessionService.setSolution(username, word);
		if(userSession != null){
			return ResponseEntity.ok(new NewSolutionResponse("success"));
		}
		else{
			return ResponseEntity.internalServerError().body(new NewSolutionResponse("error"));
		}
	}
    @GetMapping("/get-board")
    public ResponseEntity<UserBoard> getBoard(@RequestParam String username) {
        UserBoard userBoard = userTryService.getBoard(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok()
                .headers(headers)
                .body(userBoard);
    }

	@GetMapping("/get-keyboard")
	public ResponseEntity<UserKeyboard> getKeyboard(@RequestParam String username) {
        UserKeyboard userKeyboard = userTryService.getKeyboard(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok()
                .headers(headers)
                .body(userKeyboard);
	}
    @GetMapping("/get-keyboard-image")
	public ResponseEntity<Resource> getKeyboardImage(@RequestParam String username) {
        Resource keyboardImage = userTryService.getKeyboardImage(username);
        try {
            File tempFile = File.createTempFile("keyboard_" + username, ".png");
            FileOutputStream fos = new FileOutputStream(tempFile);
            IOUtils.copy(keyboardImage.getInputStream(), fos);
            fos.close();
            Resource savedResource = new FileSystemResource(tempFile);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(savedResource);
        } catch (IOException e) {
            Util.logger.error("Error while creating keyboard image for user: {}", username, e);
            return ResponseEntity.notFound().build();
        }
	}

    @GetMapping("/get-board-image")
	public ResponseEntity<Resource> getBoardImage(@RequestParam String username) {
        Resource keyboardImage = userTryService.getBoardImage(username);
        try {
            File tempFile = File.createTempFile("board_" + username, ".png");
            FileOutputStream fos = new FileOutputStream(tempFile);
            IOUtils.copy(keyboardImage.getInputStream(), fos);
            fos.close();
            Resource savedResource = new FileSystemResource(tempFile);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(savedResource);
        } catch (IOException e) {
            Util.logger.error("Error while creating board image for user: {}", username, e);
            return ResponseEntity.notFound().build();
        }
	}

    @PostMapping("/guess")
    public ResponseEntity<UserTryResponse> guess(@RequestBody UserTryRequest userTryRequest) {
        return ResponseEntity.ok(solutionService.guess(userTryRequest));
    }

}
