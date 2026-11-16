package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Enemy;
import it.unicam.cs.mpgc.rpg125944.model.characters.Hero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BattleTest {
    @Test
    void attackResolvesPlayerActionThenEnemyResponse() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 1);

        TurnResult result = battle.executeTurn(new AttackAction());

        assertEquals(10, result.playerActionResult().damageDealt());
        assertEquals(10, result.enemyActionResult().damageDealt());
        assertEquals(20, battle.getHero().getHp());
        assertEquals(10, battle.getEnemy().getHp());
        assertEquals(BattleState.IN_PROGRESS, result.battleState());
    }

    @Test
    void defendCounterattacksAndReducesIncomingDamage() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 1);

        TurnResult result = battle.executeTurn(new DefendAction());

        assertNotNull(result.playerActionResult());
        assertEquals(5, result.playerActionResult().damageDealt());
        assertNotNull(result.enemyActionResult());
        assertEquals(5, result.enemyActionResult().damageDealt());
        assertEquals(25, battle.getHero().getHp());
        assertEquals(15, battle.getEnemy().getHp());
    }

    @Test
    void potionRestoresHpAndConsumesOnePotion() {
        Battle battle = battleWith(30, 10, 0, 20, 0, 0, 1);
        battle.getHero().takeDamage(15);

        TurnResult result = battle.executeTurn(new UsePotionAction());

        assertEquals(15, result.hpRestored());
        assertEquals(0, result.remainingPotions());
        assertEquals(30, battle.getHero().getHp());
    }

    @Test
    void heroVictoryPreventsEnemyResponseAndFurtherTurns() {
        Battle battle = battleWith(30, 30, 0, 20, 10, 0, 1);

        TurnResult result = battle.executeTurn(new AttackAction());

        assertEquals(BattleState.HERO_WON, result.battleState());
        assertNull(result.enemyActionResult());
        assertThrows(IllegalStateException.class, () -> battle.executeTurn(new AttackAction()));
    }

    @Test
    void cannotUsePotionWhenNoneAreAvailable() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 0);

        assertThrows(IllegalStateException.class, () -> battle.executeTurn(new UsePotionAction()));
    }

    @Test
    void cannotUsePotionAtFullHealth() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 1);

        assertThrows(IllegalStateException.class, () -> battle.executeTurn(new UsePotionAction()));
        assertEquals(1, battle.getRemainingPotions());
    }

    @Test
    void newActionsWorkWithoutChangingBattle() {
        Battle battle = battleWith(30, 10, 0, 40, 10, 0, 0);
        PlayerAction doubleStrike = new PlayerAction() {
            @Override
            public String getName() {
                return "Doppio colpo";
            }

            @Override
            public boolean isAvailable(Battle battle) {
                return true;
            }

            @Override
            public ActionOutcome execute(Battle battle) {
                int first = battle.getEnemy().takeDamage(battle.getHero().getAttackPower());
                int second = battle.getEnemy().takeDamage(battle.getHero().getAttackPower());
                return ActionOutcome.damage(new ActionResult(20, first + second, false));
            }
        };

        TurnResult result = battle.executeTurn(doubleStrike);

        assertAll(
                () -> assertEquals("Doppio colpo", result.playerAction().getName()),
                () -> assertEquals(20, result.playerActionResult().damageDealt()),
                () -> assertEquals(20, battle.getEnemy().getHp())
        );
    }

    @Test
    void unavailableActionDoesNotChangeTheBattle() {
        Battle battle = battleWith(30, 10, 0, 20, 10, 0, 0);

        assertThrows(IllegalStateException.class, () -> battle.executeTurn(new UsePotionAction()));

        assertAll(
                () -> assertEquals(1, battle.getTurnNumber()),
                () -> assertEquals(30, battle.getHero().getHp()),
                () -> assertEquals(20, battle.getEnemy().getHp())
        );
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
        Character hero = new Hero("Hero", heroHp, heroAttack, heroDefense, new BasicAttack());
        Character enemy = new Enemy("Enemy", enemyHp, enemyAttack, enemyDefense, new BasicAttack());
        return new Battle(hero, enemy, potions);
    }
}
