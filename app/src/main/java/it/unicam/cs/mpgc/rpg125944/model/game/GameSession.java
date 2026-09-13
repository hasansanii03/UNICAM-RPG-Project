package it.unicam.cs.mpgc.rpg125944.model.game;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.Battle;
import it.unicam.cs.mpgc.rpg125944.model.combat.BattleState;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;

import java.util.Objects;

/** Possiede lo stato e le transizioni valide di una partita. */
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
        this.hero = Objects.requireNonNull(hero, "l'eroe non puo' essere null");
        this.enemyProvider = Objects.requireNonNull(enemyProvider, "enemyProvider non puo' essere null");
        if (totalWaves <= 0) {
            throw new IllegalArgumentException("il numero totale di ondate deve essere positivo");
        }
        if (remainingPotions < 0) {
            throw new IllegalArgumentException("le pozioni residue non possono essere negative");
        }
        if (currentWave <= 0 || currentWave > totalWaves) {
            throw new IllegalArgumentException("l'ondata corrente deve essere compresa nel range della partita");
        }
        this.totalWaves = totalWaves;
        this.remainingPotions = remainingPotions;
        this.currentWave = currentWave;
        this.state = Objects.requireNonNull(state, "lo stato non puo' essere null");
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
        session.currentBattle = new Battle(hero, Objects.requireNonNull(enemy, "il nemico non puo' essere null"),
                remainingPotions, turnNumber);
        session.validateRestoredState();
        return session;
    }

    public TurnResult executePlayerAction(PlayerAction action) {
        if (state != GameState.IN_PROGRESS) {
            throw new IllegalStateException("non e' possibile eseguire azioni nello stato corrente della partita");
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
            throw new IllegalStateException("la prossima ondata non e' disponibile");
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
            throw new IllegalArgumentException("una partita attiva richiede uno scontro attivo");
        }
        if ((state == GameState.WAVE_WON || state == GameState.VICTORY)
                && battleState != BattleState.HERO_WON) {
            throw new IllegalArgumentException("uno stato di vittoria richiede un nemico sconfitto");
        }
        if (state == GameState.DEFEAT && battleState != BattleState.HERO_LOST) {
            throw new IllegalArgumentException("uno stato di sconfitta richiede un eroe sconfitto");
        }
        if (state == GameState.WAVE_WON && currentWave >= totalWaves) {
            throw new IllegalArgumentException("l'ultima ondata deve produrre la vittoria");
        }
        if (state == GameState.VICTORY && currentWave != totalWaves) {
            throw new IllegalArgumentException("la vittoria richiede l'ultima ondata");
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
