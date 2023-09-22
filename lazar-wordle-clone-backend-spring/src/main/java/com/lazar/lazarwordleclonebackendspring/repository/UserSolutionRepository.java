package com.lazar.lazarwordleclonebackendspring.repository;

import com.lazar.lazarwordleclonebackendspring.model.UserSolution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSolutionRepository extends MongoRepository<UserSolution, String> {
    Optional<UserSolution> findByUsername(String username);
}
