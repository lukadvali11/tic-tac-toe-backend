package com.example.IqsikNolik.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameState {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    private String boardState;

    private int numberOfX;

    private int numberOfO;

    private boolean finished;

    @Enumerated(STRING)
    private Symbol winner = Symbol.N;
}
