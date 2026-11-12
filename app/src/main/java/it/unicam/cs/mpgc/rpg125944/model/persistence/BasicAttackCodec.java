package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

/** Codec dell'attacco base, che non ha dati da salvare. */
public class BasicAttackCodec implements CombatActionCodec {

    @Override
    public String getType() {
        return "BASIC";
    }

    @Override
    public boolean supports(CombatAction action) {
        return action instanceof BasicAttack;
    }

    @Override
    public JsonObject encode(CombatAction action) {
        return new JsonObject();
    }

    @Override
    public CombatAction decode(JsonObject json) {
        return new BasicAttack();
    }
}
