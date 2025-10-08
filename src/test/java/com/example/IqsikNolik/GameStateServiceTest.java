package com.example.IqsikNolik;

import com.example.IqsikNolik.domain.GameState;
import com.example.IqsikNolik.domain.Symbol;
import com.example.IqsikNolik.repository.GameStateRepository;
import com.example.IqsikNolik.service.GameStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GameStateServiceTest {
    @Mock
    private GameStateRepository repo;

    @InjectMocks
    private GameStateService service;

    private GameState almostFinishedBoard;

    /*@BeforeEach
    public void setup() {
        almostFinishedBoard = new GameState(10L, "X0NNX0NNN", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(almostFinishedBoard));
    }*/

    @Test
    public void shouldFinishWithCorrectWinner() {
        almostFinishedBoard = new GameState(10L, "XONNXONNN", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(almostFinishedBoard));

        String newStr = service.putSymbolOnBoard(Symbol.X, 8, 10L);
        Symbol winner1 = almostFinishedBoard.getWinner();

        assertEquals(Symbol.X, winner1);

    }

    @Test
    public void shouldFinishAsDraw() {
        almostFinishedBoard = new GameState(10L, "XOXOXNOXO", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(almostFinishedBoard));

        String newStr = service.putSymbolOnBoard(Symbol.X, 5, 10L);
        Symbol winner1 = almostFinishedBoard.getWinner();

        assertEquals(Symbol.N, winner1);
    }

    @Test
    public void shouldPutXOnBoard() {
        GameState emptyBoard = new GameState(10L, "NNNNNNNNN", 2, 2, false, Symbol.N);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(emptyBoard));

        String updatedBoardState = service.putSymbolOnBoard(Symbol.X, 4, 10L);
        char char1 = updatedBoardState.charAt(4);

        assertEquals('X', char1);

    }




}
