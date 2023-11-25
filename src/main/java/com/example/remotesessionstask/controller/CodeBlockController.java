package com.example.remotesessionstask.controller;

import com.example.remotesessionstask.entity.CodeBlock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/codeblocks")
public class CodeBlockController {

    @GetMapping("/getCodeBlocks")
    public ResponseEntity<List<CodeBlock>> getCodeBlocks() {
        List<CodeBlock> codeBlocks = new ArrayList<>();

        codeBlocks.add(new CodeBlock( 1L,"Async Case" , "" , "async function solution"));
        codeBlocks.add(new CodeBlock(2L,"JS Fundamentals", "", "JS fundamentals solution"));
        codeBlocks.add(new CodeBlock(3L,"Functional Programming", "", "functional programming solution"));
        codeBlocks.add(new CodeBlock( 4L,"Object Oriented Programming","", "OOP solution"));

        return ResponseEntity.ok(codeBlocks);
    }
}
