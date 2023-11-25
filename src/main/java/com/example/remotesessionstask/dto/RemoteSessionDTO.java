package com.example.remotesessionstask.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoteSessionDTO {
    private Long codeBlockId;
    private String code;
    private boolean solutionMatched;
}
