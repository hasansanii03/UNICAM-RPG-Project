package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import it.unicam.cs.mpgc.rpg125944.model.combat.ActionResult;
import it.unicam.cs.mpgc.rpg125944.model.combat.BasicAttack;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg125944.model.combat.CriticalAttack;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CombatActionAdapterTest {
    private static Gson gsonWith(CombatActionCodec... codecs) {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(CombatAction.class, new CombatActionAdapter(List.of(codecs)))
                .create();
    }

    /** Nuova azione di prova, definita solo nel test: serve a mostrare che l'adapter non va modificato. */
    private static class HealingStrike implements CombatAction {
        @Override
        public ActionResult execute(Character attacker, Character target) {
            return new ActionResult(0, 0, false);
        }
    }

    private static class HealingStrikeCodec implements CombatActionCodec {
        @Override
        public String getType() {
            return "HEALING_STRIKE";
        }

        @Override
        public boolean supports(CombatAction action) {
            return action instanceof HealingStrike;
        }

        @Override
        public JsonObject encode(CombatAction action) {
            return new JsonObject();
        }

        @Override
        public CombatAction decode(JsonObject json) {
            return new HealingStrike();
        }
    }

    @Test
    void writesTheTypeNextToTheActionData() {
        Gson gson = gsonWith(new BasicAttackCodec(), new CriticalAttackCodec());

        String json = gson.toJson(new CriticalAttack(0.25), CombatAction.class);

        assertAll(
                () -> assertEquals(true, json.contains("\"type\":\"CRITICAL\"")),
                () -> assertEquals(true, json.contains("\"criticalChance\":0.25"))
        );
    }

    @Test
    void readsBackTheSameKindOfAction() {
        Gson gson = gsonWith(new BasicAttackCodec(), new CriticalAttackCodec());

        CombatAction basic = gson.fromJson(gson.toJson(new BasicAttack(), CombatAction.class), CombatAction.class);
        CombatAction critical = gson.fromJson(
                gson.toJson(new CriticalAttack(0.4), CombatAction.class), CombatAction.class);

        assertAll(
                () -> assertInstanceOf(BasicAttack.class, basic),
                () -> assertInstanceOf(CriticalAttack.class, critical),
                () -> assertEquals(0.4, ((CriticalAttack) critical).getCriticalChance())
        );
    }

    @Test
    void newActionTypeNeedsOnlyANewCodec() {
        Gson gson = gsonWith(new BasicAttackCodec(), new HealingStrikeCodec());

        CombatAction restored = gson.fromJson(
                gson.toJson(new HealingStrike(), CombatAction.class), CombatAction.class);

        assertInstanceOf(HealingStrike.class, restored);
    }

    @Test
    void rejectsUnknownOrMissingTypes() {
        Gson gson = gsonWith(new BasicAttackCodec());

        assertThrows(JsonParseException.class, () -> gson.toJson(new CriticalAttack(0.1), CombatAction.class));
        assertThrows(JsonParseException.class, () -> gson.fromJson("{\"type\":\"MAGIC\"}", CombatAction.class));
        assertThrows(JsonParseException.class, () -> gson.fromJson("{}", CombatAction.class));
    }
}
