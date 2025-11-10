package com.example.IqsikNolik.repository;

import com.example.IqsikNolik.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameStateRepository extends JpaRepository<Game, Long> {

    @Query("SELECT gs.boards from Game gs where gs.id = :id")
    String findGameBoardById(@Param("id") Long id);




}
