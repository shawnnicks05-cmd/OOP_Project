package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 5 Concrete Faculty Boss: Jenateh
 * @author user
 */
public class Jenateh extends GameBoss {
    
    // Cleaned up constructor: No redundant, unused parameters!
    public Jenateh() {
        super("Jenateh", "Strategic Controller", "Medium", "Precision / Piercing Damage");
        this.hpBoss = 100; // Double-check if your GameBoss parent uses hpBoss or hp!
        this.maxHp = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.rage = 100;
        this.defence = 15; // Set a default starting defense value
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Project Revision", "Critical Feedback", "Final Requirement"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Perfectionist"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Strategic Controller"
        };
    }

    @Override
    public int defend() {
        // Returns the value plus a defensive block calculation
        return this.defence + 30; 
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        GameCharacter chosenTarget = null;

        // 🎯 PRIORITY 1: Front Row
        for (GameCharacter student : partyStudents) {
            if (student.getPosition().equalsIgnoreCase("Front") && student.getHp() > 0) {
                chosenTarget = student;
                break; 
            }
        }

        // 🎯 PRIORITY 2: Above Row
        if (chosenTarget == null) {
            for (GameCharacter student : partyStudents) {
                if (student.getPosition().equalsIgnoreCase("Above") && student.getHp() > 0) {
                    chosenTarget = student;
                    break; 
                }
            }
        }

        // 🎯 PRIORITY 3: Below Row
        if (chosenTarget == null) {
            for (GameCharacter student : partyStudents) {
                if (student.getPosition().equalsIgnoreCase("Below") && student.getHp() > 0) {
                    chosenTarget = student;
                    break;
                }
            }
        }

        // --- EXECUTE THE ATTACK AND CONSTRUCT UI STRING ---
        if (chosenTarget != null) {
            int baseDamage = 30; 
            chosenTarget.takeDamage(baseDamage);
            this.addRage(15);
            
            // Fixed typo: Name updated from Hydra to Jenateh
            String rageAlert = this.isEnragedDoTActive() ? " \n[WARN] Jenateh is ENRAGED! Damage Over Time active!" : "";

            return this.name + " targets the " + chosenTarget.getPosition() + 
                   " row and strikes " + chosenTarget.getName() + " for " + baseDamage + " damage!" + rageAlert;
        }

        return this.name + " scans the classroom but finds no active students remaining.";
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size())); 
        
        switch (skillNumber) {
            case 1 -> { // Project Revision
                if (this.mana >= 40) {
                    this.mana -= 40; this.addRage(15);
                    int damage = 35; target.takeDamage(damage);
                    return this.name + " casts [Project Revision] on " + target.getName() + "!\nDeals " + damage + " Piercing Damage.";
                }
                return this.name + " attempted [Project Revision] but has insufficient Mana!";
            }
            case 2 -> { // Critical Feedback
                if (this.mana >= 50) {
                    this.mana -= 50; this.addRage(20);
                    int damage = 20; target.takeDamage(damage);
                    return this.name + " unleashes [Critical Feedback] at " + target.getName() + "!\nDeals " + damage + " damage and breaks their defenses!";
                }
                return this.name + " attempted [Critical Feedback] but has insufficient Mana!";
            }
            case 3 -> { // Final Requirement
                if (this.mana >= 80) {
                    this.mana -= 80; this.addRage(30);
                    int damage = 65; target.takeDamage(damage);
                    return this.name + " drops the ultimate [Final Requirement] onto " + target.getName() + "!\nDeals a devastating " + damage + " Catastrophic Damage!";
                }
                return this.name + " attempted [Final Requirement] but has insufficient Mana!";
            }
            default -> { return this.name + " hesitates over her lesson plan."; }
        }
    }
}