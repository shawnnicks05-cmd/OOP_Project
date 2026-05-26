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
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
        
        // Default tactical positioning on your turn system grid
        this.position = "Front"; 
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"--SKILLS--\n","Information Overflow","Massive AOE knowledge attack.\n", "Explain Again","Restores ally focus and stamina.\n", "Smart Response\n","Copies enemy ability temporarily."};
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
                if (targetBoss == null) return this.name + " finds no active boss to attack.";
                
                if (this.mana >= 25) {
                    this.mana -= 25;
                    
                    int baseDamage = 52;
                    // Apply type multipliers based on the target boss classification (e.g., vs Exams)
                    double modifier = calculateDamageModifier(targetBoss.getClassification());
                    int finalDamage = (int) (baseDamage * modifier);
                    
                    targetBoss.takeDamage(finalDamage);
                    
                    String modifierAlert = modifier > 1.0 ? " [CRITICAL TYPE ADVANTAGE!]" : "";
                    return this.name + " deals a massive Information Overflow directly at " + targetBoss.getName() + "!" +
                           "\nDeals " + finalDamage + " Knowledge Damage." + modifierAlert;
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> { // 🔁 Explain Again (Support restoration)
                if (this.mana >= 30) {
                    this.mana -= 30;
                    return this.name + " triggers [Explain Again]! Restoring allied focus metrics and stabilizing team stamina bars.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> { // 🧠 Smart Response (Mimic utility)
                if (this.mana >= 40) {
                    this.mana -= 40;
                    return this.name + " executes a [Smart Response]! Analyzing framework configurations and copying enemy attributes for 1 round.";
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
            return this.name + " stands ready but finds no active threats to calculate.";
        }
        
        GameBoss target = activeBosses.get(0);
        int baseDamage = 53; // High base knowledge modifier attack
        
        double modifier = calculateDamageModifier(target.getClassification());
        int finalDamage = (int) (baseDamage * modifier);
        
        target.takeDamage(finalDamage);
        
        String bonusAlert = modifier > 1.0 ? " [COUNTER BONUS ACTIVE!]" : "";
        return this.name + " drops structural factual counter-arguments onto " + target.getName() + " for " + finalDamage + " damage!" + bonusAlert;
    }
    
    @Override
    public String defend() {
        return this.name + " cross-references the grading criteria formulas! Bracing for attacks and temporarily mitigating 45 incoming threat points.";
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
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Adaptive Mage / Knowledge DPS"
        };
    }
}