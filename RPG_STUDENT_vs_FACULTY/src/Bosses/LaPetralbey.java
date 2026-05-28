package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 6 Boss: LaPetralbey - Final Faculty Guardian
 * @author user
 */
public class LaPetralbey extends GameBoss {

    public LaPetralbey() {
        super("Petralbey", "Final Faculty Guardian", "Very Hard", "Cosmic Academic / Catastrophic Damage");
        this.hpBoss = 140;
        this.maxHp = 140;
        this.mana = 120;
        this.maxMana = 120;
        this.rage = 0;
        this.defence = 16;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Comprehensive Exam", "Impossible Deadline", "Knowledge Suppression", "Thesis Collapse"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Dean's Authority"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Final Faculty Guardian"
        };
    }

    @Override
    public int defend() {
        return this.defence + 35;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        int baseDamage = scaledDamage(28);
        StringBuilder result = new StringBuilder();
        result.append(this.name + " strikes with academic authority!\n");

        for (GameCharacter student : partyStudents) {
            if (student.getHp() > 0) {
                student.takeDamage(baseDamage);
            }
        }

        this.addRage(15);
        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Thesis deadline weight intensifies! Massive DoT active!" : "";
        result.append("All students take ").append(baseDamage).append(" damage! ").append(rageAlert);

        return result.toString();
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Comprehensive Exam
                int cost = scaledManaCost(60);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(22);
                    int damage = scaledDamage(55);
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            student.takeDamage(damage);
                        }
                    }
                    return this.name + " administers a [Comprehensive Exam]!\nMassive full-team damage of " + damage + "!";
                }
                return this.name + " attempted [Comprehensive Exam] but lacks Mana!";
            }
            case 2 -> { // Impossible Deadline
                int cost = scaledManaCost(55);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(25);
                    int damage = scaledDamage(40);
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            student.takeDamage(damage);
                        }
                    }
                    return this.name + " imposes an [Impossible Deadline]!\nApplies panic and stress! Damage: " + damage + " to all!";
                }
                return this.name + " attempted [Impossible Deadline] but lacks Mana!";
            }
            case 3 -> { // Knowledge Suppression
                int cost = scaledManaCost(50);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(20);
                    return this.name + " casts [Knowledge Suppression]!\nSilences all support skills temporarily!";
                }
                return this.name + " attempted [Knowledge Suppression] but lacks Mana!";
            }
            case 4 -> { // Thesis Collapse
                int cost = scaledManaCost(100);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(30);
                    int damage = scaledDamage(80);
                    target.takeDamage(damage);
                    return this.name + " triggers [Thesis Collapse] on " + target.getName() + "!\nUltimate finishing attack! Deals " + damage + " catastrophic damage!";
                }
                return this.name + " attempted [Thesis Collapse] but lacks Mana!";
            }
            default -> { return this.name + " adjusts her reading glasses..."; }
        }
    }
}
