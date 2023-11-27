package com.example.remotesessionstask.controller;

import com.example.remotesessionstask.request.CodeUpdateRequest;
import com.example.remotesessionstask.request.ExitCodeBlockRequest;
import com.example.remotesessionstask.response.CodeChangeResponse;
import com.example.remotesessionstask.response.ExitCodeBlockResponse;
import com.example.remotesessionstask.response.RoleResponse;
import com.example.remotesessionstask.service.RemoteSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Log4j2
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RemoteSessionService remoteSessionService;
    private final SimpMessagingTemplate template;


    @MessageMapping("/getRole/{codeBlockId}")
    public void getRole(@DestinationVariable int codeBlockId, SimpMessageHeaderAccessor headerAccessor) {

        String role = remoteSessionService.getRole(codeBlockId);

        RoleResponse roleResponse = new RoleResponse(role);

        String destination = "/topic/roles/" + codeBlockId;
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("codeBlockId", codeBlockId);
        headerAccessor.setDestination(destination);
        this.template.convertAndSend(destination, roleResponse);
    }

    @MessageMapping("/codeChange/{codeBlockId}")
    public void codeChange(@DestinationVariable int codeBlockId,
                           CodeUpdateRequest request, SimpMessageHeaderAccessor headerAccessor) {

        CodeChangeResponse codeChangeResponse = new CodeChangeResponse(request.code());

        String destination = "/topic/code/" + codeBlockId;
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("codeBlockId", codeBlockId);
        headerAccessor.setDestination(destination);
        this.template.convertAndSend(destination, codeChangeResponse);
    }

    @MessageMapping("/exitCodeBlock/{codeBlockId}")
    public void exitCodeBlock(@DestinationVariable int codeBlockId,
                              ExitCodeBlockRequest request, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Exit codeBlock Id {} request is incoming! Details : {}", codeBlockId, request);

        boolean isRemoteSessionTerminated =
                remoteSessionService.handleCodeBlockExit(codeBlockId, request.role());


        ExitCodeBlockResponse exitCodeBlockResponse = new ExitCodeBlockResponse(isRemoteSessionTerminated);

        String destination = "/topic/remoteSessionStatus/" + codeBlockId;
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("codeBlockId", codeBlockId);
        headerAccessor.setDestination(destination);

        this.template.convertAndSend(destination, exitCodeBlockResponse);
    }


}
