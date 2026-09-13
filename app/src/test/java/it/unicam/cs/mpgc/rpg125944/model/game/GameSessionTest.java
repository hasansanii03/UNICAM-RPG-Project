package it.unicam.cs.mpgc.rpg125944.model.game;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    void restoreDoesNotGenerateAnUnrelatedEnemy() {
        AtomicInteger generatedEnemies = new AtomicInteger();
        EnemyProvider provider = wave -> {
            generatedEnemies.incrementAndGet();
            return new BaseHero("Generated", 10, 1, 0, new BasicAttack());
        };
        Character hero = hero(20, 10);
        Character enemy = new BaseHero("Saved enemy", 10, 1, 0, new BasicAttack());

        GameSession.restore(hero, enemy, provider, 2, 1, 0, 1, GameState.IN_PROGRESS);

        assertEquals(0, generatedEnemies.get());
    }

    private Character hero(int hp, int attack) {
        return new BaseHero("Hero", hp, attack, 0, new BasicAttack());
    }
}
