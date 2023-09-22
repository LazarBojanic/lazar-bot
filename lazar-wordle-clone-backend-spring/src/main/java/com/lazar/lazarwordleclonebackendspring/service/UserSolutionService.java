package com.lazar.lazarwordleclonebackendspring.service;

import com.lazar.lazarwordleclonebackendspring.model.UserSolution;
import com.lazar.lazarwordleclonebackendspring.repository.SolutionRepository;
import com.lazar.lazarwordleclonebackendspring.repository.UserSolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserSolutionService {
    @Autowired
    private UserSolutionRepository userSolutionRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    public UserSolution getCurrentSolutionForUser(String username){
        Optional<UserSolution> optionalUserSolution = userSolutionRepository.findByUsername(username);
        return optionalUserSolution.orElseGet(() -> createNewSolutionForUser(username));
    }
    public Boolean decrementRemainingTriesForUser(String username){
        Optional<UserSolution> optionalUserSolution = userSolutionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSolution userSolution = optionalUserSolution.get();
            userSolution.setRemaining_tries(userSolution.getRemaining_tries() - 1);
            userSolutionRepository.save(userSolution);
            return true;
        }
        return false;
    }
    public Boolean setStatusForUser(String username, String status){
        Optional<UserSolution> optionalUserSolution = userSolutionRepository.findByUsername(username);
        if(optionalUserSolution.isPresent()){
            UserSolution userSolution = optionalUserSolution.get();
            userSolution.setStatus(status);
            userSolutionRepository.save(userSolution);
            return true;
        }
        return false;
    }
    public UserSolution createNewSolutionForUser(String username){
        Optional<UserSolution> optionalUserSolution = userSolutionRepository.findByUsername(username);
        UserSolution userSolution;
        if(optionalUserSolution.isPresent()){
            userSolution = optionalUserSolution.get();
        }
        else{
            userSolution = new UserSolution();
            userSolution.setUsername(username);
        }
        userSolution.setWord(solutionRepository.findRandomSolution().get().getWord());
        userSolution.setStatus("unsolved");
        userSolution.setRemaining_tries(6);
        userSolutionRepository.save(userSolution);
        return userSolution;
    }
}
