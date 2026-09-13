package it.unicam.cs.mpgc.rpg125944;

import it.unicam.cs.mpgc.rpg125944.controller.GameController;
import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.ActionResult;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.PlayerAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.TurnResult;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyFactory;
import it.unicam.cs.mpgc.rpg125944.model.game.GameSession;
import it.unicam.cs.mpgc.rpg125944.model.game.GameState;
import it.unicam.cs.mpgc.rpg125944.model.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg125944.model.persistence.PersistenceException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;

/** Vista JavaFX che delega tutti i comandi di gioco a GameController. */
public class MainGUI extends Application {
    private static final int TOTAL_WAVES = 3;
    private static final int INITIAL_POTIONS = 2;

    private final GameController controller = new GameController(
            new EnemyFactory(),
            TOTAL_WAVES,
            INITIAL_POTIONS,
            new JsonGameRepository(Path.of(System.getProperty("user.home"), ".unicam-rpg", "savegame.json"))
    );

    private final Label waveLabel = new Label();
    private final Label stateLabel = new Label();
    private final Label heroLabel = new Label();
    private final Label enemyLabel = new Label();
    private final ProgressBar heroHpBar = new ProgressBar();
    private final ProgressBar enemyHpBar = new ProgressBar();
    private final TextArea battleLog = new TextArea();
    private final Button attackButton = new Button("Attacca");
    private final Button defendButton = new Button("Difenditi");
    private final Button potionButton = new Button("Usa pozione");
    private final Button nextWaveButton = new Button("Prossima ondata");

    @Override
    public void start(Stage stage) {
        Button newGameButton = new Button("Nuova partita");
        Button saveButton = new Button("Salva partita");
        Button loadButton = new Button("Carica partita");
        newGameButton.setOnAction(event -> startNewGame());
        saveButton.setOnAction(event -> saveGame());
        loadButton.setOnAction(event -> loadGame());
        attackButton.setOnAction(event -> executeAction(PlayerAction.ATTACK));
        defendButton.setOnAction(event -> executeAction(PlayerAction.DEFEND));
        potionButton.setOnAction(event -> executeAction(PlayerAction.USE_POTION));
        nextWaveButton.setOnAction(event -> startNextWave());

        heroHpBar.setPrefWidth(260);
        enemyHpBar.setPrefWidth(260);
        battleLog.setEditable(false);
        battleLog.setWrapText(true);
        battleLog.setPrefRowCount(8);

        VBox heroBox = new VBox(6, new Label("Eroe"), heroLabel, heroHpBar);
        VBox enemyBox = new VBox(6, new Label("Nemico"), enemyLabel, enemyHpBar);
        HBox combatants = new HBox(40, heroBox, enemyBox);
        combatants.setAlignment(Pos.CENTER);

        HBox actions = new HBox(10, attackButton, defendButton, potionButton, nextWaveButton);
        actions.setAlignment(Pos.CENTER);
        HBox persistenceActions = new HBox(10, saveButton, loadButton);
        persistenceActions.setAlignment(Pos.CENTER);
        VBox root = new VBox(16,
                new Label("UNICAM RPG Arena"),
                waveLabel,
                stateLabel,
                combatants,
                actions,
                new Label("Registro dello scontro"),
                battleLog,
                persistenceActions,
                newGameButton
        );
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        startNewGame();
        stage.setTitle("UNICAM RPG Arena");
        stage.setScene(new Scene(root, 680, 500));
        stage.show();
    }

    private void startNewGame() {
        controller.startNewGame(createHero());
        battleLog.setText("Nuova partita iniziata. Scegli un'azione.");
        refresh();
    }

    private Character createHero() {
        return new BaseHero("Arthur", 100, 18, 5, new BasicAttack());
    }

    private void executeAction(PlayerAction action) {
        try {
            TurnResult result = controller.submitAction(action);
            appendTurn(result);
            refresh();
        } catch (IllegalStateException exception) {
            battleLog.appendText("\nAzione non disponibile: " + exception.getMessage());
        }
    }

