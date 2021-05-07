package com.trustrace.reddit.repository;

import com.trustrace.reddit.model.Post;
import com.trustrace.reddit.model.Subreddit;
import com.trustrace.reddit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post,Long>{
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
