package com.takeaway.gameofthree.repository;

import com.takeaway.gameofthree.model.Game;
import com.takeaway.gameofthree.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    Optional<Game> findTopByGameStatusOrderByIdDesc(GameStatus gameStatus);
}
