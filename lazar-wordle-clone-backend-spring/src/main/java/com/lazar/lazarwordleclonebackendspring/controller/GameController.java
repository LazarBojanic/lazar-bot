package com.lazar.lazarwordleclonebackendspring.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.lazar.lazarwordleclonebackendspring.model.LetterStatus;
import com.lazar.lazarwordleclonebackendspring.model.UserSession;
import com.lazar.lazarwordleclonebackendspring.model.UserTry;
import com.lazar.lazarwordleclonebackendspring.response.NewSolutionResponse;
import com.lazar.lazarwordleclonebackendspring.service.SolutionService;
import com.lazar.lazarwordleclonebackendspring.service.UserSessionService;
import com.lazar.lazarwordleclonebackendspring.service.UserTryService;
@RestController
@RequestMapping("/game")
public class GameController {
	@Autowired
	private UserSessionService userSessionService;
    @Autowired
	private UserTryService userTryService;

    @GetMapping("/checkGameStatus")
	public ResponseEntity<UserSession> checkGameStatus(@RequestParam String username) {
		return ResponseEntity.ok(userSessionService.checkGameStatus(username));
	}
    @GetMapping("/getUserTries")
	public ResponseEntity<List<UserTry>> getUserTries(@RequestParam String username) {
		return ResponseEntity.ok(userTryService.getUserTries(username));
	}
    @GetMapping("/getLetterStatusesForUser")
	public ResponseEntity<List<LetterStatus>> getLetterStatusesForUser(@RequestParam String username) {
		return ResponseEntity.ok(userTryService.getLetterStatusesForUser(username));
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
    @GetMapping("/set")
	public ResponseEntity<NewSolutionResponse> setSolution(@RequestParam String username, @RequestParam String word) {
		UserSession userSession = userSessionService.setSolutionForUser(username, word);
		if(userSession != null){
			return ResponseEntity.ok(new NewSolutionResponse("success"));
		}
		else{
			return ResponseEntity.internalServerError().body(new NewSolutionResponse("error"));
		}
	}
    @GetMapping("/getKeyboardForUser")
	public ResponseEntity<Resource> getKeyboardForUser(@RequestParam String username) {
        Resource keyboardImage = userTryService.getKeyboardForUser(username);
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
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
	}
    @GetMapping("/getBoardForUser")
	public ResponseEntity<Resource> getBoardForUser(@RequestParam String username) {
        Resource keyboardImage = userTryService.getBoardForUser(username);
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
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
	}
}
