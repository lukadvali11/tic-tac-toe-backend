package com.example.IqsikNolik.repository;

import com.example.IqsikNolik.domain.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameStateRepository extends JpaRepository<GameState, Long> {

    @Query("SELECT gs.boardState from GameState gs where gs.id = :id")
    String findGameBoardById(@Param("id") Long id);



}
