package com.example.IqsikNolik.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.IqsikNolik.domain.Game;
import com.example.IqsikNolik.domain.Player;
import com.example.IqsikNolik.service.GameStateService;
import com.example.IqsikNolik.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "tic-tac-toe.socket.enabled", havingValue = "true")
public class GameHandler {

	private final SocketIOServer server;
	private final GameStateService gameStateService;
	private final PlayerService playerService;

	public GameHandler(SocketIOServer server, GameStateService gameStateService, PlayerService playerService) {
		this.server = server;
		this.gameStateService = gameStateService;
		this.playerService = playerService;
		server.addListeners(this);
		server.start();
	}

	@OnConnect
	public void onConnect(SocketIOClient client) {
		log.info("Client connected, sessionId: [{}]", client.getSessionId());
	}

	@OnEvent("create-game")
	public void onCreateGame(SocketIOClient client, Long playerId) {
		log.info("Create game event received, sessionId: [{}], playerId: [{}]", client.getSessionId(), playerId);
		Game game = gameStateService.addNewGame(playerId);
		Long gameId = game.getId();

		client.joinRoom(gameId.toString());
		client.sendEvent("game-created", gameId);
	}

	@OnEvent("join-game")
	public void onJoinGame(SocketIOClient client, Long gameId, Long playerId) {
		log.info("Join game event received, sessionId: [{}], playerId: [{}]", client.getSessionId(), playerId);
		Game game = gameStateService.joinGame(gameId, playerId);

		String gameIdString = game.getId().toString();
		client.joinRoom(gameIdString);
		client.sendEvent("game-joined", gameIdString);

		var clients = server.getRoomOperations(gameIdString).getClients();
		if (clients.size() == 2) {
			clients.forEach(cl -> cl.sendEvent("game-started", game));
		}
	}

	@OnEvent("make-move")
	public void onMakeMove(SocketIOClient client, Long gameId, Integer position) {
		log.info("Make move event received, sessionId: [{}], gameId: [{}], position: [{}]",
				client.getSessionId(), gameId, position);
		Game game = gameStateService.putSymbolOnBoard(gameId, position);
		String gameIdString = game.getId().toString();
		var clients = server.getRoomOperations(gameIdString).getClients();
		clients.forEach(cl -> cl.sendEvent("move-made", game));
	}
}
