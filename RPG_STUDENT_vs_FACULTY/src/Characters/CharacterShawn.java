package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Concrete Support/Buffer Student Class: Shawn
 * @author user
 */
public class CharacterShawn extends GameCharacter {
    
    // Cleaned up constructor: Removed unused parameters since Shawn has fixed base metrics
    public CharacterShawn() {
        super("Shawn", "Support/Buffer", "Mental / Tactical Damage", "Intelligence");
        setMaxHp(100);
        setHp(100);
        setMaxMana(100);
        setMana(100);
        setMorale(100);
        setPosition("Above");
        setAttackPower(40);
        setDefensePower(35);
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"--Skills--\n","BrainStorm Burst","AOE intelligence-based attack.  \n", "Group Study","Buffs teammates' attack and defense.\n", "Deadline Focus","Removes panic and stress debuffs.\n"};
    }
    @Override
    public String getRole() {
        return super.getRole();
    }
    @Override
    public String getName() {
        return super.getName();
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

        // Fallback target: Look for the first active boss object in the system layer
        GameBoss targetBoss = null;
        if (!activeBosses.isEmpty()) {
            targetBoss = activeBosses.get(0);
        }

        switch(skillNumber) {
            case 1 -> { // 🧠 BrainStorm Burst (Offensive Single-Target Skill)
                if (targetBoss == null) return getName() + " finds no active boss to attack.";

                if (isSkillOnCooldown(1)) {
                    return getSkillDisplayName(1) + " can't be used for " + getSkillCooldownRemaining(1) + " more rounds.";
                }
                if (!trySpendMana(25)) {
                    return "No Mana!";
                }
                startSkillCooldown(1);

                int baseDamage = calculateAttackDamage(targetBoss);
                // Apply class modifier multipliers based on your parent's type chart!
                double modifier = calculateDamageModifier(targetBoss.getClassification());
                int finalDamage = (int) (baseDamage * modifier);

                targetBoss.takeDamage(finalDamage);
                String modifierAlert = modifier > 1.0 ? " [COUNTER BONUS!]" : "";
                return getName() + " unleashes a BrainStorm Burst directly at " + targetBoss.getName() + "!" +
                       "\nDeals " + finalDamage + " Mental Damage." + modifierAlert;
            }
            case 2 -> { // 📚 Group Study (Team-wide Buff)
                if (isSkillOnCooldown(2)) {
                    return getSkillDisplayName(2) + " can't be used for " + getSkillCooldownRemaining(2) + " more rounds.";
                }
                if (!trySpendMana(30)) {
                    return "No Mana!";
                }
                startSkillCooldown(2);
                // In your engine layer, loop through party and add morale values here!
                return getName() + " initiates a Group Study! The entire party's morale is boosted.";
            }
            case 3 -> { // ⏳ Deadline Focus (Urgency Buff)
                if (isSkillOnCooldown(3)) {
                    return getSkillDisplayName(3) + " can't be used for " + getSkillCooldownRemaining(3) + " more rounds.";
                }
                if (!trySpendMana(40)) {
                    return "No Mana!";
                }
                startSkillCooldown(3);
                return getName() + ": 'Stay Focused Team!' Increasing the entire team's Haste and Morale!";
            }
            default -> { return "Unknown skill selected."; }
        }
    }

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return getName() + " checks the chalkboard but finds no active threats.";
        }
        
        // Instantly grabs the front active boss entity
        GameBoss target = activeBosses.get(0);
        int baseDamage = calculateAttackDamage(target);
        
        String result = attackBossWithRoll(target, baseDamage, 1.0, "throws a crude piece of feedback notes at");
        return result;
    }

    @Override
    public String defend() {
        // Prepare defensive parameters
        return getName() + " opens their course syllabus and braces for impact! Defenses temporarily raised.";
    }
        
    @Override
    public String[] getPassivename() {
        return new String[]{
            "--Passive Skills--\n",
            "Enhance Intelligence to all by 2%",
            "Increases team critical thinking by 3%",
            "Bonuses accuracy by 3%"
        };
    }
    
    @Override
    public double[] getPassiveValue() {
        // Balanced to exactly 3 slots to prevent array index corruption loops
        return new double[] { 0.02, 0.03, 0.03 }; 
    }

    @Override
    public String[] displayStats() {
        return new String[] {
            "--STATS--\n",
            "HP: " + getHp() + "/" + getMaxHp(),
            "Mana: " + getMana() + "/" + getMaxMana(),
            "Morale: " + getMorale() + "%",
            "Role: Support/Buffer"
        };
    }
}
