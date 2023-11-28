package com.example.remotesessionstask.controller;

import com.example.remotesessionstask.response.InitRemoteSessionData;
import com.example.remotesessionstask.service.RemoteSessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Log4j2
@Component
public class StompEventListeners implements ApplicationListener<SessionSubscribeEvent> {

    private final SimpMessageSendingOperations messagingTemplate;
    private final RemoteSessionService remoteSessionService;

    public StompEventListeners(SimpMessageSendingOperations messagingTemplate, RemoteSessionService remoteSessionService) {
        this.messagingTemplate = messagingTemplate;
        this.remoteSessionService = remoteSessionService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent  event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        assert command != null;
        if (command.equals(StompCommand.SUBSCRIBE)) {
            String sessionId = accessor.getSessionId();
            String destination = accessor.getDestination();

            log.debug("subscription destination: " + destination);

            if (destination != null && destination.startsWith("/topic/remoteSessionDetails/")) {
                int codeBlockId = extractCodeBlockId(destination);

                log.debug("subscription id : " + sessionId);

                if (codeBlockId != -1) {
                    InitRemoteSessionData initRemoteSessionData = fetchInitRemoteSessionData(codeBlockId, sessionId);
                    this.messagingTemplate.convertAndSend(destination, initRemoteSessionData);
                }
            }

        }
    }

    private InitRemoteSessionData fetchInitRemoteSessionData(int codeBlockId, String sessionId) {
        return remoteSessionService.getInitRemoteSessionData(codeBlockId, sessionId);
    }

    private int extractCodeBlockId(String destination) {
        try {
            String[] parts = destination.split("/");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            log.error("Invalid codeBlockId in destination URL: {}", destination, e);
            return -1;
        }
    }
}
