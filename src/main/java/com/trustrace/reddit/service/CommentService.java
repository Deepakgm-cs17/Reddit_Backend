package com.trustrace.reddit.service;

import com.trustrace.reddit.dto.CommentsDto;
import com.trustrace.reddit.dto.PostResponse;
import com.trustrace.reddit.exception.PostNotFoundException;
import com.trustrace.reddit.exception.SubredditNotFoundException;
import com.trustrace.reddit.model.*;
import com.trustrace.reddit.repository.CommentRepository;
import com.trustrace.reddit.repository.PostRepository;
import com.trustrace.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public void save(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        commentsDto.setId((long) sequenceGeneratorService.getSequenceNumber(Comment.SEQUENCE_NAME));
        Comment comment = new Comment();
        comment.setId(commentsDto.getId());
        comment.setText(commentsDto.getText());
        comment.setUser(authService.getCurrentUser());
        comment.setCreatedDate(Instant.now());
        comment.setPost(post);
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }
    public List<CommentsDto> getAllCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    private CommentsDto mapTo(Comment comment) {
        return CommentsDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getText())
                .userName(comment.getUser().getUsername())
                .build();
    }
}
