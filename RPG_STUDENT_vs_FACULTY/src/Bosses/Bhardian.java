package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Bhardian - Aggressive Faculty
 * @author user
 */
public class Bhardian extends GameBoss {

    public Bhardian() {
        super("Bhardian", "Aggressive Faculty", "Random", "Brutal / Crushing Morale");
        this.hpBoss = 110;
        this.maxHp = 110;
        this.mana = 80;
        this.maxMana = 80;
        this.rage = 0;
        this.defence = 14;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Pressure Wave", "Strict Grading"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Dominating Presence"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Aggressive Faculty"
        };
    }

    @Override
    public int defend() {
        return this.defence + 25;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        int baseDamage = 23;
        StringBuilder result = new StringBuilder();
        result.append(this.name + " charges forward menacingly!\n");

        for (GameCharacter student : partyStudents) {
            if (student.getHp() > 0) {
                student.takeDamage(baseDamage);
            }
        }

        this.addRage(13);
        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Intimidation reaches critical levels! Morale-shredding DoT active!" : "";
        result.append("All students take ").append(baseDamage).append(" damage! ").append(rageAlert);

        return result.toString();
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Pressure Wave
                if (this.mana >= 45) {
                    this.mana -= 45;
                    this.addRage(19);
                    int damage = 35;
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            student.takeDamage(damage);
                        }
                    }
                    return this.name + " unleashes a [Pressure Wave]!\nAoE morale damage of " + damage + " to all!";
                }
                return this.name + " attempted [Pressure Wave] but lacks Mana!";
            }
            case 2 -> { // Strict Grading
                if (this.mana >= 40) {
                    this.mana -= 40;
                    this.addRage(17);
                    // Target weakest
                    GameCharacter weakest = partyStudents.get(0);
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0 && student.getHp() < weakest.getHp()) {
                            weakest = student;
                        }
                    }
                    int damage = 42;
                    weakest.takeDamage(damage);
                    return this.name + " delivers [Strict Grading] to " + weakest.getName() + "!\nCritical hit! Deals " + damage + " damage!";
                }
                return this.name + " attempted [Strict Grading] but lacks Mana!";
            }
            default -> { return this.name + " glares intensely..."; }
        }
    }
}
