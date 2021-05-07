package com.trustrace.reddit.repository;

import com.trustrace.reddit.model.Comment;
import com.trustrace.reddit.model.Post;
import com.trustrace.reddit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
