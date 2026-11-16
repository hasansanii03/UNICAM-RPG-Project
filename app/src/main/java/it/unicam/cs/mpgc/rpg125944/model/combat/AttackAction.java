package it.unicam.cs.mpgc.rpg125944.model.combat;

/** Il giocatore attacca il nemico con la strategia di attacco del proprio eroe. */
public class AttackAction implements PlayerAction {

    @Override
    public String getName() {
        return "Attacca";
    }

    @Override
    public boolean isAvailable(Battle battle) {
        return true;
    }

    @Override
    public ActionOutcome execute(Battle battle) {
        return ActionOutcome.damage(battle.getHero().getCombatAction().execute(battle.getHero(), battle.getEnemy()));
    }
}