    private void startNextWave() {
        try {
            controller.startNextWave();
            battleLog.appendText("\nIniziata l'ondata " + controller.getSession().getCurrentWave() + ".");
            refresh();
        } catch (IllegalStateException exception) {
            battleLog.appendText("\nProssima ondata non disponibile: " + exception.getMessage());
        }
    }

    private void saveGame() {
        try {
            controller.saveGame();
            battleLog.appendText("\nPartita salvata.");
        } catch (PersistenceException exception) {
            battleLog.appendText("\nSalvataggio fallito: " + exception.getMessage());
        }
    }

    private void loadGame() {
        try {
            controller.loadGame();
            battleLog.setText("Partita salvata caricata.");
            refresh();
        } catch (PersistenceException | IllegalStateException exception) {
            battleLog.appendText("\nCaricamento fallito: " + exception.getMessage());
        }
    }

    private void appendTurn(TurnResult result) {
        GameSession session = controller.getSession();
        battleLog.appendText("\nTurno " + result.turnNumber() + ": ");
        switch (result.playerAction()) {
            case ATTACK -> appendAction(session.getHero().getName(), result.playerActionResult());
            case DEFEND -> {
                battleLog.appendText(session.getHero().getName() + " si difende e ");
                appendActionResult(result.playerActionResult());
            }
            case USE_POTION -> battleLog.appendText(session.getHero().getName()
                    + " recupera " + result.hpRestored() + " HP.");
        }
        if (result.enemyActionResult() != null) {
            battleLog.appendText(" " + session.getCurrentBattle().getEnemy().getName() + " ");
            appendActionResult(result.enemyActionResult());
        }
        if (result.battleState().name().endsWith("WON")) {
            battleLog.appendText(" Il nemico e' stato sconfitto.");
        } else if (result.battleState().name().endsWith("LOST")) {
            battleLog.appendText(" L'eroe e' stato sconfitto.");
        }
    }

    private void appendAction(String actorName, ActionResult result) {
        battleLog.appendText(actorName + " ");
        appendActionResult(result);
    }

    private void appendActionResult(ActionResult result) {
        battleLog.appendText("infligge " + result.damageDealt() + " danni");
        if (result.critical()) {
            battleLog.appendText(" con un colpo critico");
        }
        battleLog.appendText(".");
    }

    private void refresh() {
        GameSession session = controller.getSession();
        Character hero = session.getHero();
        Character enemy = session.getCurrentBattle().getEnemy();
        waveLabel.setText("Ondata " + session.getCurrentWave() + " di " + session.getTotalWaves()
                + " | Pozioni: " + session.getRemainingPotions());
        stateLabel.setText("Stato: " + formatState(session.getState()));
        updateCombatant(heroLabel, heroHpBar, hero);
        updateCombatant(enemyLabel, enemyHpBar, enemy);

        boolean battleActive = session.getState() == GameState.IN_PROGRESS;
        attackButton.setDisable(!battleActive);
        defendButton.setDisable(!battleActive);
        potionButton.setDisable(!battleActive || session.getRemainingPotions() == 0
                || hero.getHp() == hero.getMaxHp());
        nextWaveButton.setDisable(session.getState() != GameState.WAVE_WON);
    }

    private void updateCombatant(Label label, ProgressBar hpBar, Character character) {
        label.setText(character.getName() + " - HP " + character.getHp() + "/" + character.getMaxHp());
        hpBar.setProgress((double) character.getHp() / character.getMaxHp());
    }

    private String formatState(GameState state) {
        return switch (state) {
            case IN_PROGRESS -> "Scontro in corso";
            case WAVE_WON -> "Ondata superata - continua quando vuoi";
            case VICTORY -> "Vittoria";
            case DEFEAT -> "Sconfitta";
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
