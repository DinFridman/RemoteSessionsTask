package com.example.remotesessionstask.initializer;

import com.example.remotesessionstask.service.CodeBlockService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final CodeBlockService codeBlockService;

    @PostConstruct
    public void initCodeBlocks() {
        codeBlockService.saveInitialCodeBlocks();
    }
}
