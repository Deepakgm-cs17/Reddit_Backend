package com.trustrace.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@AllArgsConstructor
@Document(collection = "Comments")
@NoArgsConstructor
@Builder
public class Comment {

    @Transient
    public static final String SEQUENCE_NAME = "comment_sequence";
    @Id
    private Long id;

    @NotEmpty
    private String text;

    @DBRef
    private Post post;

    private Instant createdDate;

    @DBRef
    private User user;
}
