package com.example.IqsikNolik;

import com.example.IqsikNolik.domain.GameState;
import com.example.IqsikNolik.domain.Symbol;
import com.example.IqsikNolik.repository.GameStateRepository;
import com.example.IqsikNolik.service.GameStateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameStateServiceTest {
    @Mock
    private GameStateRepository repo;

    @InjectMocks
    private GameStateService service;

    private GameState almostFinishedBoard;

    @Test
    public void shouldFinishWithCorrectWinner() {
        almostFinishedBoard = new GameState(10L, "XONNXONNN", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(almostFinishedBoard));

        GameState gameState = service.putSymbolOnBoard(Symbol.X, 8, 10L);

        assertEquals(Symbol.X, gameState.getWinner());
    }

    @Test
    public void shouldFinishAsDraw() {
        almostFinishedBoard = new GameState(10L, "XOXOXNOXO", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(almostFinishedBoard));

        GameState gameState = service.putSymbolOnBoard(Symbol.X, 5, 10L);

        assertTrue(gameState.isFinished());
        assertEquals(Symbol.N, gameState.getWinner());
    }

    @Test
    public void shouldPutXOnBoard() {
        GameState emptyBoard = new GameState(10L, "NNNNNNNNN", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(emptyBoard));

        GameState gameState = service.putSymbolOnBoard(Symbol.X, 4, 10L);

        assertEquals('X', gameState.getBoardState().charAt(4));
    }
}
