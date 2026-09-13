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

/** JavaFX view that delegates all game commands to GameController. */
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
    private final Button attackButton = new Button("Attack");
    private final Button defendButton = new Button("Defend");
    private final Button potionButton = new Button("Use potion");
    private final Button nextWaveButton = new Button("Next wave");

    @Override
    public void start(Stage stage) {
        Button newGameButton = new Button("New game");
        Button saveButton = new Button("Save game");
        Button loadButton = new Button("Load game");
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

        VBox heroBox = new VBox(6, new Label("Hero"), heroLabel, heroHpBar);
        VBox enemyBox = new VBox(6, new Label("Enemy"), enemyLabel, enemyHpBar);
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
                new Label("Battle log"),
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
        battleLog.setText("A new game has started. Choose an action.");
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
            battleLog.appendText("\nAction unavailable: " + exception.getMessage());
        }
    }

    private void startNextWave() {
        try {
            controller.startNextWave();
            battleLog.appendText("\nWave " + controller.getSession().getCurrentWave() + " has started.");
            refresh();
        } catch (IllegalStateException exception) {
            battleLog.appendText("\nNext wave unavailable: " + exception.getMessage());
        }
    }

    private void saveGame() {
        try {
            controller.saveGame();
            battleLog.appendText("\nGame saved.");
        } catch (PersistenceException exception) {
            battleLog.appendText("\nSave failed: " + exception.getMessage());
        }
    }

    private void loadGame() {
        try {
            controller.loadGame();
            battleLog.setText("Saved game loaded.");
            refresh();
        } catch (PersistenceException | IllegalStateException exception) {
            battleLog.appendText("\nLoad failed: " + exception.getMessage());
        }
    }

    private void appendTurn(TurnResult result) {
        GameSession session = controller.getSession();
        battleLog.appendText("\nTurn " + result.turnNumber() + ": ");
        switch (result.playerAction()) {
            case ATTACK -> appendAction(session.getHero().getName(), result.playerActionResult());
            case DEFEND -> battleLog.appendText(session.getHero().getName() + " defends.");
            case USE_POTION -> battleLog.appendText(session.getHero().getName()
                    + " restores " + result.hpRestored() + " HP.");
        }
        if (result.enemyActionResult() != null) {
            battleLog.appendText(" " + session.getCurrentBattle().getEnemy().getName() + " ");
            appendActionResult(result.enemyActionResult());
        }
        if (result.battleState().name().endsWith("WON")) {
            battleLog.appendText(" The enemy was defeated.");
        } else if (result.battleState().name().endsWith("LOST")) {
            battleLog.appendText(" The hero was defeated.");
        }
    }

    private void appendAction(String actorName, ActionResult result) {
        battleLog.appendText(actorName + " ");
        appendActionResult(result);
    }

    private void appendActionResult(ActionResult result) {
        battleLog.appendText("deals " + result.damageDealt() + " damage");
        if (result.critical()) {
            battleLog.appendText(" with a critical hit");
        }
        battleLog.appendText(".");
    }

    private void refresh() {
        GameSession session = controller.getSession();
        Character hero = session.getHero();
        Character enemy = session.getCurrentBattle().getEnemy();
        waveLabel.setText("Wave " + session.getCurrentWave() + " of " + session.getTotalWaves()
                + " | Potions: " + session.getRemainingPotions());
        stateLabel.setText("State: " + formatState(session.getState()));
        updateCombatant(heroLabel, heroHpBar, hero);
        updateCombatant(enemyLabel, enemyHpBar, enemy);

        boolean battleActive = session.getState() == GameState.IN_PROGRESS;
        attackButton.setDisable(!battleActive);
        defendButton.setDisable(!battleActive);
        potionButton.setDisable(!battleActive || session.getRemainingPotions() == 0);
        nextWaveButton.setDisable(session.getState() != GameState.WAVE_WON);
    }

    private void updateCombatant(Label label, ProgressBar hpBar, Character character) {
        label.setText(character.getName() + " - HP " + character.getHp() + "/" + character.getMaxHp());
        hpBar.setProgress((double) character.getHp() / character.getMaxHp());
    }

    private String formatState(GameState state) {
        return switch (state) {
            case IN_PROGRESS -> "Battle in progress";
            case WAVE_WON -> "Wave won - continue when ready";
            case VICTORY -> "Victory";
            case DEFEAT -> "Defeat";
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
