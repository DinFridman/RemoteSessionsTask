package com.example.remotesessionstask.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("RemoteSession")
@AllArgsConstructor
@Builder
public class RemoteSession {
    @Id
    private Long sessionId;
    private Long mentorId;
    private Long studentId;
    private boolean solutionMatched;
}
