package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.CriticalAttack;

/** Codec dell'attacco critico, di cui si salva la probabilita' di colpo critico. */
public class CriticalAttackCodec implements CombatActionCodec {
    private static final String CRITICAL_CHANCE = "criticalChance";

    @Override
    public String getType() {
        return "CRITICAL";
    }

    @Override
    public boolean supports(CombatAction action) {
        return action instanceof CriticalAttack;
    }

    @Override
    public JsonObject encode(CombatAction action) {
        JsonObject json = new JsonObject();
        json.addProperty(CRITICAL_CHANCE, ((CriticalAttack) action).getCriticalChance());
        return json;
    }

    @Override
    public CombatAction decode(JsonObject json) {
        if (!json.has(CRITICAL_CHANCE)) {
            throw new IllegalArgumentException("probabilita' critica mancante");
        }
        return new CriticalAttack(json.get(CRITICAL_CHANCE).getAsDouble());
    }
}
