package it.unicam.cs.mpgc.rpg125944.controller;

import it.unicam.cs.mpgc.rpg125944.model.characters.Enemy;
import it.unicam.cs.mpgc.rpg125944.model.characters.Hero;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.AttackAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.persistence.GameRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameControllerTest {
    @Test
    void exposesGameUseCasesWithoutAUserInterface() {
        EnemyProvider enemyProvider = wave ->
                new Enemy("Enemy", 5, 1, 0, new BasicAttack());
        GameController controller = new GameController(enemyProvider, 1, 0, new InMemoryGameRepository());

        assertThrows(IllegalStateException.class, () -> controller.submitAction(new AttackAction()));
        controller.startNewGame(new Hero("Hero", 20, 10, 0, new BasicAttack()));
        controller.submitAction(new AttackAction());

        assertEquals(GameState.VICTORY, controller.getSession().getState());
    }

    @Test
    void notifiesObserversWhenTheGameChanges() {
        EnemyProvider enemyProvider = wave -> new Enemy("Enemy", 50, 1, 0, new BasicAttack());
        GameController controller = new GameController(enemyProvider, 1, 0, new InMemoryGameRepository());
        AtomicInteger notifications = new AtomicInteger();
        controller.addObserver(notifications::incrementAndGet);

        controller.startNewGame(new Hero("Hero", 20, 10, 0, new BasicAttack()));
        assertEquals(1, notifications.get());

        controller.submitAction(new AttackAction());
        assertEquals(2, notifications.get());
    }

    private static class InMemoryGameRepository implements GameRepository {
        @Override
        public void save(GameSession session) {
        }

        @Override
        public Optional<GameSession> load(EnemyProvider enemyProvider) {
            return Optional.empty();
        }
    }
}
