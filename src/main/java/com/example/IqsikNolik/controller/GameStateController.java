package com.example.IqsikNolik.controller;

import com.example.IqsikNolik.domain.GameState;
import com.example.IqsikNolik.domain.Symbol;
import com.example.IqsikNolik.service.GameStateService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GameStateController {

    private final GameStateService gameStateService;

    @GetMapping("/board/{id}")
    public String getBoardState(@PathVariable Long id) {
        return gameStateService.getBoard(id);
    }

    @PutMapping("/board/{boardId}/put")
    public GameState putSymbol(@PathVariable Long boardId,
                            @RequestParam("symbol") Symbol symbol,
                            @RequestParam("position") @Min(0) @Max(8) int position) {
        return gameStateService.putSymbolOnBoard(symbol, position, boardId);
    }

    @PostMapping("/create-board")
    public GameState createBoard() {
        return gameStateService.addNewBoard();
    }

}
