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
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
        
        // Default tactical positioning on your turn system grid
        this.position = "Front"; 
    }
    
    
    @Override
    public int getMorale()
    {
        return morale;
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
                if (targetBoss == null) return this.name + " finds no active boss to analyze and strike.";
                
                if (this.mana >= 25) {
                    this.mana -= 25;
                    
                    int baseDamage = 40;
                    // Processes system-wide counters (e.g., Precision Damage vs Quizzes)
                    double modifier = calculateDamageModifier(targetBoss.getClassification());
                    int finalDamage = (int) (baseDamage * modifier);
                    
                    targetBoss.takeDamage(finalDamage);
                    
                    String modifierAlert = modifier > 1.0 ? " [CRITICAL WEAKNESS EXPLOITED!]" : "";
                    return this.name + " executes a high single-target Statistical Strike at " + targetBoss.getName() + "!" +
                           "\nDeals " + finalDamage + " Precision Damage." + modifierAlert;
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> { // 🔮 Trend Prediction (Self Evasion Buff)
                if (this.mana >= 30) {
                    this.mana -= 30;
                    // Engine level can check this flag during boss turn calculations to force a miss
                    return this.name + " maps out data patterns with [Trend Prediction] and will dodge the next incoming attack!";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> { // 🔓 Data Leak (Enemy Defense Debuff)
                if (targetBoss == null) return this.name + " finds no mainframe to breach.";
                
                if (this.mana >= 40) {
                    this.mana -= 40;
                    // Engine layer can utilize this feedback to amplify subsequent team damage output
                    return this.name + " uncovers a [Data Leak] on " + targetBoss.getName() + "! Target defenses are severely lowered.";
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
            return this.name + " calibrates his scopes, but there are no targets to acquire.";
        }
        
        GameBoss target = activeBosses.get(0);
        int baseDamage = 50; 
        
        double modifier = calculateDamageModifier(target.getClassification());
        int finalDamage = (int) (baseDamage * modifier);
        
        target.takeDamage(finalDamage);
        
        String bonusAlert = modifier > 1.0 ? " [TARGETED COUNTER HIT!]" : "";
        return this.name + " fires a calculated analytical point straight at " + target.getName() + " for " + finalDamage + " damage!" + bonusAlert;
    }

    @Override
    public String defend() {
        return this.name + " scans enemy attack vectors! Bracing tightly and mitigating up to 35 incoming damage points.";
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
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Scanner / Precision DPS"
        };
    }
}




