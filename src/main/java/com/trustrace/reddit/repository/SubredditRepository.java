package com.trustrace.reddit.repository;

import com.trustrace.reddit.model.Subreddit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends MongoRepository<Subreddit,Long> {
    Optional<Subreddit> findByName(String subredditName);
}
