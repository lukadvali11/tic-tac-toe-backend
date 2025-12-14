package com.example.IqsikNolik.controller;

import com.example.IqsikNolik.domain.Player;
import com.example.IqsikNolik.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/player")
public class PlayerController {

	private final PlayerService playerService;

	@PostMapping("/register")
	public Player register(@RequestParam("name") String name, @RequestParam("password") String password) {
		return playerService.registerPlayer(name, password);
	}
}
