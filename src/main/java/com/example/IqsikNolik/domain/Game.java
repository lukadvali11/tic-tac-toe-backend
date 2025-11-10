package com.example.IqsikNolik.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Game {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    private boolean finished;

    @Enumerated(STRING)
    private Symbol winner = Symbol.N;

    public void addBoardState(Board board) {
        boards.add(board);
        board.setGame(this);
    }

    public void removeBoardStatesAfterIndex(int moveNumber) {
        boards.stream().skip(moveNumber).forEach(bs -> bs.setGame(null));
        boards.subList(moveNumber, boards.size()).clear();
    }
}
