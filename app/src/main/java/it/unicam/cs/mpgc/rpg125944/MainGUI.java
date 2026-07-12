package it.unicam.cs.mpgc.rpg125944;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.persistence.HeroDAO;
import it.unicam.cs.mpgc.rpg125944.model.persistence.JsonHeroDAO;
import it.unicam.cs.mpgc.rpg125944.util.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI extends Application implements Observer {

    private BaseHero hero; // L'eroe base (che può essere osservato)
    private Character currentHeroInterface; // Il riferimento generico per il salvataggio
    private Character goblin;
    
    // Strumento di persistenza (DAO)
    private final HeroDAO heroDAO = new JsonHeroDAO();
    
    // Elementi grafici
    private Label hpLabel;
    private ProgressBar hpBar;

    @Override
    public void start(Stage primaryStage) {
        // 1. Inizializzazione del Modello
        setupNewHero();
        goblin = new BaseHero("Goblin Brutto", 30, 10, 2, new BasicAttack());
        
        // 2. Creazione Elementi Grafici
        Label titleLabel = new Label("🔥 UNICAM RPG: Arena 🔥");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        hpLabel = new Label(); 
        hpBar = new ProgressBar(1.0); 
        hpBar.setPrefWidth(250);

        // Bottoni Azione
        Button attackButton = new Button("⚔️ Il Goblin ti Attacca!");
        attackButton.setStyle("-fx-base: #ffcccc;");
        attackButton.setOnAction(e -> {
            goblin.getCombatAction().execute(goblin, hero);
        });
        
        // Bottoni Salvataggio
        Button saveButton = new Button("💾 Salva Partita");
        saveButton.setOnAction(e -> {
            // Salviamo usando il riferimento astratto
            heroDAO.saveHero(currentHeroInterface); 
            System.out.println("Salvataggio completato dal pulsante GUI.");
        });
        
        Button loadButton = new Button("📂 Carica Partita");
        loadButton.setOnAction(e -> {
            Character loaded = heroDAO.loadHero();
            if (loaded != null && loaded instanceof BaseHero) {
                // Sostituiamo il vecchio eroe con quello caricato
                this.hero = (BaseHero) loaded;
                this.currentHeroInterface = this.hero;
                // Dobbiamo ricollegare la GUI (che è l'Observer) al nuovo eroe
                this.hero.addObserver(this);
                update(); // Aggiorniamo subito la grafica
                System.out.println("Partita caricata dalla GUI.");
            } else {
                System.out.println("Nessun salvataggio trovato.");
            }
        });

        // Layout
        HBox saveLoadBox = new HBox(10, saveButton, loadButton);
        saveLoadBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, hpBar, hpLabel, attackButton, saveLoadBox);

        update(); // Aggiornamento iniziale

        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setTitle("UNICAM Auto-Battler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metodo di supporto per creare un eroe pulito
    private void setupNewHero() {
        hero = new BaseHero("Arthur", 100, 15, 5, new BasicAttack());
        currentHeroInterface = hero; // Per il salvataggio
        hero.addObserver(this); // La GUI osserva l'Eroe
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            hpLabel.setText(hero.getName() + " - HP: " + hero.getHp() + " / " + hero.getMaxHp());
            double hpPercentage = (double) hero.getHp() / hero.getMaxHp();
            hpBar.setProgress(hpPercentage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}