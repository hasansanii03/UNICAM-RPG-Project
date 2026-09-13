package it.unicam.cs.mpgc.rpg125944.model.persistence;

import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;

import java.util.Optional;

/** Persists complete game sessions independently of the storage technology. */
public interface GameRepository {
    void save(GameSession session);

    Optional<GameSession> load(EnemyProvider enemyProvider);
}
