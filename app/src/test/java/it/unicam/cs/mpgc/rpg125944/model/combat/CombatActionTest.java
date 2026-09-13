package it.unicam.cs.mpgc.rpg125944.model.combat;

import it.unicam.cs.mpgc.rpg125944.model.characters.BaseHero;
import it.unicam.cs.mpgc.rpg125944.model.characters.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatActionTest {
    @Test
    void basicAttackReturnsDamageOutcome() {
        Character attacker = new BaseHero("Attacker", 20, 8, 0, new BasicAttack());
        Character target = new BaseHero("Target", 20, 1, 3, new BasicAttack());

        ActionResult result = new BasicAttack().execute(attacker, target);

        assertEquals(8, result.rawDamage());
        assertEquals(5, result.damageDealt());
        assertFalse(result.critical());
        assertEquals(15, target.getHp());
    }

    @Test
    void criticalAttackWithCertaintyDoublesDamage() {
        Character attacker = new BaseHero("Attacker", 20, 8, 0, new BasicAttack());
        Character target = new BaseHero("Target", 20, 1, 0, new BasicAttack());

        ActionResult result = new CriticalAttack(1).execute(attacker, target);

        assertEquals(16, result.rawDamage());
        assertEquals(16, result.damageDealt());
        assertTrue(result.critical());
    }

    @Test
    void criticalAttackRejectsInvalidChance() {
        assertThrows(IllegalArgumentException.class, () -> new CriticalAttack(-0.1));
        assertThrows(IllegalArgumentException.class, () -> new CriticalAttack(1.1));
    }
}
