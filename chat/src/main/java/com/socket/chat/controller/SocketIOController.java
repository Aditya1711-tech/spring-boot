package com.socket.chat.controller;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.socket.chat.dto.Message;
import com.sun.jdi.ArrayReference;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
@Log4j2
public class SocketIOController {

    @Autowired
    private SocketIOServer socketServer;

    private static final Map<String, List<Message>> pendingMessages = new LinkedHashMap<>();
    private static final Map<String, SocketIOClient> connectedClients = new LinkedHashMap<>();

    SocketIOController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * Here we create only one event listener
         * but we can create any number of listener
         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
         */
        this.socketServer.addEventListener("messageSendToUser", Message.class, onSendMessage);

    }


    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            log.info("Perform operation on user connect in controller");
            String userName = client.getHandshakeData().getHttpHeaders().get("userName");
            System.out.println(client.getNamespace().getName());

            connectedClients.put(userName, client);

            if(pendingMessages.containsKey(userName)){
                List<Message> messages = pendingMessages.get(userName);
                for(Message message : messages){
                    socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);
                }
                pendingMessages.remove(userName);
                log.info("SENT ALL THE PENDING MESSAGES TO USER {}", userName);
            }else{
                log.info("THERE ARE NO PENDING MESSAGES FOR USER {}", userName);
            }
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("Perform operation on user disconnect in controller");
            connectedClients.remove(client);
        }
    };

    public DataListener<Message> onSendMessage = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {

            /**
             * Sending message to target user
             * target user should subscribe the socket event with his/her name.
             * Send the same payload to user
             */
            if(connectedClients.containsKey(message.getTargetUserName())){
                log.info(message.getSenderName()+" user send message to user "+message.getTargetUserName()+" and message is "+message.getMessage());
                socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);


                /**
                 * After sending message to target user we can send acknowledge to sender
                 */
                acknowledge.sendAckData("Message send to target user successfully");
            }else{
                if(pendingMessages.containsKey(message.getTargetUserName())){
                    List<Message> existingMessages = pendingMessages.get(message.getTargetUserName());
                    existingMessages.add(message);
                    pendingMessages.put(message.getTargetUserName(), existingMessages);
                }else{
                    List<Message> messages = new ArrayList<>();
                    messages.add(message);
                    pendingMessages.put(message.getTargetUserName(), messages);
                }
                acknowledge.sendAckData("Message queued successfully");
            }
        }
    };

}
