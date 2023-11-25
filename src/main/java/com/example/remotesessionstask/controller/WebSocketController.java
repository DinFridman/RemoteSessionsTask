package com.example.remotesessionstask.controller;

import com.example.remotesessionstask.dto.RemoteSessionDTO;
import com.example.remotesessionstask.request.CodeUpdateRequest;
import com.example.remotesessionstask.request.GetRoleRequest;
import com.example.remotesessionstask.response.RoleResponse;
import com.example.remotesessionstask.service.RemoteSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RemoteSessionService remoteSessionService;


    @MessageMapping("/getRole")
    @SendTo("/topic/roles")
    public RoleResponse getRole(GetRoleRequest getRoleRequest) {
       log.info("getRole request incoming! codeBlockId: " + getRoleRequest.codeBlockId());
        String role = remoteSessionService.getSessionRole(getRoleRequest.codeBlockId());
        return new RoleResponse(role);
    }

    @MessageMapping("/codeChange")
    @SendTo("/topic/code")
    public RemoteSessionDTO codeChange(CodeUpdateRequest request)  {
        log.info("codeChange request incoming! request details: " + request);
        return remoteSessionService.handleCodeChange(request);
    }

}
