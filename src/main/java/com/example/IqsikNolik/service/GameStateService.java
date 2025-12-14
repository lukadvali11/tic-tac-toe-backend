package com.example.IqsikNolik.service;

import com.example.IqsikNolik.domain.Board;
import com.example.IqsikNolik.domain.Game;
import com.example.IqsikNolik.domain.Player;
import com.example.IqsikNolik.domain.Symbol;
import com.example.IqsikNolik.repository.GameStateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.IqsikNolik.domain.Symbol.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameStateService {

    private final GameStateRepository gameStateRepository;
    private final PlayerService playerService;

    @Transactional
    public Game addNewGame(Long playerId) {
        Player player = playerService.findPlayerById(playerId);
        Game game = new Game();
        Board board = new Board();
        game.addBoardState(board);
        game.addPlayerX(player);
        return gameStateRepository.save(game);
    }

    public Game addNewGame() {
        Game game = new Game();
        Board board = new Board();
        game.addBoardState(board);
        return gameStateRepository.save(game);
    }

    @Transactional
    public Game joinGame(Long gameId, Long playerId) {
        Player player = playerService.findPlayerById(playerId);
        Game game = findGame(gameId);
        if (game.getPlayerX() == null) {
            game.addPlayerX(player);
        } else if (game.getPlayerO() == null) {
            game.addPlayerO(player);
        } else {
            log.warn("Could not join player [{}] to game [{}], as the game is already in progress",
                    player.getId(), game.getId());
        }
        gameStateRepository.save(game);
        return game;
    }

    public String getBoard(Long id, int moveNum) {
        Game game = gameStateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GameState not found with id: " + id));
        if (game.getBoards().size() <= moveNum) {
            throw new IllegalArgumentException("moveNumber [%d] is incorrect".formatted(moveNum));
        }
        return game.getBoards().get(moveNum).getBoard();
    }

    public Game putSymbolOnBoard(Long gameStateId, int position) {
        Game game = gameStateRepository.findById(gameStateId)
                .orElseThrow(() -> new EntityNotFoundException("GameState not found with id: " + gameStateId));
        if (game.isFinished()) {
            throw new IllegalArgumentException("Game is already finished");
        }
        Board board = game.getBoards().getLast();

        Symbol symbol = board.getBoardIndex() % 2 == 0 ? X : O;
        validate(board, position);
        Board nextBoard = new Board();
        nextBoard.setBoardIndex(board.getBoardIndex() + 1);

        StringBuilder sb = new StringBuilder(board.getBoard());
        if (symbol == X) {
            sb.setCharAt(position, 'X');
        } else {
            sb.setCharAt(position, 'O');
        }
        String newStr = sb.toString();
        nextBoard.setBoard(newStr);

        game.addBoardState(nextBoard);

        if (isFinished(nextBoard.getBoard())) {
            game.setFinished(true);
            game.setWinner(getWinner(nextBoard.getBoard()));
        }
        return gameStateRepository.save(game);
    }

    public Game findGame(long id) {
        return gameStateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GameState not found with id: " + id));
    }

    private static void validate(Board boardState, int position) {
        String board = boardState.getBoard();
        if (board.charAt(position) != 'N') {
            throw new IllegalArgumentException("Position [%s] is  already occupied".formatted(position));
        }
        if (boardState.getGame().isFinished()) {
            throw new IllegalArgumentException("game is finished");
        }
    }

    private static boolean isFinished(String board) {
        if (getWinner(board) != N) {
            return true;
        } else {
            return !board.contains("N");
        }
    }

    public static Symbol getWinner(String board) {
        Symbol winningSymbol = checkColumnsForWinner(board);
        if (winningSymbol != N) return winningSymbol;
        winningSymbol = checkRowsForWinner(board);
        if (winningSymbol != N) return winningSymbol;
        winningSymbol = checkDiagonalsForWinner(board);
        if (winningSymbol != N) return winningSymbol;
        return N;
    }

    private static Symbol checkColumnsForWinner(String board) {
        for (int i = 0; i <= 2; i++) {
            if (board.charAt(i) == 'N') continue;
            if (board.charAt(i) == board.charAt(i+3) && board.charAt(i) == board.charAt(i + 6)) {
                return getSymbol(board.charAt(i));
            }
        }
        return N;
    }

    private static Symbol checkRowsForWinner(String board) {
        for (int i = 0; i <= 2; i++) {
            if (board.charAt(3*i) == 'N') continue;
            if (board.charAt(3*i) == board.charAt(3*i + 1) && board.charAt(3*i + 1) == board.charAt(3*i + 2)) {
                return getSymbol(board.charAt(3*i));
            }
        }
        return N;
    }

    private static Symbol checkDiagonalsForWinner(String board) {
        if (board.charAt(0) != 'N' && board.charAt(0) == board.charAt(4) && board.charAt(4) == board.charAt(8)) {
            return getSymbol(board.charAt(0));
        }
        if (board.charAt(2) != 'N' && board.charAt(2) == board.charAt(4) && board.charAt(4) == board.charAt(6)) {
            return getSymbol(board.charAt(2));
        }
        return N;
    }
}
