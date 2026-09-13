package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BattleTest {
    @Test
    void attackResolvesPlayerActionThenEnemyResponse() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 1);

        TurnResult result = battle.executeTurn(PlayerAction.ATTACK);

        assertEquals(10, result.playerActionResult().damageDealt());
        assertEquals(10, result.enemyActionResult().damageDealt());
        assertEquals(20, battle.getHero().getHp());
        assertEquals(10, battle.getEnemy().getHp());
        assertEquals(BattleState.IN_PROGRESS, result.battleState());
    }

    @Test
    void defendReducesIncomingDamage() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 1);

        TurnResult result = battle.executeTurn(PlayerAction.DEFEND);

        assertNull(result.playerActionResult());
        assertNotNull(result.enemyActionResult());
        assertEquals(5, result.enemyActionResult().damageDealt());
        assertEquals(25, battle.getHero().getHp());
        assertEquals(20, battle.getEnemy().getHp());
    }

    @Test
    void potionRestoresHpAndConsumesOnePotion() {
        Battle battle = battleWith(30, 10, 0, 20, 0, 0, 1);
        battle.getHero().takeDamage(15);

        TurnResult result = battle.executeTurn(PlayerAction.USE_POTION);

        assertEquals(15, result.hpRestored());
        assertEquals(0, result.remainingPotions());
        assertEquals(30, battle.getHero().getHp());
    }

    @Test
    void heroVictoryPreventsEnemyResponseAndFurtherTurns() {
        Battle battle = battleWith(30, 30, 0, 20, 10, 0, 1);

        TurnResult result = battle.executeTurn(PlayerAction.ATTACK);

        assertEquals(BattleState.HERO_WON, result.battleState());
        assertNull(result.enemyActionResult());
        assertThrows(IllegalStateException.class, () -> battle.executeTurn(PlayerAction.ATTACK));
    }

    @Test
    void cannotUsePotionWhenNoneAreAvailable() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 0);

        assertThrows(IllegalStateException.class, () -> battle.executeTurn(PlayerAction.USE_POTION));
    }

    @Test
    void cannotUsePotionAtFullHealth() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 1);

        assertThrows(IllegalStateException.class, () -> battle.executeTurn(PlayerAction.USE_POTION));
        assertEquals(1, battle.getRemainingPotions());
    }

    private Battle battleWith(
            int heroHp,
            int heroAttack,
            int heroDefense,
            int enemyHp,
            int enemyAttack,
            int enemyDefense,
            int potions
    ) {
        Character hero = new BaseHero("Hero", heroHp, heroAttack, heroDefense, new BasicAttack());
        Character enemy = new BaseHero("Enemy", enemyHp, enemyAttack, enemyDefense, new BasicAttack());
        return new Battle(hero, enemy, potions);
    }
}
