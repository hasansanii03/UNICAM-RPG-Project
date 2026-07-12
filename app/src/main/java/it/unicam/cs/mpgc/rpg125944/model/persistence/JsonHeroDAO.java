package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHeroDAO implements HeroDAO {

    private static final String FILE_PATH = "savegame.json";
    private Gson gson = new Gson();

    @Override
    public void saveHero(Character hero) {
        // Travasiamo i dati nel DTO
        HeroSaveData data = new HeroSaveData();
        data.name = hero.getName();
        data.hp = hero.getHp();
        data.maxHp = hero.getMaxHp();
        data.baseAttack = hero.getAttackPower();
        data.baseDefense = hero.getDefense();

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
            System.out.println("Partita salvata con successo in " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

   @Override
    public Character loadHero() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            HeroSaveData data = gson.fromJson(reader, HeroSaveData.class);
            if (data != null) {
                BaseHero loadedHero = new BaseHero(data.name, data.maxHp, data.baseAttack, data.baseDefense, new BasicAttack());
                // Usa il metodo dedicato per impostare gli HP
                loadedHero.setHpForLoading(data.hp); 
                System.out.println("Partita caricata con successo!");
                return loadedHero;
            }
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento o file non trovato: " + e.getMessage());
        }
        return null; 
    }
}