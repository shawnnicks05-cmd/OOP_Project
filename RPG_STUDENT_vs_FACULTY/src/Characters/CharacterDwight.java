package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Concrete Tech Builder / Utility DPS Student Class: Dwight
 * @author user
 */
public class CharacterDwight extends GameCharacter {
    
    public CharacterDwight() {
        super("Dwight", "Tech Builder / Utility DPS", "Tech Damage", "Creativity");
        setMaxHp(100);
        setHp(100);
        setMaxMana(100);
        setMana(100);
        setMorale(100);
        setPosition("Front");
        setAttackPower(55);
        setDefensePower(25);
    }
    @Override
    public int getMorale() {
        return super.getMorale();
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
        return new String[] {"--SKILLS--","JavaScript Crash","Glitch-based tech attack.\n", "Responsive Shield","Creates temporary protection.\n", "UI Overload","Confuses enemies.\n"};
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
            case 1 -> { // 💻 JavaScript Crash (Single-Target Tech Attack)
                if (targetBoss == null) return getName() + " finds no compiler terminal to target.";

                if (isSkillOnCooldown(1)) {
                    return getSkillDisplayName(1) + " can't be used for " + getSkillCooldownRemaining(1) + " more rounds.";
                }
                if (!trySpendMana(25)) {
                    return "No Mana!";
                }
                startSkillCooldown(1);

                int baseDamage = calculateAttackDamage(targetBoss) + 10;
                // Processes system multipliers (e.g., Tech Damage deals 1.5x damage against Projects classification!)
                double modifier = calculateDamageModifier(targetBoss.getClassification());
                int finalDamage = (int) (baseDamage * modifier);

                targetBoss.takeDamage(finalDamage);

                String modifierAlert = modifier > 1.0 ? " [COMPILER ERROR OVERFLOW BONUS!]" : "";
                return getName() + " executes a JavaScript Crash on " + targetBoss.getName() + "!" +
                       "\nDeals " + finalDamage + " Tech Damage." + modifierAlert;
            }
            case 2 -> { // 🛡️ Responsive Shield (Defensive Support)
                if (isSkillOnCooldown(2)) {
                    return getSkillDisplayName(2) + " can't be used for " + getSkillCooldownRemaining(2) + " more rounds.";
                }
                if (!trySpendMana(30)) {
                    return "No Mana!";
                }
                startSkillCooldown(2);
                // In your battle turn processor, this can inject temporary shield layers to an ally
                return getName() + " deploys a [Responsive Shield]! Creating temporary structural protection for the targeted slot.";
            }
            case 3 -> { // 🌀 UI Overload (Status Control Debuff)
                if (targetBoss == null) return getName() + " finds no interface viewport to clutter.";

                if (isSkillOnCooldown(3)) {
                    return getSkillDisplayName(3) + " can't be used for " + getSkillCooldownRemaining(3) + " more rounds.";
                }
                if (!trySpendMana(40)) {
                    return "No Mana!";
                }
                startSkillCooldown(3);
                // Modifies status metrics inside your turn mechanics
                return getName() + " triggers a complete [UI Overload]! Cluttering the viewport and confusing " + targetBoss.getName() + ".";
            }
            default -> {
                return "Unknown skill selected.";
            }
        }
    }

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return getName() + " runs a diagnostic, but there are no targets to ping.";
        }
        
        GameBoss target = activeBosses.get(0);
        int baseDamage = calculateAttackDamage(target);
        
        double modifier = calculateDamageModifier(target.getClassification());
        String result = attackBossWithRoll(target, baseDamage, modifier, "throws a raw unoptimized script terminal at");
        if (modifier > 1.0) {
            result += " [DIRECT DEP_INJECTION EXPLOIT!]";
        }
        return result;
    }

    @Override
    public String defend() {
        return getName() + " refactors the catch blocks! Hardening connection ports and mitigating 35 incoming damage points.";
    }
    
    @Override
    public String[] getPassivename() {
        return new String[]{
            "--PASSIVE--\n",
            "Clean Interface: Increasing team's morale by 7 every 5 rounds",
            "Code Optimization: Increasing critical damage and crit rate by 20% and \n15%"
        };
    }

    @Override
    public double[] getPassiveValue() {
        // Consolidated cleanly into 2 index values to map 1:1 with getPassivename and avoid crashes
        // Saved as clean engine percentage representations (20% -> 0.20, 15% -> 0.15)
        return new double[] { 7.0, 0.20 }; 
    }

    @Override
    public String[] displayStats() {
        return new String[] {
            "--STATS--\n",
            "HP: " + getHp() + "/" + getMaxHp(),
            "Mana: " + getMana() + "/" + getMaxMana(),
            "Morale: " + getMorale() + "%",
            "Role: Tech Builder / Utility DPS"
        };
    }
}
