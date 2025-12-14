package com.example.IqsikNolik.repository;

import com.example.IqsikNolik.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStateRepository extends JpaRepository<Game, Long> {
}
