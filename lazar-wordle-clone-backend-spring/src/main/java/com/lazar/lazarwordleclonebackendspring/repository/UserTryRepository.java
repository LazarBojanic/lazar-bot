package com.lazar.lazarwordleclonebackendspring.repository;
import com.lazar.lazarwordleclonebackendspring.model.UserTry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public interface UserTryRepository extends MongoRepository<UserTry, String> {
    Optional<UserTry> findByUsername(String username);
    Optional<List<UserTry>> findAllByUsername(String username);
    void deleteByUsername(String username);
}