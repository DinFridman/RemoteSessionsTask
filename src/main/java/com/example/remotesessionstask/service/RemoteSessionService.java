package com.example.remotesessionstask.service;

import com.example.remotesessionstask.dto.CodeBlockDTO;
import com.example.remotesessionstask.response.InitRemoteSessionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import static com.example.remotesessionstask.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class RemoteSessionService {
    private final boolean[] mentorAssignments = new boolean[NUM_OF_CODE_BLOCKS];
    private final CodeBlockService codeBlockService;

    public synchronized InitRemoteSessionData getInitRemoteSessionData(int codeBlockId) {
        String role = getRole(codeBlockId);
        CodeBlockDTO codeBlockDTO = codeBlockService.getCodeBlock(codeBlockId);

        return createInitRemoteSessionData(role,codeBlockDTO);
    }

    private InitRemoteSessionData createInitRemoteSessionData(String role, CodeBlockDTO codeBlockDTO) {
        return new InitRemoteSessionData(
                role,
                codeBlockDTO.code(),
                codeBlockDTO.solution(),
                INIT_DATA_TYPE);

    }

    public synchronized String getRole(int codeBlockId) {
        if(isMentorAssigned(codeBlockId))
            return STUDENT_ROLE;

        assignMentor(codeBlockId);
        return MENTOR_ROLE;
    }

    public synchronized boolean handleCodeBlockExit(int codeBlockId, String role) {
        if(role.equals(MENTOR_ROLE)) {
            resetMentorAssignment(codeBlockId);
            return REMOTE_SESSION_TERMINATION_SIGNAL;
        }
        return !REMOTE_SESSION_TERMINATION_SIGNAL;
    }

    private synchronized void assignMentor(int codeBlockId) {
        validateCodeBlockId(codeBlockId);

        if (!mentorAssignments[codeBlockId]) {
            mentorAssignments[codeBlockId] = true;
        }
    }

    private synchronized boolean isMentorAssigned(int codeBlockId) {
        validateCodeBlockId(codeBlockId);

        return mentorAssignments[codeBlockId];
    }

    private synchronized void resetMentorAssignment(int codeBlockId) {
        validateCodeBlockId(codeBlockId);

        mentorAssignments[codeBlockId] = false;
    }

    private void validateCodeBlockId(int codeBlockId) {
        if (codeBlockId < 0 || codeBlockId >= mentorAssignments.length) {
            throw new IllegalArgumentException("Invalid code block ID");
        }
    }
}
