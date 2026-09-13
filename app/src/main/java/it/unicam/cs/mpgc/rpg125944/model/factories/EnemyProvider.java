package it.unicam.cs.mpgc.rpg125944.model.factories;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/** Fornisce un nemico adatto all'ondata richiesta. */
public interface EnemyProvider {
    Character createEnemy(int wave);
}
