package com.example.remotesessionstask.service;

import com.example.remotesessionstask.dto.CodeBlockDTO;
import com.example.remotesessionstask.entity.MentorAssignmentsManager;
import com.example.remotesessionstask.response.InitRemoteSessionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.example.remotesessionstask.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class RemoteSessionService {
    private final MentorAssignmentsManager mentorAssignmentsManager;
    private final CodeBlockService codeBlockService;

    public synchronized InitRemoteSessionData getInitRemoteSessionData(int codeBlockId, String sessionId) {
        validateCodeBlockId(codeBlockId);

        String role = getRole(codeBlockId);
        mentorAssignmentsManager.assignMentor(codeBlockId,sessionId);
        CodeBlockDTO codeBlockDTO = codeBlockService.getCodeBlock(codeBlockId);

        return createInitRemoteSessionData(role,codeBlockDTO);
    }

    public synchronized boolean handleCodeBlockExit(int codeBlockId, String sessionId) {
        if(mentorAssignmentsManager.checkIfMentorIsAssignedBySessionId(codeBlockId,sessionId)) {
            mentorAssignmentsManager.resetMentorAssignment(codeBlockId);
            return REMOTE_SESSION_TERMINATION_SIGNAL;
        }
        return !REMOTE_SESSION_TERMINATION_SIGNAL;
    }

    private InitRemoteSessionData createInitRemoteSessionData(String role, CodeBlockDTO codeBlockDTO) {
        return new InitRemoteSessionData(
                role,
                codeBlockDTO.code(),
                codeBlockDTO.solution(),
                INIT_DATA_TYPE);

    }

    private synchronized String getRole(int codeBlockId) {
        if(mentorAssignmentsManager.isMentorAssigned(codeBlockId))
            return STUDENT_ROLE;

        return MENTOR_ROLE;
    }

    private void validateCodeBlockId(int codeBlockId) {
        if (codeBlockId < 0 || codeBlockId >= NUM_OF_CODE_BLOCKS) {
            throw new IllegalArgumentException("Invalid code block ID");
        }
    }
}
