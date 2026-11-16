package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.Enemy;
import it.unicam.cs.mpgc.rpg125944.model.characters.Hero;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Implementazione JSON del repository per partite complete. */
public class JsonGameRepository implements GameRepository {
    private static final int FORMAT_VERSION = 2;
    private final Path savePath;
    private final Gson gson;

    /** Crea un repository che sa salvare le azioni di combattimento di base. */
    public JsonGameRepository(Path savePath) {
        this(savePath, List.of(new BasicAttackCodec(), new CriticalAttackCodec()));
    }

    /** Crea un repository che sa salvare le azioni descritte dai codec indicati. */
    public JsonGameRepository(Path savePath, List<CombatActionCodec> actionCodecs) {
        this.savePath = Objects.requireNonNull(savePath, "savePath non puo' essere null");
        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(CombatAction.class, new CombatActionAdapter(actionCodecs))
                .create();
    }

    @Override
    public void save(GameSession session) {
        Objects.requireNonNull(session, "la partita non puo' essere null");
        GameSaveData data = toSaveData(session);
        try {
            Path parent = savePath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(savePath)) {
                gson.toJson(data, writer);
            }
        } catch (IOException | RuntimeException exception) {
            throw new PersistenceException("impossibile salvare la partita", exception);
        }
    }

    @Override
    public Optional<GameSession> load(EnemyProvider enemyProvider) {
        Objects.requireNonNull(enemyProvider, "enemyProvider non puo' essere null");
        if (!Files.exists(savePath)) {
            return Optional.empty();
        }
        try (Reader reader = Files.newBufferedReader(savePath)) {
            GameSaveData data = gson.fromJson(reader, GameSaveData.class);
            return Optional.of(toSession(data, enemyProvider));
        } catch (IOException | JsonParseException | IllegalArgumentException exception) {
            throw new PersistenceException("impossibile caricare la partita salvata", exception);
        }
    }

    private GameSaveData toSaveData(GameSession session) {
        GameSaveData data = new GameSaveData();
        data.formatVersion = FORMAT_VERSION;
        data.totalWaves = session.getTotalWaves();
        data.currentWave = session.getCurrentWave();
        data.remainingPotions = session.getRemainingPotions();
        data.turnNumber = session.getCurrentBattle().getTurnNumber();
        data.gameState = session.getState().name();
        data.hero = toCharacterData(session.getHero());
        data.enemy = toCharacterData(session.getCurrentBattle().getEnemy());
        return data;
    }

    private GameSession toSession(GameSaveData data, EnemyProvider enemyProvider) {
        if (data == null || data.formatVersion != FORMAT_VERSION) {
            throw new IllegalArgumentException("formato di salvataggio non supportato");
        }
        if (data.gameState == null) {
            throw new IllegalArgumentException("stato della partita mancante");
        }
        return GameSession.restore(
                toHero(data.hero),
                toEnemy(data.enemy),
                enemyProvider,
                data.totalWaves,
                data.currentWave,
                data.remainingPotions,
                data.turnNumber,
                GameState.valueOf(data.gameState)
        );
    }

    private CharacterSaveData toCharacterData(Character character) {
        CharacterSaveData data = new CharacterSaveData();
        data.name = character.getName();
        data.hp = character.getHp();
        data.maxHp = character.getMaxHp();
        data.attack = character.getAttackPower();
        data.defense = character.getDefense();
        data.combatAction = character.getCombatAction();
        return data;
    }

    private Hero toHero(CharacterSaveData data) {
        validate(data);
        return new Hero(data.name, data.maxHp, data.hp, data.attack, data.defense,
                data.combatAction);
    }

    private Enemy toEnemy(CharacterSaveData data) {
        validate(data);
        return new Enemy(data.name, data.maxHp, data.hp, data.attack, data.defense,
                data.combatAction);
    }

    private void validate(CharacterSaveData data) {
        if (data == null) {
            throw new IllegalArgumentException("dati del combattente mancanti");
        }
        if (data.name == null || data.combatAction == null) {
            throw new IllegalArgumentException("dati del combattente incompleti");
        }
    }
}
