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
public class StompSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
    private final SimpMessageSendingOperations messagingTemplate;
    private final RemoteSessionService remoteSessionService;

    public StompSubscribeEventListener(SimpMessageSendingOperations messagingTemplate, RemoteSessionService remoteSessionService) {
        this.messagingTemplate = messagingTemplate;
        this.remoteSessionService = remoteSessionService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        assert command != null;
        if (command.equals(StompCommand.SUBSCRIBE)) {
            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/topic/remoteSessionDetails/")) {
                // Extracting the codeBlockId from the URL
                String[] parts = destination.split("/");
                if (parts.length > 3) {
                    String codeBlockIdStr = parts[3];
                    try {
                        int codeBlockId = Integer.parseInt(codeBlockIdStr);

                        InitRemoteSessionData initRemoteSessionData = fetchInitRemoteSessionData(codeBlockId);

                        this.messagingTemplate.convertAndSend(destination, initRemoteSessionData);

                    } catch (NumberFormatException e) {
                        System.err.println("Invalid codeBlockId: " + codeBlockIdStr);
                    }
                }
            }
        }
    }

    private InitRemoteSessionData fetchInitRemoteSessionData(int codeBlockId) {
        InitRemoteSessionData initRemoteSessionData = remoteSessionService.getInitRemoteSessionData(codeBlockId);
        log.debug("InitRemoteSessionData details : {}", initRemoteSessionData);

        return initRemoteSessionData;
    }
}