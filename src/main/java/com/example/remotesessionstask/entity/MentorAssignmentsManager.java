package com.example.remotesessionstask.entity;

import lombok.Data;


@Data
public class MentorAssignmentsManager {
    private final boolean[] mentorAssignments;

    public MentorAssignmentsManager(int numOfCodeBlocks) {
        this.mentorAssignments = new boolean[numOfCodeBlocks];
    }

    public void resetMentorAssignment(int codeBlockId) {
        this.mentorAssignments[codeBlockId] = false;
    }

    public boolean isMentorAssigned(int codeBlockId) {
        return mentorAssignments[codeBlockId];
    }

    public void assignMentor(int codeBlockId) {
        mentorAssignments[codeBlockId] = true;
    }
}
