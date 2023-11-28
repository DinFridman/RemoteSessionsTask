package com.example.remotesessionstask.service;

import com.example.remotesessionstask.dto.CodeBlockDTO;
import com.example.remotesessionstask.entity.MentorAssignmentsManager;
import com.example.remotesessionstask.response.InitRemoteSessionData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.example.remotesessionstask.utils.Constants.*;
@ExtendWith(MockitoExtension.class)
class RemoteSessionServiceTest {
    @Mock private CodeBlockService codeBlockService;
    @InjectMocks private RemoteSessionService underTest;
    @Mock private MentorAssignmentsManager mentorAssignmentsManager;
    private String sessionId;
    private CodeBlockDTO codeBlockDTO;
    private InitRemoteSessionData expectedResponse;
    @BeforeEach
    void setUp() {
        sessionId = String.valueOf(1);
        codeBlockDTO = new CodeBlockDTO(0, "Async Case", "function asyncCase() {}", "function asyncCase() {async solution}");

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTryingToGetInitRemoteSessionData() {
        int invalidCodeBlockId = NUM_OF_CODE_BLOCKS + 1;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> underTest.getInitRemoteSessionData(invalidCodeBlockId, sessionId));
    }

    @Test
    void shouldReturnInitRemoteSessionDataWithStudentRole() {
        int codeBlockId = codeBlockDTO.codeBlockId();
        expectedResponse = new InitRemoteSessionData(STUDENT_ROLE,
                codeBlockDTO.code(), codeBlockDTO.solution(), INIT_DATA_TYPE);

        Mockito.when(mentorAssignmentsManager.isMentorAssigned(codeBlockId)).thenReturn(true);
        Mockito.when(codeBlockService.getCodeBlock(codeBlockId)).thenReturn(codeBlockDTO);

        InitRemoteSessionData actualResponse =
                underTest.getInitRemoteSessionData(codeBlockId,sessionId);

        Assertions.assertEquals(expectedResponse,actualResponse);
    }

    @Test
    void shouldReturnInitRemoteSessionDataWithMentorRole() {
        int codeBlockId = codeBlockDTO.codeBlockId();
        expectedResponse = new InitRemoteSessionData(MENTOR_ROLE,
                codeBlockDTO.code(), codeBlockDTO.solution(), INIT_DATA_TYPE);

        Mockito.when(mentorAssignmentsManager.isMentorAssigned(codeBlockId)).thenReturn(false);
        Mockito.when(codeBlockService.getCodeBlock(codeBlockId)).thenReturn(codeBlockDTO);

        InitRemoteSessionData actualResponse =
                underTest.getInitRemoteSessionData(codeBlockId,sessionId);

        Assertions.assertEquals(expectedResponse,actualResponse);
        Mockito.verify(mentorAssignmentsManager).assignMentor(codeBlockId,sessionId);
    }

    @Test
    void shouldReturnRemoteSessionTerminationSignalTrueWhenCallingHandleCodeBlockExit() {
        int codeBlockId = codeBlockDTO.codeBlockId();
        Mockito.when(mentorAssignmentsManager.checkIfMentorIsAssignedBySessionId(codeBlockId,sessionId)).thenReturn(true);

        boolean actualResponse = underTest.handleCodeBlockExit(codeBlockId,sessionId);

        Assertions.assertEquals(REMOTE_SESSION_TERMINATION_SIGNAL, actualResponse);
        Mockito.verify(mentorAssignmentsManager).resetMentorAssignment(codeBlockId);
    }

    @Test
    void shouldReturnRemoteSessionTerminationSignalFalseWhenCallingHandleCodeBlockExit() {
        int codeBlockId = codeBlockDTO.codeBlockId();
        Mockito.when(mentorAssignmentsManager.checkIfMentorIsAssignedBySessionId(codeBlockId,sessionId)).thenReturn(false);

        boolean actualResponse = underTest.handleCodeBlockExit(codeBlockId,sessionId);

        Assertions.assertEquals(!REMOTE_SESSION_TERMINATION_SIGNAL, actualResponse);
    }
}