package Characters;

import javax.swing.JOptionPane;

// Make the class public so your UI screens can see it
public class CharacterShawn extends GameCharacter{
    
    public CharacterShawn(String name, String role, String damageType, String bestStat) {
        super(name, "Support/Buffer", "Mental / Tactical Damage", "Intelligence");
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
    }
    
    @Override
    public String[] getSkillname() {
        return new String[] {"BrainStorm Burst", "Group Study", "Deadline Focus"};
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
                    return this.name + " unleashes a BrainStorm Burst directly at " + targetBoss + "! Deals 40 Mental Damage.";
                } else {
                    return "No Mana!";
                }
            }
            case 2 -> {
                if (mana >= 30) {
                    mana -= 30;
                    return this.name + " initiates a Group Study! The entire party's morale is boosted.";
                } else {
                    return "No Mana!";
                }
            }
            case 3 -> {
                if (mana >= 40) {
                    mana -= 40;
                    return this.name + ": Stay Focused Team! Increasing Team's Morale";
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
        
        return 30; 
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
        
        return 40; // Returns defensive value 
    }
        
    @Override
    public String[] getPassivename() {
        return new String[]{
            "Enhance Intelligence to all by 2%",
            "Increases team critical Thinking by 3%",
            "Bonuses accuracy by 3% and lessing cooldown by 10secs"
        };
    }
    
    @Override
    public double[] getPassiveValue() {
        // Balanced to 3 slots to match getPassivename() perfectly
        return new double[] { 0.02, 0.03, 0.03 }; 
    }

    @Override
    public String[] displayStats() {
        return new String[] {
            "HP: " + this.hp + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Morale: " + this.morale + "%",
            "Role: Support/Buffer"
        };
    }

    // Keeping these abstract matches clean and accounted for
    @Override public void displayskills() {}
}