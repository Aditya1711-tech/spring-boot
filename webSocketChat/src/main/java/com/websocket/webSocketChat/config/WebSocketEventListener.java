package com.websocket.webSocketChat.config;

import com.websocket.webSocketChat.entity.ChatMessage;
import com.websocket.webSocketChat.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations template;

    @EventListener
    public void HandleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = (String)accessor.getSessionAttributes().get("userName");
        if(userName != null){
            log.info("User Disconnected: {}", userName);
            ChatMessage chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE).sender(userName).build();
            template.convertAndSend("/topic/public", chatMessage);
        }
    }
}
