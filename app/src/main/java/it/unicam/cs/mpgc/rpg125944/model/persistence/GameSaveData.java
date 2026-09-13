package it.unicam.cs.mpgc.rpg125944.model.persistence;

/** DTO versionato per una partita completa. */
public class GameSaveData {
    public int formatVersion;
    public int totalWaves;
    public int currentWave;
    public int remainingPotions;
    public int turnNumber;
    public String gameState;
    public CharacterSaveData hero;
    public CharacterSaveData enemy;
}
