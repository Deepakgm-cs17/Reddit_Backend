package com.trustrace.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "RefreshToken")
public class RefreshToken {

    @Transient
    public static final String SEQUENCE_NAME = "refreshToken_sequence";
    @Id
    private int id;

    private String token;

    private Instant createdDate;
}
