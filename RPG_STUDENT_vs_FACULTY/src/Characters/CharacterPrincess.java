package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Concrete Healer / Resource Support Student Class: Princess
 * @author user
 */
public class CharacterPrincess extends GameCharacter {
    
    // Cleaned up constructor: Removed unused arguments to prevent instantiation issues
    public CharacterPrincess() {
        super("Princess", "Healer / Resource Support", "Support Energy", "Knowledge");
        setMaxHp(100);
        setHp(100);
        setMaxMana(100);
        setMana(100);
        setMorale(100);
        setPosition("Below");
        setAttackPower(25);
        setDefensePower(50);
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
    public String[] getSkillname() {
        return new String[] {"--SKILLS--\n","Citation Heal","Restores HP and mana.\n", "Research Link","Boosts ally intelligence.\n", "Verified Facts","Cancels enemy confusion attacks."};
    }
     
    @Override
    public String useSkills(int skillNumber, ArrayList<GameBoss> activeBosses) {
        if (skillNumber < 1 || skillNumber > 3) {
            return "Unknown skill selected.";
        }

        // Grab a fallback target boss from the engine array if needed
        GameBoss targetBoss = null;
        if (!activeBosses.isEmpty()) {
            targetBoss = activeBosses.get(0);
        }

        switch(skillNumber) {
            case 1 -> { // 🧪 Citation Heal (Team restoration)
                if (isSkillOnCooldown(1)) {
                    return getSkillDisplayName(1) + " can't be used for " + getSkillCooldownRemaining(1) + " more rounds.";
                }
                if (!trySpendMana(25)) {
                    return "No Mana!";
                }
                startSkillCooldown(1);

                // In your battle engine loop, you would typically accept an array of
                // students here and run student.heal(25) on them!

                return getName() + " uses [Citation Heal]! Restoring Team's Hp and Mana by 25 points.";
            }
            case 2 -> { // 🔗 Research Link (Stat Multiplier Buff)
                if (isSkillOnCooldown(2)) {
                    return getSkillDisplayName(2) + " can't be used for " + getSkillCooldownRemaining(2) + " more rounds.";
                }
                if (!trySpendMana(30)) {
                    return "No Mana!";
                }
                startSkillCooldown(2);
                return getName() + " establishes a [Research Link]! Boosting the Team's structural intelligence.";
            }
            case 3 -> { // 📜 Verified Facts (Debuff Cleanse)
                if (isSkillOnCooldown(3)) {
                    return getSkillDisplayName(3) + " can't be used for " + getSkillCooldownRemaining(3) + " more rounds.";
                }
                if (!trySpendMana(40)) {
                    return "No Mana!";
                }
                startSkillCooldown(3);

                // Logic update flag changes
                this.isConfused = false;

                return getName() + " presents [Verified Facts]! Canceling enemy confusion status mechanics and correcting errors.";
            }
            default -> {
                return "Unknown skill selected.";
            }
        }
    }  

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return getName() + " checks her references but finds no targets remaining.";
        }
        
        // Select the active boss threat directly
        GameBoss target = activeBosses.get(0);
        int baseDamage = Math.max(5, calculateAttackDamage(target) / 2);
        
        // Process character type matchups against the current boss target classification
        double modifier = calculateDamageModifier(target.getClassification());
        String result = attackBossWithRoll(target, baseDamage, modifier, "strikes");
        if (modifier > 1.0) {
            result += " [COUNTER TYPE ADVANTAGE!]";
        }
        return result;
    }

    @Override
    public String defend() {
        return getName() + " reviews peer-reviewed security documentation! Defenses temporarily raised by 35.";
    }

    @Override
    public String[] getPassivename() {
        return new String[]{
            "--PASSIVE--\n",
            "Reliable Source: Increasing all secondary team stats by 40%",
            "Misinformation Block: Overshield team morale by 40%"
        };
    }

    @Override
    public double[] getPassiveValue() {
        // Keeps a clean 1:1 mapping length array with getPassivename to prevent Index errors
        return new double[] { 0.40, 0.40 }; 
    }

    @Override
    public String[] displayStats() {
        return new String[] {
            "--STATS--\n",
            "HP: " + getHp() + "/" + getMaxHp(),
            "Mana: " + getMana() + "/" + getMaxMana(),
            "Morale: " + getMorale() + "%",
            "Role: Healer / Resource Support"
        };
    }
}
