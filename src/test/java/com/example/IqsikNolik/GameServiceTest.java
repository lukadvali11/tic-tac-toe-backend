package com.example.IqsikNolik;

import com.example.IqsikNolik.domain.Board;
import com.example.IqsikNolik.domain.Game;
import com.example.IqsikNolik.repository.GameStateRepository;
import com.example.IqsikNolik.service.GameStateService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.IqsikNolik.domain.Symbol.X;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    GameStateRepository repo;

    @InjectMocks
    GameStateService service;

    @Test
    public void shouldPutSymbolOnEmptyBoard() {
        Game game = getBasicGameState();
        when(repo.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(game));
        when(repo.save(any(Game.class))).thenAnswer(i -> i.getArgument(0));

        Game result = service.putSymbolOnBoard(10L, 4);
        assertEquals("NNNNXNNNN",result.getBoards().get(1).getBoard());
    }


    @ParameterizedTest
    @MethodSource("provideArguments")
    void shouldThrowAnException(Class<? extends RuntimeException> exception, Game game) {

        when(repo.findById(10L)).thenReturn(Optional.ofNullable(game));
        assertThrows(exception,
                () -> service.putSymbolOnBoard(10L, 5));
    }

    @Test
    void shouldThrowIllegalArgException() {
        Game game = getBasicGameState();
        Board board = new Board();
        board.setBoardIndex(1);
        board.setBoard("XXXONONNN");
        game.addBoardState(board);
        game.setFinished(true);
        game.setWinner(X);
        when(repo.findById(10L)).thenReturn(java.util.Optional.of(game));
        assertThrows(IllegalArgumentException.class,
                () -> service.putSymbolOnBoard(10L, 4));
    }
    @Test
    void shouldNotGetValidated() {
        Game game = getBasicGameState();
        Board board = new Board();
        board.setBoardIndex(1);
        board.setBoard("NXNNNNNNN");
        game.addBoardState(board);

        when(repo.findById(10L)).thenReturn(Optional.of(game));
        assertThrows(IllegalArgumentException.class,
                () -> service.putSymbolOnBoard(10L, 1));
    }

    private static Game getBasicGameState() {
        Game game = new Game();
        Board board = new Board();
        board.setBoard("NNNNNNNNN");
        board.setBoardIndex(0);
        board.setGame(game);
        game.setId(10L);
        game.addBoardState(board);
        return game;
    }

    public static Stream<Arguments> provideArguments() {
        Game game = getBasicGameState();
        game.setFinished(true);

        return Stream.of(
                Arguments.of(EntityNotFoundException.class, null),
                Arguments.of(IllegalArgumentException.class, game)
        );
    }

}
