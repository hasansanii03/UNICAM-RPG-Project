package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/** Provides an enemy appropriate for the requested wave. */
public interface EnemyProvider {
    Character createEnemy(int wave);
}
