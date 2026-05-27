package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Concrete Adaptive Mage / Knowledge DPS Student Class: Omar
 * @author user
 */
public class CharacterOmar extends GameCharacter {
    
    // Cleaned up constructor: Removed unused arguments to stay optimized
    public CharacterOmar() {
        super("Omar", "Adaptive Mage / Knowledge DPS", "Knowledge Damage", "Wisdom");
        setMaxHp(100);
        setHp(100);
        setMaxMana(100);
        setMana(100);
        setMorale(100);
        setPosition("Front");
        setAttackPower(53);
        setDefensePower(20);
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"--SKILLS--\n","Information Overflow","Massive AOE knowledge attack.\n", "Explain Again","Restores ally focus and stamina.\n", "Smart Response\n","Copies enemy ability temporarily."};
    }
    @Override
    public int getMorale() {
        return super.getMorale();
    }
    @Override
    public String useSkills(int skillNumber, ArrayList<GameBoss> activeBosses) {
        if (skillNumber < 1 || skillNumber > 3) {
            return "Unknown skill selected.";
        }

        GameBoss targetBoss = null;
        if (!activeBosses.isEmpty()) {
            targetBoss = activeBosses.get(0);
        }

        switch(skillNumber) {
            case 1 -> { // 💥 Information Overflow (Massive single target / AOE nuke)
                if (targetBoss == null) return getName() + " finds no active boss to attack.";

                if (isSkillOnCooldown(1)) {
                    return getSkillDisplayName(1) + " can't be used for " + getSkillCooldownRemaining(1) + " more rounds.";
                }
                if (!trySpendMana(25)) {
                    return "No Mana!";
                }
                startSkillCooldown(1);

                int baseDamage = calculateAttackDamage(targetBoss) + 5;
                // Apply type multipliers based on the target boss classification (e.g., vs Exams)
                double modifier = calculateDamageModifier(targetBoss.getClassification());
                int finalDamage = (int) (baseDamage * modifier);

                targetBoss.takeDamage(finalDamage);
                String modifierAlert = modifier > 1.0 ? " [CRITICAL TYPE ADVANTAGE!]" : "";
                return getName() + " deals a massive Information Overflow directly at " + targetBoss.getName() + "!" +
                       "\nDeals " + finalDamage + " Knowledge Damage." + modifierAlert;
            }
            case 2 -> { // 🔁 Explain Again (Support restoration)
                if (isSkillOnCooldown(2)) {
                    return getSkillDisplayName(2) + " can't be used for " + getSkillCooldownRemaining(2) + " more rounds.";
                }
                if (!trySpendMana(30)) {
                    return "No Mana!";
                }
                startSkillCooldown(2);
                return getName() + " triggers [Explain Again]! Restoring allied focus metrics and stabilizing team stamina bars.";
            }
            case 3 -> { // 🧠 Smart Response (Mimic utility)
                if (isSkillOnCooldown(3)) {
                    return getSkillDisplayName(3) + " can't be used for " + getSkillCooldownRemaining(3) + " more rounds.";
                }
                if (!trySpendMana(40)) {
                    return "No Mana!";
                }
                startSkillCooldown(3);
                return getName() + " executes a [Smart Response]! Analyzing framework configurations and copying enemy attributes for 1 round.";
            }
            default -> {
                return "Unknown skill selected.";
            }
        }
    }
    
    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return getName() + " stands ready but finds no active threats to calculate.";
        }
        
        GameBoss target = activeBosses.get(0);
        int baseDamage = 53; // High base knowledge modifier attack
        
        double modifier = calculateDamageModifier(target.getClassification());
        String result = attackBossWithRoll(target, baseDamage, modifier, "drops structural factual counter-arguments onto");
        if (modifier > 1.0) {
            result += " [COUNTER BONUS ACTIVE!]";
        }
        return result;
    }
    
    @Override
    public String defend() {
        return getName() + " cross-references the grading criteria formulas! Bracing for attacks and temporarily mitigating 45 incoming threat points.";
    }
    
    @Override
    public String[] getPassivename() {
        return new String[]{
            "--PASSIVE--\n",
            "Instant Answer: Increasing team's morale by 30% every 4 rounds",
            "Counter Question: Instantly counter questions,\n increasing team intelligence by 40%"
        };
    }
    
    @Override
    public double[] getPassiveValue() {
        // Keeps a strict 1:1 mapping with getPassivename length to prevent UI array crashes
        return new double[] { 0.30, 0.40 }; 
    }
    
    @Override
    public String[] displayStats() {
        return new String[] {
            "--STATS--\n",
            "HP: " + getHp() + "/" + getMaxHp(),
            "Mana: " + getMana() + "/" + getMaxMana(),
            "Morale: " + getMorale() + "%",
            "Role: Adaptive Mage / Knowledge DPS"
        };
    }
}
