package com.trustrace.reddit.service;

import com.trustrace.reddit.dto.SubredditDto;
import com.trustrace.reddit.exception.SpringRedditException;
import com.trustrace.reddit.model.Post;
import com.trustrace.reddit.model.Subreddit;
import com.trustrace.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public SubredditDto createSubreddit(SubredditDto subredditDto) {
        subredditDto.setId((long) sequenceGeneratorService.getSequenceNumber(Subreddit.SEQUENCE_NAME));
        Subreddit subreddit = new Subreddit();
        subreddit.setId(subredditDto.getId());
        subreddit.setName(subredditDto.getName());
        subreddit.setDescription(subredditDto.getDescription());
        subreddit.setCreatedDate(Instant.now());
        subredditRepository.save(subreddit);
        return subredditDto;
    }

    public List<SubredditDto> getAllSubreddits() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        int size = subreddit.getPosts() == null ? 0 : subreddit.getPosts().size();
        return SubredditDto.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(size)
                .build();
    }

    public SubredditDto getSubredditById(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId).orElseThrow(() -> new SpringRedditException("No Subreddit found."));
        int size = subreddit.getPosts() == null ? 0 : subreddit.getPosts().size();
        return SubredditDto.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(size)
                .build();
    }
}
