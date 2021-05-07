package com.trustrace.reddit.repository;

import com.trustrace.reddit.model.Post;
import com.trustrace.reddit.model.User;
import com.trustrace.reddit.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends MongoRepository<Vote ,Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
