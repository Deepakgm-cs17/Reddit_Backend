package com.trustrace.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "Vote")
public class Vote {

    @Transient
    public static final String SEQUENCE_NAME = "vote_sequence";
    @Id
    private Long voteId;

    private VoteType voteType;

    @NotNull
    @DBRef
    private Post post;

    @DBRef
    private User user;
}
