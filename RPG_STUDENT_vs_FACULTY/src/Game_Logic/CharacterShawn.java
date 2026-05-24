/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Game_Logic;
import javax.swing.JOptionPane;
/**
 *
 * @author user
 */
class CharacterShawn extends Character {
    
    public CharacterShawn(String name,String role, String damageType, String bestStat) {
        
        super(name, "Support/Buffer", "Mental / Tactical Damage","Intelligence");
        this.maxHp = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.morale = 100;
    }
    
    /*abstract void displayskills();
    abstract double getPassiveValue();
    abstract void useSkills();
    abstract void displayStats();
    abstract void basicAttack();
    abstract void defend();
     public abstract String[] getSkillname();
    */
    
    
    
    
    public String[] getSkillname()
    {
        return new String[] {"BrainStorm Burst","Group Study","Dealine Focus"};
    }
    
    @Override
    public String useSkills(int skillNumber)
    {
        String targetBoss = "";
        if (skillNumber == 1) {
            // Show a dialog box with buttons mapped to each boss's name
            int choice = JOptionPane.showOptionDialog(
                null, 
                "Where do you want to attack?",      // Message inside popup
                "Select Target Boss",                // Title of popup window
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                enemyBosses,                        // The array of boss names (your custom buttons!)
                enemyBosses[0]                      // Default selected button
            );

            // If the user closed the window without picking, cancel the attack
            if (choice == JOptionPane.CLOSED_OPTION) {
                return this.name + " cancelled their action.";
            }
            
            targetBoss = enemyBosses[choice]; // Capture the chosen boss name
        }
        switch(skillNumber){
            case 1:
                if (mana >= 25)
                {
                    mana -= 25;
                    return this.name + "Here Comes the Burst!";
                }
            case 2:
                if( mana >= 30)
                {
                    mana -= 30;
                    
                    return this.name + "Get Strong Team!";
                }
            case 3:
            {
                if( mana >= 40)
                {
                    mana -= 40;
                    return this.name + "Stay Focused Team!";
                }
            }
            default:
                return "Unknown skill selected.";
        }
    }
    
    
    @Override
    public String[] getPassivename()
    {
        return new String[]{
            "Enhance Intelligence to all by 2%",
            "Increases team critical Thinking by 3%",
            "Bonuses accuracy by 3% and lessing cooldown by 10secs"
        };
    }
    
    @Override
    public double[] getPassiveValue()
    {
        return new double[] {2.0,3.0,3.0,10};
    }
    
    public void displaySkills()
    {
        
    }
 
  
   
}