package com.nab.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.nab.chat.domain.ChatMessage;

@Controller
public class ChatController {

	@MessageMapping("/send")
	@SendTo("/chatroom")    
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessage;
    }
	
	@GetMapping("/")
	public String home() {
		return "login"; 
	}
	
	@GetMapping("/chat")
	public String chat() {
		return "chat"; 
	}
}
