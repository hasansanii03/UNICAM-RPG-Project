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
        this(hero, enemyProvider, totalWaves, availablePotions, 1, GameState.IN_PROGRESS);
        createBattle();
        synchronizeStateWithBattle();
    }

    private GameSession(
            Character hero,
            EnemyProvider enemyProvider,
            int totalWaves,
            int remainingPotions,
            int currentWave,
            GameState state
    ) {
        this.hero = Objects.requireNonNull(hero, "hero must not be null");
        this.enemyProvider = Objects.requireNonNull(enemyProvider, "enemyProvider must not be null");
        if (totalWaves <= 0) {
            throw new IllegalArgumentException("totalWaves must be positive");
        }
        if (remainingPotions < 0) {
            throw new IllegalArgumentException("remainingPotions must not be negative");
        }
        if (currentWave <= 0 || currentWave > totalWaves) {
            throw new IllegalArgumentException("currentWave must be within the game range");
        }
        this.totalWaves = totalWaves;
        this.remainingPotions = remainingPotions;
        this.currentWave = currentWave;
        this.state = Objects.requireNonNull(state, "state must not be null");
    }

    public static GameSession restore(
            Character hero,
            Character enemy,
            EnemyProvider enemyProvider,
            int totalWaves,
            int currentWave,
            int remainingPotions,
            int turnNumber,
            GameState state
    ) {
        GameSession session = new GameSession(
                hero, enemyProvider, totalWaves, remainingPotions, currentWave, state
        );
        session.currentBattle = new Battle(hero, Objects.requireNonNull(enemy, "enemy must not be null"),
                remainingPotions, turnNumber);
        session.validateRestoredState();
        return session;
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

    private void validateRestoredState() {
        BattleState battleState = currentBattle.getState();
        if (state == GameState.IN_PROGRESS && battleState != BattleState.IN_PROGRESS) {
            throw new IllegalArgumentException("an active game requires an active battle");
        }
        if ((state == GameState.WAVE_WON || state == GameState.VICTORY)
                && battleState != BattleState.HERO_WON) {
            throw new IllegalArgumentException("a won game state requires a defeated enemy");
        }
        if (state == GameState.DEFEAT && battleState != BattleState.HERO_LOST) {
            throw new IllegalArgumentException("a defeated game state requires a defeated hero");
        }
        if (state == GameState.WAVE_WON && currentWave >= totalWaves) {
            throw new IllegalArgumentException("the last wave must produce victory");
        }
        if (state == GameState.VICTORY && currentWave != totalWaves) {
            throw new IllegalArgumentException("victory requires the last wave");
        }
    }

    private void synchronizeStateWithBattle() {
        if (currentBattle.getState() == BattleState.HERO_LOST) {
            state = GameState.DEFEAT;
        } else if (currentBattle.getState() == BattleState.HERO_WON) {
            state = currentWave == totalWaves ? GameState.VICTORY : GameState.WAVE_WON;
        }
    }
}
