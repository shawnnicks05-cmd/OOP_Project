package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 4 Boss: Lerui - Heavy Damage Boss
 * @author user
 */
public class Lerui extends GameBoss {

    public Lerui() {
        super("Lerui", "Heavy Damage Boss", "Medium-Hard", "Heavy AoE / Sonic Damage");
        this.hpBoss = 110;
        this.maxHp = 110;
        this.mana = 85;
        this.maxMana = 85;
        this.rage = 0;
        this.defence = 14;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Power Lecture", "Attendance Check", "Mental Exhaustion"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Intimidating Aura"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Heavy Damage Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 28;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        int baseDamage = scaledDamage(22);
        StringBuilder result = new StringBuilder();
        result.append(this.name + " delivers a mighty strike!\n");

        for (GameCharacter student : partyStudents) {
            if (student.getHp() > 0) {
                student.takeDamage(baseDamage);
                result.append("- ").append(student.getName()).append(" takes ").append(baseDamage).append(" damage!\n");
            }
        }

        this.addRage(13);
        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Lerui's voice reaches deafening levels! Sanity damage active!" : "";
        result.append(rageAlert);

        return result.toString();
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Power Lecture
                int cost = scaledManaCost(50);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(20);
                    int damage = scaledDamage(45);
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            student.takeDamage(damage);
                        }
                    }
                    return this.name + " unleashes a devastating [Power Lecture]!\nAoE damage of " + damage + " to all students!";
                }
                return this.name + " attempted [Power Lecture] but lacks Mana!";
            }
            case 2 -> { // Attendance Check
                int cost = scaledManaCost(30);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(12);
                    int damage = scaledDamage(20);
                    target.takeDamage(damage);
                    return this.name + " performs [Attendance Check] on " + target.getName() + "!\nStuns the target and deals " + damage + " damage!";
                }
                return this.name + " attempted [Attendance Check] but lacks Mana!";
            }
            case 3 -> { // Mental Exhaustion
                int cost = scaledManaCost(45);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(18);
                    return this.name + " drains mental energy with [Mental Exhaustion]!\nAll students lose stamina over time!";
                }
                return this.name + " attempted [Mental Exhaustion] but lacks Mana!";
            }
            default -> { return this.name + " booms loudly..."; }
        }
    }
}
