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
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
        
        // Default positioning layout strategy for the combat grid
        this.position = "Front"; 
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
                if (targetBoss == null) return this.name + " finds no compiler terminal to target.";
                
                if (this.mana >= 25) {
                    this.mana -= 25;
                    
                    int baseDamage = 50;
                    // Processes system multipliers (e.g., Tech Damage deals 1.5x damage against Projects classification!)
                    double modifier = calculateDamageModifier(targetBoss.getClassification());
                    int finalDamage = (int) (baseDamage * modifier);
                    
                    targetBoss.takeDamage(finalDamage);
                    
                    String modifierAlert = modifier > 1.0 ? " [COMPILER ERROR OVERFLOW BONUS!]" : "";
                    return this.name + " executes a JavaScript Crash on " + targetBoss.getName() + "!" +
                           "\nDeals " + finalDamage + " Tech Damage." + modifierAlert;
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> { // 🛡️ Responsive Shield (Defensive Support)
                if (this.mana >= 30) {
                    this.mana -= 30;
                    // In your battle turn processor, this can inject temporary shield layers to an ally
                    return this.name + " deploys a [Responsive Shield]! Creating temporary structural protection for the targeted slot.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> { // 🌀 UI Overload (Status Control Debuff)
                if (targetBoss == null) return this.name + " finds no interface viewport to clutter.";
                
                if (this.mana >= 40) {
                    this.mana -= 40;
                    // Modifies status metrics inside your turn mechanics
                    return this.name + " triggers a complete [UI Overload]! Cluttering the viewport and confusing " + targetBoss.getName() + ".";
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
            return this.name + " runs a diagnostic, but there are no targets to ping.";
        }
        
        GameBoss target = activeBosses.get(0);
        int baseDamage = 50; 
        
        double modifier = calculateDamageModifier(target.getClassification());
        int finalDamage = (int) (baseDamage * modifier);
        
        target.takeDamage(finalDamage);
        
        String bonusAlert = modifier > 1.0 ? " [DIRECT DEP_INJECTION EXPLOIT!]" : "";
        return this.name + " throws a raw unoptimized script terminal at " + target.getName() + " for " + finalDamage + " damage!" + bonusAlert;
    }

    @Override
    public String defend() {
        return this.name + " refactors the catch blocks! Hardening connection ports and mitigating 35 incoming damage points.";
    }
    
    @Override
    public String[] getPassivename() {
        return new String[]{
            "--PASSIVE--\n",
            "Clean Interface: Increasing team's morale by 7 every 5 rounds",
            "Code Optimization: Increasing critical damage and crit rate by 20% and 15%"
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
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Tech Builder / Utility DPS"
        };
    }
}