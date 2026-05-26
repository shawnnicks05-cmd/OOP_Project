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
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
        
        // Default positioning rule for your grid layout (e.g., Back row support)
        this.position = "Below"; 
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"Citation Heal", "Research Link", "Verified Facts"};
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
                if (this.mana >= 25) {
                    this.mana -= 25;
                    
                    // In your battle engine loop, you would typically accept an array of 
                    // students here and run student.heal(25) on them!
                    
                    return this.name + " uses [Citation Heal]! Restoring Team's Hp and Mana by 25 points.";
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> { // 🔗 Research Link (Stat Multiplier Buff)
                if (this.mana >= 30) {
                    this.mana -= 30;
                    return this.name + " establishes a [Research Link]! Boosting the Team's structural intelligence.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> { // 📜 Verified Facts (Debuff Cleanse)
                if (this.mana >= 40) {
                    this.mana -= 40;
                    
                    // Logic update flag changes
                    this.isConfused = false; 
                    
                    return this.name + " presents [Verified Facts]! Canceling enemy confusion status mechanics and correcting errors.";
                } else {
                    return "No Mana!";
                }
            }
            default -> {
                return "Unknown skill selected.";
            }
        }
    }  

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return this.name + " checks her references but finds no targets remaining.";
        }
        
        // Select the active boss threat directly
        GameBoss target = activeBosses.get(0);
        int baseDamage = 20; 
        
        // Process character type matchups against the current boss target classification
        double modifier = calculateDamageModifier(target.getClassification());
        int finalDamage = (int) (baseDamage * modifier);
        
        target.takeDamage(finalDamage);
        
        String counterBonusText = modifier > 1.0 ? " [COUNTER TYPE ADVANTAGE!]" : "";
        return this.name + " strikes " + target.getName() + " with baseline academic proof for " 
               + finalDamage + " Support damage!" + counterBonusText;
    }

    @Override
    public String defend() {
        return this.name + " reviews peer-reviewed security documentation! Defenses temporarily raised by 35.";
    }

    @Override
    public String[] getPassivename() {
        return new String[]{
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
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Healer / Resource Support"
        };
    }
}