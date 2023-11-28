package com.example.remotesessionstask.service;

import com.example.remotesessionstask.dto.CodeBlockDTO;
import com.example.remotesessionstask.entity.CodeBlock;
import com.example.remotesessionstask.respository.CodeBlockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class CodeBlockService {
    private final CodeBlockRepository codeBlockRepository;

    public void saveInitialCodeBlocks() {
        List<CodeBlock> codeBlocks = Arrays.asList(
                new CodeBlock(0, "Async Case", "function asyncCase() {}", "function asyncCase() {async solution}"),
                new CodeBlock(1, "JS Fundamentals", "function JSFundamentals() {}", "function JSFundamentals() {JSFundamentals solution}"),
                new CodeBlock(2, "Functional Programming", "function functionalProgramming() {}", "function functionalProgramming() {functionalProgramming solution}"),
                new CodeBlock(3, "Object Oriented Programming", "function objectOrientedProgramming() {}", "function objectOrientedProgramming() {OOP solution}")
        );
        if(codeBlockRepository.saveAll(codeBlocks).isEmpty())
            log.error("Error while saving codeblocks!");

        log.info("CodeBlocks initialized successfully.");
    }

    public CodeBlockDTO getCodeBlock(int codeBlockId) {
        CodeBlock codeBlock = codeBlockRepository
                .findById(codeBlockId)
                .orElseThrow(() -> new EntityNotFoundException("CodeBlock is not found!"));

        CodeBlockDTO codeBlockDTO = mapCodeBlockToCodeBlockDTO(codeBlock);

        log.debug("CodeBlockDTO details : {}",codeBlockDTO);

        return codeBlockDTO;
    }
    private CodeBlockDTO mapCodeBlockToCodeBlockDTO(CodeBlock codeBlock) {
        return new CodeBlockDTO(
                codeBlock.getCodeBlockId(),
                codeBlock.getTitle(),
                codeBlock.getCode(),
                codeBlock.getSolution());
    }
}
