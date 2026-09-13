package it.unicam.cs.mpgc.rpg125944.controller;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.persistence.GameRepository;

import java.util.Objects;

/** Punto di accesso applicativo usato dalle interfacce per controllare una partita. */
public class GameController {
    private final EnemyProvider enemyProvider;
    private final int totalWaves;
    private final int availablePotions;
    private final GameRepository gameRepository;
    private GameSession session;

    public GameController(
            EnemyProvider enemyProvider,
            int totalWaves,
            int availablePotions,
            GameRepository gameRepository
    ) {
        this.enemyProvider = Objects.requireNonNull(enemyProvider, "enemyProvider non puo' essere null");
        this.totalWaves = totalWaves;
        this.availablePotions = availablePotions;
        this.gameRepository = Objects.requireNonNull(gameRepository, "gameRepository non puo' essere null");
    }

    public void startNewGame(Character hero) {
        session = new GameSession(hero, enemyProvider, totalWaves, availablePotions);
    }

    public TurnResult submitAction(PlayerAction action) {
        return getSession().executePlayerAction(action);
    }

    public void startNextWave() {
        getSession().startNextWave();
    }

    public void saveGame() {
        gameRepository.save(getSession());
    }

    public void loadGame() {
        session = gameRepository.load(enemyProvider)
                .orElseThrow(() -> new IllegalStateException("nessuna partita salvata disponibile"));
    }

    public GameSession getSession() {
        if (session == null) {
            throw new IllegalStateException("avvia prima una nuova partita");
        }
        return session;
    }
}
