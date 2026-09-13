package it.unicam.cs.mpgc.rpg125944.model.game;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.Battle;
import it.unicam.cs.mpgc.rpg125944.model.combat.BattleState;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;

import java.util.Objects;

/** Owns the state and valid transitions of one game session. */
public class GameSession {
    private final Character hero;
    private final EnemyProvider enemyProvider;
    private final int totalWaves;
    private int currentWave;
    private int remainingPotions;
    private Battle currentBattle;
    private GameState state;

    public GameSession(Character hero, EnemyProvider enemyProvider, int totalWaves, int availablePotions) {
        this.hero = Objects.requireNonNull(hero, "hero must not be null");
        this.enemyProvider = Objects.requireNonNull(enemyProvider, "enemyProvider must not be null");
        if (totalWaves <= 0) {
            throw new IllegalArgumentException("totalWaves must be positive");
        }
        if (availablePotions < 0) {
            throw new IllegalArgumentException("availablePotions must not be negative");
        }
        this.totalWaves = totalWaves;
        this.remainingPotions = availablePotions;
        this.currentWave = 1;
        this.state = GameState.IN_PROGRESS;
        createBattle();
    }

    public TurnResult executePlayerAction(PlayerAction action) {
        if (state != GameState.IN_PROGRESS) {
            throw new IllegalStateException("no action is allowed in the current game state");
        }

        TurnResult result = currentBattle.executeTurn(action);
        remainingPotions = result.remainingPotions();
        if (result.battleState() == BattleState.HERO_LOST) {
            state = GameState.DEFEAT;
        } else if (result.battleState() == BattleState.HERO_WON) {
            state = currentWave == totalWaves ? GameState.VICTORY : GameState.WAVE_WON;
        }
        return result;
    }

    public void startNextWave() {
        if (state != GameState.WAVE_WON) {
            throw new IllegalStateException("the next wave is not available");
        }
        currentWave++;
        state = GameState.IN_PROGRESS;
        createBattle();
    }

    public Character getHero() {
        return hero;
    }

    public Battle getCurrentBattle() {
        return currentBattle;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getTotalWaves() {
        return totalWaves;
    }

    public int getRemainingPotions() {
        return remainingPotions;
    }

    public GameState getState() {
        return state;
    }

    private void createBattle() {
        currentBattle = new Battle(hero, enemyProvider.createEnemy(currentWave), remainingPotions);
    }
}
