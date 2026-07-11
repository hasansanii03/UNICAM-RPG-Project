package it.unicam.cs.mpgc.rpg125944.model.characters;

/**
 * Decoratore concreto: aggiunge potenza d'attacco a un personaggio.
 */
public class Weapon extends CharacterDecorator {

    private String weaponName;
    private int bonusAttack;

    public Weapon(Character character, String weaponName, int bonusAttack) {
        super(character);
        this.weaponName = weaponName;
        this.bonusAttack = bonusAttack;
    }

    @Override
    public String getName() {
        // Modifica il nome dinamicamente
        return character.getName() + " armato con " + weaponName;
    }

    @Override
    public int getAttackPower() {
        // Somma l'attacco dell'eroe base + il bonus dell'arma
        return character.getAttackPower() + bonusAttack;
    }
}