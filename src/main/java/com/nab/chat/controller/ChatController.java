package com.nab.chat.controller;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nab.chat.domain.ChatMessage;
import com.nab.chat.domain.User;

@Controller
public class ChatController {

	@MessageMapping("/send")
	@SendTo("/topic/chat")    
    public ChatMessage sendMessage(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		chatMessage.setName(headerAccessor.getSessionAttributes().get("username").toString());
        return chatMessage;
    }
	
	@GetMapping("/")
	public String home(Model model, HttpSession httpSession) {
		String username = (String) httpSession.getAttribute("username");
		if(username!=null) {
			return "redirect:/chat";
		}
		model.addAttribute("user", new User());
		return "login"; 
	}
	
	@PostMapping("/")
	public String homePost(@ModelAttribute User user, HttpSession httpSession) {
		if(!user.getUsername().isEmpty()) {
			httpSession.setAttribute("username", user.getUsername());
			return "redirect:/chat";
		}else {
			return "login";
		}
	}
	
	@GetMapping("/chat")
	public String chat(HttpSession httpSession) {
		String username = (String) httpSession.getAttribute("username");
		if(username!=null) {
			return "chat"; 	
		}else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/logout")
	public String logout(HttpSession httpSession) {
		String username = (String) httpSession.getAttribute("username");
		if(username!=null) {
			httpSession.removeAttribute("username");
		}
		return "redirect:/";
	}
}
