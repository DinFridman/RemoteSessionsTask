package com.example.remotesessionstask.service;

import com.example.remotesessionstask.dto.RemoteSessionDTO;
import com.example.remotesessionstask.entity.RemoteSession;
import com.example.remotesessionstask.exception.EntityNotFoundException;
import com.example.remotesessionstask.repository.RemoteSessionRepository;
import com.example.remotesessionstask.request.CodeUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.example.remotesessionstask.utils.Constants.MENTOR_ROLE;
import static com.example.remotesessionstask.utils.Constants.STUDENT_ROLE;

@Service
@RequiredArgsConstructor
@Log4j2
public class RemoteSessionService {
    private final RemoteSessionRepository remoteSessionRepository;


    public String getSessionRole(Long codeBlockId) {
         String role = checkIfRemoteSessionExistsByCodeBlockId(codeBlockId) ? STUDENT_ROLE : MENTOR_ROLE;

         if(role.equals(MENTOR_ROLE)) {
             RemoteSession remoteSession = createRemoteSession(codeBlockId);
             log.info("RemoteSession ID: {} created successfully.", remoteSession.getCodeBlockId());
             remoteSessionRepository.save(remoteSession);
         }
         return role;
    }

    private RemoteSession createRemoteSession(Long codeBlockId) {
        return RemoteSession.builder()
                .codeBlockId(codeBlockId)
                .code("")
                .solutionMatched(false)
                .build();
    }

    private boolean checkIfRemoteSessionExistsByCodeBlockId(Long codeBlockId) {
        boolean isExists =
                remoteSessionRepository.existsById(codeBlockId);

        log.info("RemoteSession ID: {} Exists : {}", codeBlockId,isExists);

        return isExists;
    }

    public RemoteSessionDTO handleCodeChange(CodeUpdateRequest codeUpdateRequest) {
        RemoteSession remoteSession = fetchRemoteSession(codeUpdateRequest.codeBlockId());

        updateAndSaveRemoteSession(remoteSession,codeUpdateRequest);

        return mapRemoteSessionToRemoteSessionDTO(remoteSession);
    }

    private void updateAndSaveRemoteSession(RemoteSession remoteSession,
                                            CodeUpdateRequest codeUpdateRequest) {

        remoteSession.setCode(codeUpdateRequest.code());
        remoteSession.setSolutionMatched(codeUpdateRequest.solutionMatched());
        remoteSessionRepository.save(remoteSession);
    }

    private RemoteSession fetchRemoteSession(Long codeBlockId) {
        return remoteSessionRepository.findById(codeBlockId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "RemoteSession not found for code block ID: "
                                + codeBlockId));
    }

    private RemoteSessionDTO mapRemoteSessionToRemoteSessionDTO(RemoteSession remoteSession) {
        return RemoteSessionDTO.builder()
                .solutionMatched(remoteSession.isSolutionMatched())
                .code(remoteSession.getCode())
                .codeBlockId(remoteSession.getCodeBlockId())
                .build();
    }
}
