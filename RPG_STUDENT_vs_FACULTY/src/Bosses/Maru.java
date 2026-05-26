package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 2 Boss: Maru  - Tactical Faculty Boss
 * @author user
 */
public class Maru extends GameBoss {

    public Maru () {
        super("Maru ", "Tactical Faculty Boss", "Medium", "True Damage / Pure Evaluation");
        this.hpBoss = 95;
        this.maxHp = 95;
        this.mana = 80;
        this.maxMana = 80;
        this.rage = 0;
        this.defence = 12;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Silent Pressure", "Focused Observation", "Strict Evaluation"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Calm Presence"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Tactical Faculty Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 25;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        GameCharacter chosenTarget = null;

        for (GameCharacter student : partyStudents) {
            if (student.getPosition().equalsIgnoreCase("Front") && student.getHp() > 0) {
                chosenTarget = student;
                break;
            }
        }

        if (chosenTarget == null) {
            for (GameCharacter student : partyStudents) {
                if (student.getHp() > 0) {
                    chosenTarget = student;
                    break;
                }
            }
        }

        if (chosenTarget != null) {
            int baseDamage = 18;
            chosenTarget.takeDamage(baseDamage);
            this.addRage(12);

            String rageAlert = this.isEnragedDoTActive() ? " \n[WARN] Maru 's precision is devastating! DoT active!" : "";
            return this.name + " observes " + chosenTarget.getName() + " and strikes with cold precision for " + baseDamage + " damage!" + rageAlert;
        }

        return this.name + " scans for targets but finds none.";
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Silent Pressure
                if (this.mana >= 30) {
                    this.mana -= 30;
                    this.addRage(14);
                    int damage = 22;
                    target.takeDamage(damage);
                    return this.name + " applies [Silent Pressure] to the team!\nConfidence lowered and " + damage + " damage dealt!";
                }
                return this.name + " attempted [Silent Pressure] but lacks Mana!";
            }
            case 2 -> { // Focused Observation
                if (this.mana >= 35) {
                    this.mana -= 35;
                    this.addRage(16);
                    // Find weakest target
                    GameCharacter weakest = partyStudents.get(0);
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0 && student.getHp() < weakest.getHp()) {
                            weakest = student;
                        }
                    }
                    int damage = 35;
                    weakest.takeDamage(damage);
                    return this.name + " uses [Focused Observation] to identify weakness in " + weakest.getName() + "!\nDeals " + damage + " bonus damage!";
                }
                return this.name + " attempted [Focused Observation] but lacks Mana!";
            }
            case 3 -> { // Strict Evaluation
                if (this.mana >= 45) {
                    this.mana -= 45;
                    this.addRage(18);
                    int damage = 40;
                    target.takeDamage(damage);
                    return this.name + " delivers [Strict Evaluation] to " + target.getName() + "!\nDeals " + damage + " precise damage!";
                }
                return this.name + " attempted [Strict Evaluation] but lacks Mana!";
            }
            default -> { return this.name + " remains silent and composed."; }
        }
    }
}
