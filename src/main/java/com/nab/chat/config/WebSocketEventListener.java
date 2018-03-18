package com.nab.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.nab.chat.domain.ChatMessage;
import com.nab.chat.domain.MessageType;

@Component
public class WebSocketEventListener {
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setMessageType(MessageType.LEAVE);
			chatMessage.setName(username);

			messagingTemplate.convertAndSend("/topic/chat", chatMessage);
		}
	}
}
