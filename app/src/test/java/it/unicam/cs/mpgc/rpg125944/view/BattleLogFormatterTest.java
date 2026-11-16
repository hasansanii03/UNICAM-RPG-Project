package it.unicam.cs.mpgc.rpg125944.view;

import it.unicam.cs.mpgc.rpg125944.model.combat.ActionResult;
import it.unicam.cs.mpgc.rpg125944.model.combat.AttackAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.BattleState;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.combat.UsePotionAction;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BattleLogFormatterTest {
    private final BattleLogFormatter formatter = new BattleLogFormatter();

    @Test
    void describesAttackAndEnemyReply() {
        TurnResult result = new TurnResult(2, new AttackAction(), new ActionResult(18, 13, false),
                new ActionResult(10, 5, true), 0, 2, BattleState.IN_PROGRESS);

        assertEquals("Turno 2: Arthur usa Attacca: infligge 13 danni."
                + " Goblin infligge 5 danni con un colpo critico.",
                formatter.formatTurn(result, "Arthur", "Goblin"));
    }

    @Test
    void describesPotionUse() {
        TurnResult result = new TurnResult(1, new UsePotionAction(), null,
                new ActionResult(5, 0, false), 20, 1, BattleState.IN_PROGRESS);

        assertEquals("Turno 1: Arthur usa Usa pozione: recupera 20 HP. Goblin infligge 0 danni.",
                formatter.formatTurn(result, "Arthur", "Goblin"));
    }

    @Test
    void announcesTheEndOfTheBattle() {
        TurnResult won = new TurnResult(3, new AttackAction(), new ActionResult(18, 13, false),
                null, 0, 2, BattleState.HERO_WON);
        TurnResult lost = new TurnResult(3, new AttackAction(), new ActionResult(18, 13, false),
                new ActionResult(10, 10, false), 0, 2, BattleState.HERO_LOST);

        assertAll(
                () -> assertEquals(true, formatter.formatTurn(won, "Arthur", "Goblin")
                        .endsWith("Il nemico e' stato sconfitto.")),
                () -> assertEquals(true, formatter.formatTurn(lost, "Arthur", "Goblin")
                        .endsWith("L'eroe e' stato sconfitto."))
        );
    }

    @Test
    void translatesEveryGameState() {
        for (GameState state : GameState.values()) {
            assertEquals(false, formatter.formatState(state).isBlank());
        }
    }
}
