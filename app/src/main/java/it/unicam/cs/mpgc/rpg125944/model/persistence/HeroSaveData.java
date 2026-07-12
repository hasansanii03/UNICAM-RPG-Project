package it.unicam.cs.mpgc.rpg125944.model.persistence;

/**
 * DTO (Data Transfer Object) per il salvataggio.
 * Contiene solo i dati grezzi, senza logica o interfacce.
 */
public class HeroSaveData {
    public String name;
    public int hp;
    public int maxHp;
    public int baseAttack;
    public int baseDefense;
    
    // Costruttore vuoto richiesto da Gson
    public HeroSaveData() {}
}