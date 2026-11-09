package it.unicam.cs.mpgc.rpg125944.controller;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.AttackAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.DefendAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.combat.UsePotionAction;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyProvider;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.persistence.GameRepository;

import java.util.List;
import java.util.Objects;

/** Punto di accesso applicativo usato dalle interfacce per controllare una partita. */
public class GameController {
    private final EnemyProvider enemyProvider;
    private final int totalWaves;
    private final int availablePotions;
    private final GameRepository gameRepository;
    private final List<PlayerAction> playerActions;
    private GameSession session;

    /** Crea un controller con le azioni di base: attacco, difesa e pozione. */
    public GameController(
            EnemyProvider enemyProvider,
            int totalWaves,
            int availablePotions,
            GameRepository gameRepository
    ) {
        this(enemyProvider, totalWaves, availablePotions, gameRepository,
                List.of(new AttackAction(), new DefendAction(), new UsePotionAction()));
    }

    /** Crea un controller con le azioni indicate: aggiungere un'azione non richiede altre modifiche. */
    public GameController(
            EnemyProvider enemyProvider,
            int totalWaves,
            int availablePotions,
            GameRepository gameRepository,
            List<PlayerAction> playerActions
    ) {
        this.enemyProvider = Objects.requireNonNull(enemyProvider, "enemyProvider non puo' essere null");
        this.totalWaves = totalWaves;
        this.availablePotions = availablePotions;
        this.gameRepository = Objects.requireNonNull(gameRepository, "gameRepository non puo' essere null");
        this.playerActions = List.copyOf(Objects.requireNonNull(playerActions, "playerActions non puo' essere null"));
    }

    /** Azioni che il giocatore puo' scegliere, nell'ordine in cui vanno mostrate. */
    public List<PlayerAction> getPlayerActions() {
        return playerActions;
    }

    public void startNewGame(Character hero) {
        session = new GameSession(hero, enemyProvider, totalWaves, availablePotions);
    }

    public TurnResult submitAction(PlayerAction action) {
        return getSession().executePlayerAction(action);
    }

    public void startNextWave() {
        getSession().startNextWave();
    }

    public void saveGame() {
        gameRepository.save(getSession());
    }

    public void loadGame() {
        session = gameRepository.load(enemyProvider)
                .orElseThrow(() -> new IllegalStateException("nessuna partita salvata disponibile"));
    }

    public GameSession getSession() {
        if (session == null) {
            throw new IllegalStateException("avvia prima una nuova partita");
        }
        return session;
    }
}
