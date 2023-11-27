package com.example.remotesessionstask.dto;

import lombok.Builder;

@Builder
public record CodeBlockDTO(int codeBlockId, String title, String code, String solution) {
}
