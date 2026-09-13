package it.unicam.cs.mpgc.rpg125944.model.persistence;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonGameRepositoryTest {
    @TempDir
    Path temporaryDirectory;

    private final EnemyProvider enemyProvider = wave ->
            new BaseHero("Enemy " + wave, 30, 5, 0, new BasicAttack());

    @Test
    void savesAndRestoresTheCompleteActiveSession() {
        Path savePath = temporaryDirectory.resolve("savegame.json");
        JsonGameRepository repository = new JsonGameRepository(savePath);
        GameSession session = new GameSession(
                new BaseHero("Hero", 50, 10, 2, new BasicAttack()), enemyProvider, 3, 2
        );
        session.getHero().takeDamage(10);
        session.executePlayerAction(PlayerAction.USE_POTION);

        repository.save(session);
        GameSession restored = repository.load(enemyProvider).orElseThrow();

        assertEquals(session.getCurrentWave(), restored.getCurrentWave());
        assertEquals(session.getTotalWaves(), restored.getTotalWaves());
        assertEquals(session.getRemainingPotions(), restored.getRemainingPotions());
        assertEquals(session.getHero().getHp(), restored.getHero().getHp());
        assertEquals(session.getCurrentBattle().getEnemy().getHp(), restored.getCurrentBattle().getEnemy().getHp());
        assertEquals(session.getCurrentBattle().getTurnNumber(), restored.getCurrentBattle().getTurnNumber());
    }

    @Test
    void returnsEmptyWhenNoSaveExists() {
        JsonGameRepository repository = new JsonGameRepository(temporaryDirectory.resolve("missing.json"));

        assertTrue(repository.load(enemyProvider).isEmpty());
    }

    @Test
    void rejectsMalformedJson() throws IOException {
        Path savePath = temporaryDirectory.resolve("invalid.json");
        Files.writeString(savePath, "not json");
        JsonGameRepository repository = new JsonGameRepository(savePath);

        assertThrows(PersistenceException.class, () -> repository.load(enemyProvider));
    }

    @Test
    void rejectsIncompleteJson() throws IOException {
        Path savePath = temporaryDirectory.resolve("incomplete.json");
        Files.writeString(savePath, "{\"formatVersion\":1,\"gameState\":\"IN_PROGRESS\"}");
        JsonGameRepository repository = new JsonGameRepository(savePath);

        assertThrows(PersistenceException.class, () -> repository.load(enemyProvider));
    }

    @Test
    void writesTheSaveFile() {
        Path savePath = temporaryDirectory.resolve("nested").resolve("savegame.json");
        JsonGameRepository repository = new JsonGameRepository(savePath);
        GameSession session = new GameSession(
                new BaseHero("Hero", 20, 10, 0, new BasicAttack()), enemyProvider, 1, 0
        );

        repository.save(session);

        assertTrue(Files.exists(savePath));
        assertFalse(Files.isDirectory(savePath));
    }
}
