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
        // Display name changed per request (class name/file remains Maru.java)
        super("Meruh", "Tactical Faculty Boss", "Medium", "True Damage / Pure Evaluation");
        this.hpBoss = 95;
        this.maxHp = 95;
        this.mana = 80;
        this.maxMana = 80;
        this.rage = 0;
        this.defence = 12;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Silent Pressure", "Focused Observation", "Bursting 3.0"};
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
            int baseDamage = scaledDamage(18);
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
                int cost = scaledManaCost(30);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(14);
                    int damage = scaledDamage(22);
                    target.takeDamage(damage);
                    return this.name + " applies [Silent Pressure] to the team!\nConfidence lowered and " + damage + " damage dealt!";
                }
                return this.name + " attempted [Silent Pressure] but lacks Mana!";
            }
            case 2 -> { // Focused Observation
                int cost = scaledManaCost(35);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(16);
                    // Find weakest target
                    GameCharacter weakest = partyStudents.get(0);
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0 && student.getHp() < weakest.getHp()) {
                            weakest = student;
                        }
                    }
                    int damage = scaledDamage(35);
                    weakest.takeDamage(damage);
                    return this.name + " uses [Focused Observation] to identify weakness in " + weakest.getName() + "!\nDeals " + damage + " bonus damage!";
                }
                return this.name + " attempted [Focused Observation] but lacks Mana!";
            }
            case 3 -> { // Bursting 3.0
                int cost = scaledManaCost(45);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(20);
                    int hitDamage = scaledDamage(16);
                    int hits = 3;
                    int total = hitDamage * hits;
                    for (int i = 0; i < hits; i++) {
                        target.takeDamage(hitDamage);
                    }
                    return this.name + " unleashes [Bursting 3.0] on " + target.getName() + "!"
                        + "\nHits " + hits + " times for " + hitDamage + " damage each (" + total + " total)!";
                }
                return this.name + " attempted [Bursting 3.0] but lacks Mana!";
            }
            default -> { return this.name + " remains silent and composed."; }
        }
    }
}
