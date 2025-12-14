package com.example.IqsikNolik.controller;

import com.example.IqsikNolik.domain.Game;
import com.example.IqsikNolik.service.GameStateService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GameStateController {

    private final GameStateService gameStateService;

    @GetMapping("/game/{id}")
    public Game getGame(@PathVariable long id) {
        return gameStateService.findGame(id);
    }

    @GetMapping("/game/{id}/move-number/{moveNum}")
    public String getBoard(@PathVariable long id,
                           @PathVariable @Min(0) @Max(9) int moveNum) {
        return gameStateService.getBoard(id, moveNum);
    }

    @PutMapping("/game/{gameStateId}")
    public Game putSymbol(@PathVariable Long gameStateId,
                          @RequestParam("position") @Min(0) @Max(8) int position) {
        return gameStateService.putSymbolOnBoard(gameStateId, position );
    }

    @PostMapping("/game")
    public Game createGame() {

        return gameStateService.addNewGame();
    }

}
