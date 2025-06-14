package com.lazar.lazarwordleclonebackendspring.service;

import com.lazar.lazarwordleclonebackendspring.model.UserSession;
import com.lazar.lazarwordleclonebackendspring.repository.SolutionRepository;
import com.lazar.lazarwordleclonebackendspring.repository.UserSessionRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserSessionService {
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private UserTryService userTryService;

    public UserSession getCurrentSolution(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        return optionalUserSolution.orElseGet(() -> createNewSolution(username));
    }
    public void decrementRemainingTries(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            userSession.setRemainingTries(userSession.getRemainingTries() - 1);
            userSessionRepository.save(userSession);
        }
    }
    public int getRemainingTries(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            return userSession.getRemainingTries();
        }
        return -1;
    }
    public void setStatus(String username, String status){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            userSession.setStatus(status);
            userSessionRepository.save(userSession);
        }
    }
    public String getStatus(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            return userSession.getStatus();
        }
        return "gameOver";
    }
    public UserSession createNewSolution(String username){
        userTryService.deleteTries(username);
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        UserSession userSession;
        if(optionalUserSolution.isPresent()){
            userSession = optionalUserSolution.get();
        }
        else{
            userSession = new UserSession();
            userSession.setUsername(username);
        }
        userSession.setWord(solutionRepository.findRandomSolution().get().getWord());
        userSession.setStatus("unsolved");
        userSession.setRemainingTries(6);
        userSessionRepository.save(userSession);
        return userSession;
    }
    public UserSession setSolution(String username, String word){
        userTryService.deleteTries(username);
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        UserSession userSession;
        if(optionalUserSolution.isPresent()){
            userSession = optionalUserSolution.get();
        }
        else{
            userSession = new UserSession();
            userSession.setUsername(username);
        }
        userSession.setWord(solutionRepository.findByWord(word.toUpperCase()).get().getWord());
        userSession.setStatus("unsolved");
        userSession.setRemainingTries(6);
        userSessionRepository.save(userSession);
        return userSession;
    }
    public UserSession checkStatus(String username){
        Optional<UserSession> optionalUserSession = userSessionRepository.findByUsername(username);
        UserSession userSession;
        if(optionalUserSession.isPresent()){
            userSession = optionalUserSession.get();
            return userSession;
        }
        else{
            return new UserSession(username, "undefined", "undefined", 0);
        }
    }
}
