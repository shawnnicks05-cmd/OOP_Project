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
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
        
        // Default positioning rule for your turn grid setup
        this.position = "Above"; 
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"--Skills--\n","BrainStorm Burst","AOE intelligence-based attack.  \n", "Group Study","Buffs teammates' attack and defense.\n", "Deadline Focus","Removes panic and stress debuffs.\n"};
    }
    @Override
    public String getRole()
    {
        return role;
    }
    @Override
    public String getName()
    {
        return name;
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
                if (targetBoss == null) return this.name + " finds no active boss to attack.";
                
                if (this.mana >= 25) {
                    this.mana -= 25;
                    
                    int baseDamage = 40;
                    // Apply class modifier multipliers based on your parent's type chart!
                    double modifier = calculateDamageModifier(targetBoss.getClassification());
                    int finalDamage = (int) (baseDamage * modifier);
                    
                    targetBoss.takeDamage(finalDamage);
                    
                    String modifierAlert = modifier > 1.0 ? " [COUNTER BONUS!]" : "";
                    return this.name + " unleashes a BrainStorm Burst directly at " + targetBoss.getName() + "!" + 
                           "\nDeals " + finalDamage + " Mental Damage." + modifierAlert;
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> { // 📚 Group Study (Team-wide Buff)
                if (this.mana >= 30) {
                    this.mana -= 30;
                    // In your engine layer, loop through party and add morale values here!
                    return this.name + " initiates a Group Study! The entire party's morale is boosted.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> { // ⏳ Deadline Focus (Urgency Buff)
                if (this.mana >= 40) {
                    this.mana -= 40;
                    return this.name + ": 'Stay Focused Team!' Increasing the entire team's Haste and Morale!";
                } else {
                    return "No Mana!";
                }
            }
            default -> { return "Unknown skill selected."; }
        }
    }

    @Override
    public String basicAttack(ArrayList<GameBoss> activeBosses) {
        if (activeBosses.isEmpty()) {
            return this.name + " checks the chalkboard but finds no active threats.";
        }
        
        // Instantly grabs the front active boss entity
        GameBoss target = activeBosses.get(0);
        int baseDamage = 30;
        
        target.takeDamage(baseDamage);
        
        return this.name + " throws a crude piece of feedback notes at " + target.getName() + " for " + baseDamage + " damage!";
    }

    @Override
    public String defend() {
        // Prepare defensive parameters
        return this.name + " opens their course syllabus and braces for impact! Defenses temporarily raised.";
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
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Support/Buffer"
        };
    }
}