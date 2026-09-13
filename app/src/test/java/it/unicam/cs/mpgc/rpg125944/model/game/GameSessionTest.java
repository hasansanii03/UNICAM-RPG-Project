package it.unicam.cs.mpgc.rpg125944.model.game;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameSessionTest {
    private final EnemyProvider weakEnemyProvider = wave ->
            new BaseHero("Enemy " + wave, 5, 1, 0, new BasicAttack());

    @Test
    void startsTheNextWaveOnlyAfterWinningTheCurrentOne() {
        GameSession session = new GameSession(hero(20, 10), weakEnemyProvider, 2, 1);

        session.executePlayerAction(PlayerAction.ATTACK);

        assertEquals(GameState.WAVE_WON, session.getState());
        assertEquals(1, session.getCurrentWave());
        session.startNextWave();
        assertEquals(GameState.IN_PROGRESS, session.getState());
        assertEquals(2, session.getCurrentWave());
        assertEquals("Enemy 2", session.getCurrentBattle().getEnemy().getName());
    }

    @Test
    void winsTheGameAfterTheLastWave() {
        GameSession session = new GameSession(hero(20, 10), weakEnemyProvider, 1, 0);

        session.executePlayerAction(PlayerAction.ATTACK);

        assertEquals(GameState.VICTORY, session.getState());
        assertThrows(IllegalStateException.class, session::startNextWave);
    }

    @Test
    void recordsDefeatWhenTheHeroIsDefeated() {
        EnemyProvider strongEnemyProvider = wave ->
                new BaseHero("Enemy", 20, 20, 0, new BasicAttack());
        GameSession session = new GameSession(hero(10, 1), strongEnemyProvider, 1, 0);

        session.executePlayerAction(PlayerAction.DEFEND);

        assertEquals(GameState.DEFEAT, session.getState());
        assertThrows(IllegalStateException.class, () -> session.executePlayerAction(PlayerAction.ATTACK));
    }

    @Test
    void rejectsSkippingAnActiveWave() {
        GameSession session = new GameSession(hero(20, 10), weakEnemyProvider, 2, 0);

        assertThrows(IllegalStateException.class, session::startNextWave);
    }

    private Character hero(int hp, int attack) {
        return new BaseHero("Hero", hp, attack, 0, new BasicAttack());
    }
}
