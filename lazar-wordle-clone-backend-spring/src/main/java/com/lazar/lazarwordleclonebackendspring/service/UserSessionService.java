package com.lazar.lazarwordleclonebackendspring.service;

import com.lazar.lazarwordleclonebackendspring.model.UserSession;
import com.lazar.lazarwordleclonebackendspring.repository.SolutionRepository;
import com.lazar.lazarwordleclonebackendspring.repository.UserSessionRepository;
import com.lazar.lazarwordleclonebackendspring.repository.UserTryRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service

public class UserSessionService {
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private UserTryService userTryService;

    public UserSession getCurrentSolutionForUser(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        return optionalUserSolution.orElseGet(() -> createNewSolutionForUser(username));
    }
    public Boolean decrementRemainingTriesForUser(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            userSession.setRemaining_tries(userSession.getRemaining_tries() - 1);
            userSessionRepository.save(userSession);
            return true;
        }
        return false;
    }
    public Integer getRemainingTriesForUser(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            return userSession.getRemaining_tries();
        }
        return -1;
    }
    public Boolean setStatusForUser(String username, String status){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            userSession.setStatus(status);
            userSessionRepository.save(userSession);
            return true;
        }
        return false;
    }
    public String getStatusForUser(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSession userSession = optionalUserSolution.get();
            return userSession.getStatus();
        }
        return "game_over";
    }
    public UserSession createNewSolutionForUser(String username){
        userTryService.deleteTriesForUser(username);
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
        userSession.setRemaining_tries(6);
        userSessionRepository.save(userSession);
        return userSession;
    }
    public UserSession checkGameStatus(String username){
        Optional<UserSession> optionalUserSolution = userSessionRepository.findByUsername(username);
        UserSession userSession;
        if(optionalUserSolution.isPresent()){
            userSession = optionalUserSolution.get();
            return userSession;
        }
        else{
            return new UserSession(username, "undefined", "undefined", 0);
        }
    }
}
