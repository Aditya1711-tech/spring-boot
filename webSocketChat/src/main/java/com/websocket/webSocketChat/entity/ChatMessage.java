package com.websocket.webSocketChat.entity;

import com.websocket.webSocketChat.enums.MessageType;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String message;
    private String sender;
    private MessageType messageType;
}
