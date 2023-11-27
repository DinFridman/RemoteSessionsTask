package com.example.remotesessionstask.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CodeBlocks")
@Table(name = "codeblocks")
public class CodeBlock {
    @Id
    @Column(
            name = "codeblock_id",
            updatable = false,
            nullable = false
    )
    private int codeBlockId;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;

    @Column(
            name = "code",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String code;

    @Column(
            name = "solution",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String solution;
}
