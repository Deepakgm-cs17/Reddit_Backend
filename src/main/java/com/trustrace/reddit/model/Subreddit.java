package com.trustrace.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "Subreddits")
@Builder
@NoArgsConstructor
public class Subreddit {

    @Transient
    public static final String SEQUENCE_NAME = "subreddit_sequence";
    @Id
    private Long id;

    @NotBlank(message = "Community name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @DBRef
    private List<Post> posts;

    private Instant createdDate;

    @DBRef
    private User user;
}
