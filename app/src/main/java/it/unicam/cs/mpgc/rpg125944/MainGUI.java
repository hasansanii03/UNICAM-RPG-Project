package it.unicam.cs.mpgc.rpg125944;

import it.unicam.cs.mpgc.rpg125944.controller.GameController;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.characters.Hero;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.factories.EnemyFactory;
import it.unicam.cs.mpgc.rpg125944.model.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg125944.view.BattleLogFormatter;
import it.unicam.cs.mpgc.rpg125944.view.GameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Punto di ingresso e composition root: sceglie le implementazioni concrete
 * (nemici, salvataggio, eroe) e le consegna alla vista. L'aspetto grafico sta in game.fxml.
 */
public class MainGUI extends Application {
    private static final int TOTAL_WAVES = 3;
    private static final int INITIAL_POTIONS = 2;

    @Override
    public void start(Stage stage) throws IOException {
        GameController gameController = new GameController(
                new EnemyFactory(),
                TOTAL_WAVES,
                INITIAL_POTIONS,
                new JsonGameRepository(Path.of(System.getProperty("user.home"), ".unicam-rpg", "savegame.json"))
        );

        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource("view/game.fxml"));
        loader.setControllerFactory(type ->
                new GameViewController(gameController, MainGUI::createHero, new BattleLogFormatter()));
        Parent root = loader.load();

        stage.setTitle("UNICAM RPG Arena");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private static Character createHero() {
        return new Hero("Arthur", 100, 18, 5, new BasicAttack());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
