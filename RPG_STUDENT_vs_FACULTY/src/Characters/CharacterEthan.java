package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Concrete Scanner / Precision DPS Student Class: Ethan
 * @author user
 */
public class CharacterEthan extends GameCharacter {
    
    public CharacterEthan() {
        super("Ethan", "Scanner / Precision DPS", "Precision Damage", "Analysis");
        setMaxHp(100);
        setHp(100);
        setMaxMana(100);
        setMana(100);
        setMorale(100);
        setPosition("Front");
        setAttackPower(50);
        setDefensePower(30);
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
            case 1 -> { // 🎯 Statistical Strike (High Precision Single-Target Nuke)
                if (targetBoss == null) return getName() + " finds no active boss to analyze and strike.";

                if (isSkillOnCooldown(1)) {
                    return getSkillDisplayName(1) + " can't be used for " + getSkillCooldownRemaining(1) + " more rounds.";
                }
                if (!trySpendMana(25)) {
                    return "No Mana!";
                }
                startSkillCooldown(1);

                int baseDamage = Math.max(5, calculateAttackDamage(targetBoss) - 5);
                // Processes system-wide counters (e.g., Precision Damage vs Quizzes)
                double modifier = calculateDamageModifier(targetBoss.getClassification());
                int finalDamage = (int) (baseDamage * modifier);
                targetBoss.takeDamage(finalDamage);

                String modifierAlert = modifier > 1.0 ? " [CRITICAL WEAKNESS EXPLOITED!]" : "";
                return getName() + " executes a high single-target Statistical Strike at " + targetBoss.getName() + "!" +
                       "\nDeals " + finalDamage + " Precision Damage." + modifierAlert;
            }
            case 2 -> { // 🔮 Trend Prediction (Self Evasion Buff)
                if (isSkillOnCooldown(2)) {
                    return getSkillDisplayName(2) + " can't be used for " + getSkillCooldownRemaining(2) + " more rounds.";
                }
                if (!trySpendMana(30)) {
                    return "No Mana!";
                }
                startSkillCooldown(2);
                // Engine level can check this flag during boss turn calculations to force a miss
                return getName() + " maps out data patterns with [Trend Prediction] and will dodge the next incoming attack!";
            }
            case 3 -> { // 🔓 Data Leak (Enemy Defense Debuff)
                if (targetBoss == null) return getName() + " finds no mainframe to breach.";

                if (isSkillOnCooldown(3)) {
                    return getSkillDisplayName(3) + " can't be used for " + getSkillCooldownRemaining(3) + " more rounds.";
                }
                if (!trySpendMana(40)) {
                    return "No Mana!";
                }
                startSkillCooldown(3);
                // Engine layer can utilize this feedback to amplify subsequent team damage output
                return getName() + " uncovers a [Data Leak] on " + targetBoss.getName() + "! Target defenses are severely lowered.";
            }
            default -> {
                return "Unknown skill selected.";
            }
        }
    }

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return getName() + " calibrates his scopes, but there are no targets to acquire.";
        }
        
        GameBoss target = activeBosses.get(0);
        int baseDamage = calculateAttackDamage(target);
        
        double modifier = calculateDamageModifier(target.getClassification());
        String result = attackBossWithRoll(target, baseDamage, modifier, "fires a calculated analytical point straight at");
        if (modifier > 1.0) {
            result += " [TARGETED COUNTER HIT!]";
        }
        return result;
    }

    @Override
    public String defend() {
        return getName() + " scans enemy attack vectors! Bracing tightly and mitigating up to 35 incoming damage points.";
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
    public String[] getSkillname() {
        return new String[] {"--SKILLS--\n","Statistical Strike","High single-target damage.\n", "Trend Prediction","Dodges the next enemy attack.\n", "Data Leak","Lowers enemy defense."};
    } 
    
    @Override
    public double[] getPassiveValue() {
        // Cleaned up to safe system decimals (3% and 20%) to match 1:1 with string mapping array
        return new double[] { 0.03, 0.20 }; 
    }
    @Override
    public String[] getPassivename() {
        return new String[]{
            "--PASSIVE--",
            "Weakens Detection by 3%",
            "Reveals enemy weak points increasing critical damage by 20%"
        };
    }
    @Override
    public String[] displayStats() {
        return new String[] {
            "--STATS--\n",
            "HP: " + getHp() + "/" + getMaxHp(),
            "Mana: " + getMana() + "/" + getMaxMana(),
            "Morale: " + getMorale() + "%",
            "Role: Scanner / Precision DPS"
        };
    }
}




