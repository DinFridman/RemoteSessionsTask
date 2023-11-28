package com.example.remotesessionstask.service;

import com.example.remotesessionstask.dto.CodeBlockDTO;
import com.example.remotesessionstask.entity.CodeBlock;
import com.example.remotesessionstask.respository.CodeBlockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.remotesessionstask.utils.Constants.NUM_OF_CODE_BLOCKS;


@ExtendWith(MockitoExtension.class)
class CodeBlockServiceTest {
    @Mock private CodeBlockRepository codeBlockRepository;
    @InjectMocks private CodeBlockService underTest;
    private List<CodeBlock> codeBlocks;
    private CodeBlockDTO codeBlockDTO;
    private CodeBlock codeBlock;

    @BeforeEach
    void setUp() {
        codeBlocks = Arrays.asList(
                new CodeBlock(0, "Async Case", "function asyncCase() {}", "function asyncCase() {async solution}"),
                new CodeBlock(1, "JS Fundamentals", "function JSFundamentals() {}", "function JSFundamentals() {JSFundamentals solution}"),
                new CodeBlock(2, "Functional Programming", "function functionalProgramming() {}", "function functionalProgramming() {functionalProgramming solution}"),
                new CodeBlock(3, "Object Oriented Programming", "function objectOrientedProgramming() {}", "function objectOrientedProgramming() {OOP solution}")
        );

        codeBlock = codeBlocks.get(0);

        codeBlockDTO = new CodeBlockDTO(
                codeBlock.getCodeBlockId(),
                codeBlock.getTitle(),
                codeBlock.getCode(),
                codeBlock.getSolution());


    }

    @Test
    void saveInitialCodeBlocks() {
        underTest.saveInitialCodeBlocks();

        Mockito.verify(codeBlockRepository).saveAll(codeBlocks);
    }

    @Test
    void shouldGetCodeBlock() {
        Mockito.when(codeBlockRepository
                .findById(codeBlock.getCodeBlockId())).thenReturn(Optional.ofNullable(codeBlock));

        CodeBlockDTO actualResponse = underTest.getCodeBlock(codeBlock.getCodeBlockId());

        Assertions.assertEquals(actualResponse,codeBlockDTO);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenTryingToCodeBlock() {
        int invalidCodeBlockId = NUM_OF_CODE_BLOCKS + 1;

        Mockito.when(codeBlockRepository
                .findById(invalidCodeBlockId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> underTest.getCodeBlock(invalidCodeBlockId));

    }
}