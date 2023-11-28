package com.example.remotesessionstask.entity;

import lombok.Data;


@Data
public class MentorAssignmentsManager {
    private final String[] mentorAssignments;

    public MentorAssignmentsManager(int numOfCodeBlocks) {
        this.mentorAssignments = new String[numOfCodeBlocks];
    }

    public void resetMentorAssignment(int codeBlockId) {
        this.mentorAssignments[codeBlockId] = null;
    }

    public boolean isMentorAssigned(int codeBlockId) {
        return mentorAssignments[codeBlockId] != null;
    }

    public void assignMentor(int codeBlockId, String sessionId) {
        mentorAssignments[codeBlockId] = sessionId;
    }

    public boolean checkIfMentorIsAssignedBySessionId(int codeBlockId, String sessionId) {
        return mentorAssignments[codeBlockId].equals(sessionId);
    }
}
