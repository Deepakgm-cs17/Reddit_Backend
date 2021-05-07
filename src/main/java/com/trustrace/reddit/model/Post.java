package com.trustrace.reddit.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@AllArgsConstructor
@Document(collection = "Posts")
@NoArgsConstructor
@Builder
public class Post {

    @Transient
    public static final String SEQUENCE_NAME = "post_sequence";
    @Id
    private Long postId;

    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;

    @Nullable
    private String url;

    @Nullable
    private String description;

    private Integer voteCount = 0;

    @DBRef
    private User user;

    private Instant createdDate;

    @DBRef
    private Subreddit subreddit;
}
