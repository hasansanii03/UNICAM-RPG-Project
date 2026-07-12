package it.unicam.cs.mpgc.rpg125944.model.persistence;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

public interface HeroDAO {
    void saveHero(Character hero);
    Character loadHero();
}