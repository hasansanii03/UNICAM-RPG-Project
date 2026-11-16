package it.unicam.cs.mpgc.rpg125944.model.combat;

/** Il giocatore beve una pozione per recuperare punti vita. */
public class UsePotionAction implements PlayerAction {

    @Override
    public String getName() {
        return "Usa pozione";
    }

    @Override
    public boolean isAvailable(Battle battle) {
        return battle.getRemainingPotions() > 0
                && battle.getHero().getHp() < battle.getHero().getMaxHp();
    }

    @Override
    public ActionOutcome execute(Battle battle) {
        return ActionOutcome.healing(battle.consumePotion());
    }
}
