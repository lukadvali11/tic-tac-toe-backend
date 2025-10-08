package com.example.IqsikNolik.service;

import com.example.IqsikNolik.domain.GameState;
import com.example.IqsikNolik.domain.Symbol;
import com.example.IqsikNolik.repository.GameStateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.IqsikNolik.domain.Symbol.*;

@Service
@RequiredArgsConstructor
public class GameStateService {

    private final GameState gameState;
    private final GameStateRepository gameStateRepository;

    public String getBoard(Long id) {
        return gameStateRepository.findGameBoardById(id);
    }

    public String putSymbolOnBoard(Symbol symbol, int position, Long boardId) {
        GameState gameState = gameStateRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("GameState not found with id: " + boardId));
        validate(gameState, symbol, position);
        StringBuilder sb = new StringBuilder(gameState.getBoardState());
        if (symbol == X) {
            sb.setCharAt(position, 'X');
        } else if(symbol == O) {
            sb.setCharAt(position, 'O');
        }
        String newStr = sb.toString();
        gameState.setBoardState(newStr);

        if (isFinished(gameState.getBoardState())) {
            gameState.setFinished(true);
            gameState.setWinner(getWinner(gameState.getBoardState()));
        }

        return gameState.getBoardState();
    }


    public GameState addNewBoard() {
        gameState.setBoardState("NNNNNNNNN");
        gameState.setNumberOfO(0);
        gameState.setNumberOfX(0);
        gameState.setFinished(false);
        gameState.setWinner(N);
        gameStateRepository.save(gameState);
        return gameState;
    }

    private void validate(GameState gameState, Symbol symbol, int position) {
        String board = gameState.getBoardState();
        if (symbol == N) {
            throw new IllegalArgumentException("incorrect symbol passed");
        }
        if (board.charAt(position) != 'N') {
            throw new IllegalArgumentException("Position [%s] is  already occupied".formatted(position));
        }
        if (symbol == X && gameState.getNumberOfX() - gameState.getNumberOfO() != 0
                || symbol == O && gameState.getNumberOfX() - gameState.getNumberOfO() != 1) {
            throw new IllegalArgumentException("incorrect symbol passed");
        }
        if (isFinished(gameState.getBoardState())) {
            throw new IllegalArgumentException("incorrect symbol passed");
        }
    }

    private boolean isFinished(String board) {
        if (getWinner(board) != N) {
            return true;
        } else {
            return !board.contains("N");
        }
    }

    private Symbol getWinner(String board) {
        Symbol sym1 = checkColumnsForWinner(board);
        Symbol sym2 = checkRowsForWinner(board);
        Symbol sym3 = checkDiagonalsForWinner(board);
        Symbol sym4 = N;

        if (sym1 != N) {
            sym4 = sym1;
        } else if (sym2 != N) {
            sym4 = sym2;
        } else if (sym3 != N) {
            sym4 = sym3;
        }
        return sym4;
    }

    private Symbol checkColumnsForWinner(String board) {
        Symbol symbol = N;
        char char1 = 'N';
        for (int i = 0; i <= 2; i++) {
            if (board.charAt(i) == board.charAt(i+3) && board.charAt(i) == board.charAt(i + 6)) {
                char1 = board.charAt(i);
            }
        }
        if (char1 == 'X') {
            symbol = X;
        } else if (char1 == 'O') {
            symbol = O;
        }
        return symbol;
    }

    private Symbol checkRowsForWinner(String board) {
        Symbol symbol = N;
        char char1 = 'N';
        for (int i = 0; i <= 6; i += 3) {
            if (board.charAt(i) == board.charAt(i+1) && board.charAt(i) == board.charAt(i + 2)) {
                char1 = board.charAt(i);
            }
        }
        if (char1 == 'X') {
            symbol = X;
        } else if (char1 == 'O') {
            symbol = O;
        }
        return symbol;
    }

    private Symbol checkDiagonalsForWinner(String board) {
        Symbol symbol = N;
        char char1 = 'N';
        for (int i = 0; i <= 2; i += 2) {
            char char2 = board.charAt(i);
            if (i == 0 && char2 == board.charAt(i + 4) && char2 == board.charAt(i + 8)) {
                char1 = char2;
            }
            if (char2 == board.charAt(i + 2) && char2 == board.charAt(i + 4)) {
                char1 = char2;
            }
        }
        if (char1 == 'X') {
            symbol = X;
        } else if (char1 == 'O') {
            symbol = O;
        }
        return symbol;
    }
}
