package com.lazar.lazarwordleclonebackendspring.controller;

import com.lazar.lazarwordleclonebackendspring.model.*;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
    @GetMapping("/setSolutionForUser")
	public ResponseEntity<NewSolutionResponse> setSolutionForUser(@RequestParam String username, @RequestParam String word) {
		UserSession userSession = userSessionService.setSolutionForUser(username, word);
		if(userSession != null){
			return ResponseEntity.ok(new NewSolutionResponse("success"));
		}
		else{
			return ResponseEntity.internalServerError().body(new NewSolutionResponse("error"));
		}
	}
    @GetMapping("/getBoardForUserRaw")
    public ResponseEntity<UserBoardRaw> getBoardForUserRaw(@RequestParam String username) {
        UserBoardRaw userBoardRaw = userTryService.getBoardForUserRaw(username);

        /*StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < userBoardRaw.getUserTryList().size(); i++) {
            UserTry userTry = userBoardRaw.getUserTryList().get(i);
            stringbuilder.append(userTry.getValidatedWord().getWord());
            if(i < userBoardRaw.getUserTryList().size() - 1){
                stringbuilder.append("\n");
            }
        }
        System.out.println(stringbuilder);*/

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok()
                .headers(headers)
                .body(userBoardRaw);
    }

	@GetMapping("/getKeyboardForUserRaw")
	public ResponseEntity<UserKeyboardRaw> getKeyboardForUserRaw(@RequestParam String username) {
        UserKeyboardRaw userKeyboardRaw = userTryService.getKeyboardForUserRaw(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok()
                .headers(headers)
                .body(userKeyboardRaw);
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
