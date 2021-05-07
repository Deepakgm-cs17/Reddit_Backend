package com.trustrace.reddit.repository;

import com.trustrace.reddit.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}