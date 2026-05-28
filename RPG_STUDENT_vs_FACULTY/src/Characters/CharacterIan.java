package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Concrete Tech Builder / Utility DPS Student Class: Dwight
 * @author user
 */
public class CharacterIan extends GameCharacter {
    
    public CharacterIan() {
        super("Ian", "A.i Spcialist", "Mouth Damage", "Audience Damage");
        setMaxHp(100);
        setHp(100);
        setMaxMana(100);
        setMana(100);
        setMorale(100);
        setPosition("front");
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
        return new String[] {"--SKILLS--","Audience Impact","Attack that critically impact \n", "Calculus Equate","Counter critical skills\n", "Audience Magistry Impact","Damaging boss with a chance of 1 hit \n"};
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

                // Skills should be stronger than basic attacks
                int baseDamage = (int) Math.round(calculateAttackDamage(targetBoss) * 1.6) + 10;
                // Processes system multipliers (e.g., Tech Damage deals 1.5x damage against Projects classification!)
                double modifier = calculateDamageModifier(targetBoss.getClassification());
                int finalDamage = (int) Math.round(baseDamage * modifier);

                targetBoss.takeDamage(finalDamage);

                String modifierAlert = modifier > 1.0 ? " [COMPILER ERROR OVERFLOW BONUS!]" : "";
                return getName() + " executes an Audence Impact" + targetBoss.getName() + "!" +
                       "\nDeals " + finalDamage + " Mouth Damage." + modifierAlert;
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
                return getName() + " equating Calculus penombros";
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
                return getName() + " All audience attack! " + targetBoss.getName() + ".";
            }
            default -> {
                return "Unknown skill selected.";
            }
        }
    }

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return getName() + " Attacking using microphone";
        }
        
        GameBoss target = activeBosses.get(0);
        // Basic attacks are intentionally weaker than skills
        int baseDamage = (int) Math.round(calculateAttackDamage(target) * 0.70);
        
        double modifier = calculateDamageModifier(target.getClassification());
        String result = attackBossWithRoll(target, baseDamage, modifier, "throws a raw unoptimized script terminal at");
        if (modifier > 1.0) {
            result += " [DIRECT DEP_INJECTION EXPLOIT!]";
        }
        return result;
    }

    @Override
    public String defend() {
        return getName() + " Defending myself adding 30 def";
    }
    
    @Override
    public String[] getPassivename() {
        return new String[]{
            "--PASSIVE--\n",
            "Friend zone: adding everyone attack damage 7.0",
            "Calculus mathically: increases critical damage to himself by 0.20\n"
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
            "Role: A.I specialist"
        };
    }
}
