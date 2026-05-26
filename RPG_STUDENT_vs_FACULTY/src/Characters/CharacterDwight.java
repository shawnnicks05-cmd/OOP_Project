/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Characters;

import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class CharacterDwight extends GameCharacter {
    
    
    public CharacterDwight(String name, String role, String damageType, String bestStat) {
        super(name, "Tech Builder / Utility DPS", "Tech Damage", "Creativity");
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
    }
    @Override
    public String[] getSkillname() {
        return new String[] {"JavaScript Crash", "Responsive Shield", "UI Overload"};
    }
    @Override
    public String useSkills(int skillNumber, String[] enemyBosses) {
        String targetBoss = "";
        
        if (skillNumber == 1) {
            int choice = JOptionPane.showOptionDialog(
                null, 
                "Where do you want to attack?",      
                "Select Target Boss",                
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                enemyBosses,                        
                enemyBosses[0]                      
            );

            if (choice == JOptionPane.CLOSED_OPTION) {
                return this.name + " cancelled their action.";
            }
            
            targetBoss = enemyBosses[choice]; 
        }
        

        switch(skillNumber) {
            case 1 -> {
                if (mana >= 25) {
                    mana -= 25;
                    return this.name + " Giving them a Glitch-based tech attack. " + targetBoss + "! Deals 50 Creativity Damage.";
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> {
                if (mana >= 30) {
                    mana -= 30;
                    return this.name + " Creates temporary protection.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> {
                if (mana >= 40) {
                    mana -= 40;
                    return this.name + ":Confuses enemies.";
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
    public int basicAttack(String[] enemyBosses) {
        int choice = JOptionPane.showOptionDialog(
            null, 
            "Where do you want to attack?",      
            "Select Target Boss",                
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            enemyBosses,                        
            enemyBosses[0]                      
        );                     

        if (choice == JOptionPane.CLOSED_OPTION) {
            return 0; 
        }
        
        return 50; 
    }
    
    @Override
    public int defend(String[] enemyBosses) {
        int choice = JOptionPane.showOptionDialog(
            null,
            "Brace for which boss's incoming attack?", // Fixed description text to match defending
            "Select Threat to Defend",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            enemyBosses,
            enemyBosses[0]
        );
    
        if (choice == JOptionPane.CLOSED_OPTION) {
            return 0;
        }
        
        return 35; // Returns defensive value 
    }
    
    @Override
    public String[] getPassivename() {
        return new String[]{
            "Clean Interface increasing teams morale by 7 every 5 rounds ",
            "Team skills become faster and more efficient increasing critical damage and critical change by 20% and 15% "
        };
    }
    @Override
    public double[] getPassiveValue() {
        // Balanced to 3 slots to match getPassivename() perfectly
        return new double[] { 7.0, 20.0, 15.0 }; 
    }
    @Override
    public String[] displayStats() {
        return new String[] {
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Tech Builder / Utility DPS"
        };
    }
     @Override public void displayskills() {}
    
}
