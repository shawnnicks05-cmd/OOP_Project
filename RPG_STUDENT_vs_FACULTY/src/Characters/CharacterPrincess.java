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
public class CharacterPrincess extends GameCharacter {
    
    public CharacterPrincess(String name, String role, String damageType, String bestStat) {
        super(name, "Healer / Resource Support", "Support Energy", "Knowledge");
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"Citation Heal", "Research Link", "Verified Facts"};
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
                    return this.name + " Restoring Teams Hp and Mana";
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> {
                if (mana >= 30) {
                    mana -= 30;
                    return this.name + " Boosting  Teams intelligence.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> {
                if (mana >= 40) {
                    mana -= 40;
                    return this.name + ": Canceleling enemy confusion attacks";
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
        
        return 20; 
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
            "Reliable Source Increasing all secondary teams stats by 40%",
            "Team gains resistance against misinformation Overshield morale by 40%"
        };
    }
    @Override
    public double[] getPassiveValue() {
        // Balanced to 3 slots to match getPassivename() perfectly
        return new double[] { 40.0, 40.0 }; 
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
    @Override 
    public void displayskills() {}
}
