package it.unicam.cs.mpgc.rpg125944.view;

import it.unicam.cs.mpgc.rpg125944.controller.GameController;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.factories.HeroCatalog;
import it.unicam.cs.mpgc.rpg125944.model.factories.HeroClass;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;
import it.unicam.cs.mpgc.rpg125944.model.persistence.PersistenceException;
import it.unicam.cs.mpgc.rpg125944.util.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Controller della vista definita in game.fxml. Inoltra i comandi a {@link GameController}
 * e si aggiorna quando il model lo avvisa tramite {@link Observer}.
 */
public class GameViewController implements Observer {
    private final GameController gameController;
    private final HeroCatalog heroCatalog;
    private final BattleLogFormatter formatter;
    private final Map<PlayerAction, Button> actionButtons = new LinkedHashMap<>();

    @FXML private Label waveLabel;
    @FXML private Label stateLabel;
    @FXML private Label heroLabel;
    @FXML private Label enemyLabel;
    @FXML private ProgressBar heroHpBar;
    @FXML private ProgressBar enemyHpBar;
    @FXML private TextArea battleLog;
    @FXML private HBox actionBox;
    @FXML private ChoiceBox<HeroClass> heroChoice;
    @FXML private Button nextWaveButton;

    public GameViewController(
            GameController gameController, HeroCatalog heroCatalog, BattleLogFormatter formatter
    ) {
        this.gameController = Objects.requireNonNull(gameController, "gameController non puo' essere null");
        this.heroCatalog = Objects.requireNonNull(heroCatalog, "heroCatalog non puo' essere null");
        this.formatter = Objects.requireNonNull(formatter, "formatter non puo' essere null");
    }

    /** Chiamato da FXMLLoader dopo aver collegato i campi annotati con @FXML. */
    @FXML
    private void initialize() {
        for (PlayerAction action : gameController.getPlayerActions()) {
            Button button = new Button(action.getName());
            button.setOnAction(event -> executeAction(action));
            actionButtons.put(action, button);
            actionBox.getChildren().add(button);
        }
        heroChoice.getItems().addAll(heroCatalog.getClassesByToughness());
        heroChoice.getSelectionModel().selectFirst();
        gameController.addObserver(this);
        onNewGame();
    }

    @FXML
    private void onNewGame() {
        gameController.startNewGame(heroChoice.getValue().createHero());
        battleLog.setText("Nuova partita iniziata. Scegli un'azione.");
    }

    @FXML
    private void onNextWave() {
        try {
            gameController.startNextWave();
            log("Iniziata l'ondata " + gameController.getSession().getCurrentWave() + ".");
        } catch (IllegalStateException exception) {
            log("Prossima ondata non disponibile: " + exception.getMessage());
        }
    }

    @FXML
    private void onSave() {
        try {
            gameController.saveGame();
            log("Partita salvata.");
        } catch (PersistenceException exception) {
            log("Salvataggio fallito: " + exception.getMessage());
        }
    }

    @FXML
    private void onLoad() {
        try {
            gameController.loadGame();
            battleLog.setText("Partita salvata caricata.");
        } catch (PersistenceException | IllegalStateException exception) {
            log("Caricamento fallito: " + exception.getMessage());
        }
    }

    /** Il model ha cambiato stato: la vista si ridisegna. */
    @Override
    public void update() {
        GameSession session = gameController.getSession();
        Character hero = session.getHero();
        Character enemy = session.getCurrentBattle().getEnemy();
        waveLabel.setText(formatter.formatWave(session));
        stateLabel.setText("Stato: " + formatter.formatState(session.getState()));
        showCombatant(heroLabel, heroHpBar, hero);
        showCombatant(enemyLabel, enemyHpBar, enemy);

        boolean battleActive = session.getState() == GameState.IN_PROGRESS;
        actionButtons.forEach((action, button) ->
                button.setDisable(!battleActive || !action.isAvailable(session.getCurrentBattle())));
        nextWaveButton.setDisable(session.getState() != GameState.WAVE_WON);
    }

    private void executeAction(PlayerAction action) {
        try {
            TurnResult result = gameController.submitAction(action);
            GameSession session = gameController.getSession();
            log(formatter.formatTurn(result, session.getHero().getName(),
                    session.getCurrentBattle().getEnemy().getName()));
        } catch (IllegalStateException exception) {
            log("Azione non disponibile: " + exception.getMessage());
        }
    }

    private void showCombatant(Label label, ProgressBar hpBar, Character character) {
        label.setText(formatter.formatCombatant(character));
        hpBar.setProgress((double) character.getHp() / character.getMaxHp());
    }

    private void log(String message) {
        battleLog.appendText("\n" + message);
    }
}
