package com.example.IqsikNolik;

import com.example.IqsikNolik.config.SocketIOConfig;
import com.example.IqsikNolik.domain.Board;
import com.example.IqsikNolik.domain.Game;
import com.example.IqsikNolik.repository.GameStateRepository;
import com.example.IqsikNolik.service.GameStateService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static com.example.IqsikNolik.domain.Symbol.X;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "socket.enabled=false")
@ActiveProfiles("test")
@Transactional
public class GameServiceIT {

    @Autowired
    private GameStateService service;

    @Autowired
    private GameStateRepository repo;

    private Long gameStateId;

    @BeforeEach
    void setup() {
        Game game = getBasicGameState();
        Board board = new Board();
        board.setBoardIndex(1);
        board.setBoard("NXNNNNNNN");
        game.addBoardState(board);
        gameStateId = repo.save(game).getId();
    }

    @Test
    void shouldNotGetValidated() {

        assertThrows(IllegalArgumentException.class,
                () -> service.putSymbolOnBoard(gameStateId, 1));
    }

    @Test
    void shouldTestWholeFlow() {
        Game game = service.addNewGame();
        long gameId = game.getId();
        service.putSymbolOnBoard(gameId, 0);
        Game updatedGame = repo.findById(gameId).get();
        assertEquals("XNNNNNNNN", updatedGame.getBoards().get(1).getBoard());
        service.putSymbolOnBoard(gameId, 3);
        assertEquals("XNNONNNNN", updatedGame.getBoards().get(2).getBoard());
        service.putSymbolOnBoard(gameId, 1);
        assertEquals("XXNONNNNN", updatedGame.getBoards().get(3).getBoard());
        service.putSymbolOnBoard(gameId, 4);
        assertEquals("XXNOONNNN", updatedGame.getBoards().get(4).getBoard());
        service.putSymbolOnBoard(gameId, 2);
        assertEquals("XXXOONNNN", updatedGame.getBoards().get(5).getBoard());
        assertTrue(game.isFinished());
        assertEquals(X, game.getWinner());
        assertThrows(IllegalArgumentException.class, () -> service.putSymbolOnBoard(gameId, 5));
    }

    private static Game getBasicGameState() {
        Game game = new Game();
        Board board = new Board();
        board.setBoardIndex(0);
        game.addBoardState(board);
        return game;
    }
}
