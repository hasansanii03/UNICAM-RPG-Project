package it.unicam.cs.mpgc.rpg125944.controller;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameControllerTest {
    @Test
    void exposesGameUseCasesWithoutAUserInterface() {
        EnemyProvider enemyProvider = wave ->
                new BaseHero("Enemy", 5, 1, 0, new BasicAttack());
        GameController controller = new GameController(enemyProvider, 1, 0);

        assertThrows(IllegalStateException.class, () -> controller.submitAction(PlayerAction.ATTACK));
        controller.startNewGame(new BaseHero("Hero", 20, 10, 0, new BasicAttack()));
        controller.submitAction(PlayerAction.ATTACK);

        assertEquals(GameState.VICTORY, controller.getSession().getState());
    }
}
