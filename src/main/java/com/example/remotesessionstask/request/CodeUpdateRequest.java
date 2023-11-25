package com.example.remotesessionstask.request;


public record CodeUpdateRequest(String code, Long codeBlockId, boolean solutionMatched) {
}
