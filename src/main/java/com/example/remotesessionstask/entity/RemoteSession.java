package com.example.remotesessionstask.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("RemoteSession")
@AllArgsConstructor
@Data
@Builder
public class RemoteSession {
    @Id
    private Long codeBlockId;
    private String code;
    private boolean solutionMatched;
}
