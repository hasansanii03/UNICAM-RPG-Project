package it.unicam.cs.mpgc.rpg125944.controller;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;

import java.util.Objects;

/** Application boundary used by user interfaces to control a game session. */
public class GameController {
    private final EnemyProvider enemyProvider;
    private final int totalWaves;
    private final int availablePotions;
    private GameSession session;

    public GameController(EnemyProvider enemyProvider, int totalWaves, int availablePotions) {
        this.enemyProvider = Objects.requireNonNull(enemyProvider, "enemyProvider must not be null");
        this.totalWaves = totalWaves;
        this.availablePotions = availablePotions;
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

    public GameSession getSession() {
        if (session == null) {
            throw new IllegalStateException("start a new game first");
        }
        return session;
    }
}
