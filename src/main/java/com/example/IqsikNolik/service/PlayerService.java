package com.example.IqsikNolik.service;

import com.example.IqsikNolik.domain.Player;
import com.example.IqsikNolik.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

	private final PlayerRepository playerRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public Player findPlayerById(Long id) {
		return playerRepository.findById(id).orElse(null);
	}

	public Player registerPlayer(String name, String password) {
		Player player = new Player();
		player.setName(name);
		player.setHashedPassword(bCryptPasswordEncoder.encode(password));
		return playerRepository.save(player);
	}
}

