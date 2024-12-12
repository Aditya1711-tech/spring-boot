package com.ess.api.socketConfig;

import com.ess.api.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private JwtUtils jwtUtils;

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, String> sessionsForUser = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, WebSocketSession> sessionsForSessionId = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String token = null;

        if (query != null && query.contains("token=")) {
            token = query.split("token=")[1];
        }

        if (token == null || token.isEmpty() || !isValidToken(token)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing token"));
            return;
        }

        String email = jwtUtils.getEmailFromJwtToken(token);
        sessions.add(session);
        sessionsForUser.put(email, session.getId());
        sessionsForSessionId.put(session.getId(), session);
        session.sendMessage(new TextMessage("{\"message\": \"Welcome to the WebSocket server!\"}"));
        System.out.println("Client: " + email + " connected with session id: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);

        String email = sessionsForUser.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(session.getId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (email != null) {
            sessionsForUser.remove(email);
        }
        sessionsForSessionId.remove(session.getId());

        System.out.println("Client disconnected: " + session.getId());
    }

    public void broadcast(String message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    System.err.println("Failed to send broadcast message to session: " + session.getId());
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessageToUsers(List<String> userEmails, String message) {
        synchronized (userEmails){
            for (String userEmail : userEmails) {
                String sessionId = sessionsForUser.get(userEmail);
                WebSocketSession session = sessionsForSessionId.get(sessionId);

                if (session != null && session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage("{\"message\":\"" + message + "\",\"user\":\"" + userEmail + "\"}"));
                    } catch (IOException e) {
                        System.err.println("Failed to send message to user: " + userEmail);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No active session for user: " + userEmail);
                }
            }
        }
    }

    private boolean isValidToken(String token) {
        return jwtUtils.validateJwtToken(token); // Implement actual validation logic
    }
}

