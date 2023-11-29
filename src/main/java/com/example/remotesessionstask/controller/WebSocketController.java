package com.example.remotesessionstask.controller;

import com.example.remotesessionstask.request.CodeUpdateRequest;
import com.example.remotesessionstask.request.ExitCodeBlockRequest;
import com.example.remotesessionstask.response.CodeUpdateResponse;
import com.example.remotesessionstask.response.DisconnectCodeBlockResponse;
import com.example.remotesessionstask.service.RemoteSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import static com.example.remotesessionstask.utils.Constants.CODE_UPDATE_TYPE;
import static com.example.remotesessionstask.utils.Constants.REMOTE_SESSION_TERMINATION_TYPE;

@Log4j2
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RemoteSessionService remoteSessionService;
    private final SimpMessagingTemplate template;


    @MessageMapping("/codeUpdate/{codeBlockId}")
    public void codeChange(@DestinationVariable int codeBlockId,
                           CodeUpdateRequest request, SimpMessageHeaderAccessor headerAccessor) {

        CodeUpdateResponse codeUpdateResponse = new CodeUpdateResponse(request.code(), CODE_UPDATE_TYPE);

        String destination = "/topic/remoteSessionDetails/" + codeBlockId;
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("codeBlockId", codeBlockId);
        headerAccessor.setDestination(destination);
        this.template.convertAndSend(destination, codeUpdateResponse);
    }

    @MessageMapping("/disconnectCodeBlock/{codeBlockId}")
    public void exitCodeBlock(@DestinationVariable int codeBlockId,
                              ExitCodeBlockRequest request, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Disconnect codeBlock Id {} request is incoming! Details : {}", codeBlockId, request);

        boolean isRemoteSessionTerminated =
                remoteSessionService.handleCodeBlockExit(codeBlockId);


        DisconnectCodeBlockResponse disconnectCodeBlockResponse =
                new DisconnectCodeBlockResponse(isRemoteSessionTerminated, REMOTE_SESSION_TERMINATION_TYPE);

        String destination = "/topic/remoteSessionDetails/" + codeBlockId;
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("codeBlockId", codeBlockId);
        headerAccessor.setDestination(destination);

        this.template.convertAndSend(destination, disconnectCodeBlockResponse);
    }


    @GetMapping("/")
    public String index() {
        return "test";
    }

}

