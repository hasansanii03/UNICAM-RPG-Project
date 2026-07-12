package it.unicam.cs.mpgc.rpg125944;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.util.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI extends Application implements Observer {

    private BaseHero hero;
    private Character goblin;
    
    // Elementi grafici
    private Label hpLabel;
    private ProgressBar hpBar;

    @Override
    public void start(Stage primaryStage) {
        // 1. Inizializzazione del Modello (Model)
        hero = new BaseHero("Arthur", 100, 15, 5, new BasicAttack());
        goblin = new BaseHero("Goblin Brutto", 30, 10, 2, new BasicAttack());
        
        // 2. Iscrizione al pattern Observer (La GUI osserva l'Eroe)
        hero.addObserver(this);

        // 3. Creazione Elementi Grafici (View)
        Label nameLabel = new Label("Eroe: " + hero.getName());
        hpLabel = new Label(); // Verrà aggiornata da update()
        hpBar = new ProgressBar(1.0); // 1.0 significa 100%
        hpBar.setPrefWidth(200);

        // Bottone che simula un turno di gioco
        Button attackButton = new Button("Goblin attacca Arthur (Simulazione Turno)");
        attackButton.setOnAction(e -> {
            // Il bottone chiama il model per eseguire l'azione
            goblin.getCombatAction().execute(goblin, hero);
        });

        // Layout verticale
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(nameLabel, hpBar, hpLabel, attackButton);

        // Aggiornamento iniziale della grafica
        update();

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("UNICAM Auto-Battler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Questo metodo viene chiamato in automatico da notifyObservers() quando l'eroe perde HP
    @Override
    public void update() {
        // Platform.runLater assicura che le modifiche grafiche avvengano nel thread corretto di JavaFX
        Platform.runLater(() -> {
            hpLabel.setText("HP: " + hero.getHp() + " / " + hero.getMaxHp());
            // Calcola la percentuale di vita per la barra (da 0.0 a 1.0)
            double hpPercentage = (double) hero.getHp() / hero.getMaxHp();
            hpBar.setProgress(hpPercentage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}