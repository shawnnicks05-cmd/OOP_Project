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
public class CharacterOmar extends GameCharacter {
    
    public CharacterOmar(String name, String role, String damageType, String bestStat) {
        super(name, "Adaptive Mage / Knowledge DPS", "Knowledge Damage", "Wisdom");
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"Information Overflow", "Explain Again", "Smart Response"};
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
                    return this.name + " Dealing Massive AOE knowledge attack. " + targetBoss + "! Deals 52 Knowledge Damage.";
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> {
                if (mana >= 30) {
                    mana -= 30;
                    return this.name + " Restoring ally focus and stamina.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> {
                if (mana >= 40) {
                    mana -= 40;
                    return this.name + ": Copies enemy ability temporarily for 1 round";
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
        
        return 53; 
    }
    
    @Override
    public int defend(String[] enemyBosses) {
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
        
        return 45; 
    }
    
    @Override
    public String[] getPassivename() {
        return new String[]{
            "Instant Answer increasing teams morale by 30% every 4 rounds",
            "Small chance to instantly counter questions Increasing teams intelligence by 40%"
        };
    }
    @Override
    public double[] getPassiveValue() {
        // Balanced to 3 slots to match getPassivename() perfectly
        return new double[] { 30.0, 40.0}; 
    }
    @Override
    public String[] displayStats() {
        return new String[] {
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Adaptive Mage / Knowledge DPS"
        };
    }
    
    @Override 
    public void displayskills() {}
}
