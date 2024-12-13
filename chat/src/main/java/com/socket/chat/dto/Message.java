package com.socket.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Message {
    private String senderName;
    private String targetUserName;
    private String message;
}
