package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.Character;

/** Il giocatore si difende: contrattacca debolmente e subisce meta' dei danni dal nemico. */
public class DefendAction implements PlayerAction {
    private static final int COUNTERATTACK_DIVISOR = 2;

    @Override
    public String getName() {
        return "Difenditi";
    }

    @Override
    public boolean isAvailable(Battle battle) {
        return true;
    }

    @Override
    public ActionOutcome execute(Battle battle) {
        int rawDamage = (battle.getHero().getAttackPower() + 1) / COUNTERATTACK_DIVISOR;
        int damageDealt = battle.getEnemy().takeDamage(rawDamage);
        return ActionOutcome.damage(new ActionResult(rawDamage, damageDealt, false));
    }

    @Override
    public Character protect(Character hero) {
        return new GuardedCharacter(hero);
    }
}
