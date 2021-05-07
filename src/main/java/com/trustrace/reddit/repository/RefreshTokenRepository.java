package com.trustrace.reddit.repository;

import com.trustrace.reddit.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
