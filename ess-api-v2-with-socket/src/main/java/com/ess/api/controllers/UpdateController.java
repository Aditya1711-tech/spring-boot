package com.ess.api.controllers;

import com.ess.api.request.UpdateRequest;
import com.ess.api.socketConfig.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UpdateController {

    private final WebSocketHandler webSocketHandler;

    @Autowired
    public UpdateController(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/update-socket")
    public ResponseEntity<String> sendUpdate(@RequestBody UpdateRequest updateRequest) {
        String message = updateRequest.getCount() + " Data has been updated!";
        webSocketHandler.broadcast("{\"message\": \"" + message + "\"}");
        webSocketHandler.sendMessageToUsers(new ArrayList<>(List.of("fenil@gmail.com", "rohan@gmail.com")), "This message is only for you");
        return ResponseEntity.ok("Update notification sent to all clients.");
    }
}
