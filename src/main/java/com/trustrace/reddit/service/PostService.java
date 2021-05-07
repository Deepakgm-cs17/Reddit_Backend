package com.trustrace.reddit.service;

import com.trustrace.reddit.dto.PostRequest;
import com.trustrace.reddit.dto.PostResponse;
import com.trustrace.reddit.exception.PostNotFoundException;
import com.trustrace.reddit.exception.SubredditNotFoundException;
import com.trustrace.reddit.model.*;
import com.trustrace.reddit.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;

    public void save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRequest.setPostId((long) sequenceGeneratorService.getSequenceNumber(Post.SEQUENCE_NAME));
        Post post = new Post();
        post.setPostId(postRequest.getPostId());
        post.setPostName(postRequest.getPostName());
        post.setDescription(postRequest.getDescription());
        post.setUrl(postRequest.getUrl());
        post.setCreatedDate(Instant.now());
        post.setVoteCount(0);
        post.setUser(authService.getCurrentUser());
        post.setSubreddit(subreddit);
        postRepository.save(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    private PostResponse mapTo(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .subredditName(post.getSubreddit().getName())
                .userName(post.getUser().getUsername())
                .voteCount(post.getVoteCount())
                .upVote(checkVoteType(post, VoteType.UPVOTE))
                .commentCount(commentRepository.findByPost(post).size())
                .downVote(checkVoteType(post, VoteType.DOWNVOTE))
                .build();
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository
                    .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .subredditName(post.getSubreddit().getName())
                .userName(post.getUser().getUsername())
                .voteCount(post.getVoteCount())
                .commentCount(commentRepository.findByPost(post).size())
                .upVote(checkVoteType(post, VoteType.UPVOTE))
                .downVote(checkVoteType(post, VoteType.DOWNVOTE))
                .build();
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException(id.toString()));
        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }
}
