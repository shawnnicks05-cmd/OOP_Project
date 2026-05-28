package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Jehcey - Mentor Boss
 * @author user
 */
public class Jehcey extends GameBoss {

    public Jehcey() {
        super("Jehcey", "Mentor Boss", "Random", "Conceptual / Crushing Logic");
        this.hpBoss = 105;
        this.maxHp = 105;
        this.mana = 85;
        this.maxMana = 85;
        this.rage = 0;
        this.defence = 15;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Advanced Question", "Reality Check"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Experienced Teacher"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Mentor Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 26;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = scaledDamage(22);
        target.takeDamage(baseDamage);
        this.addRage(12);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] High expectations crush spirits! Intellectual DoT active!" : "";
        return this.name + " challenges " + target.getName() + " with calculated precision for " + baseDamage + " damage! " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Advanced Question
                int cost = scaledManaCost(45);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(18);
                    int damage = scaledDamage(38);
                    target.takeDamage(damage);
                    return this.name + " poses an [Advanced Question] to " + target.getName() + "!\nStrong single-target attack! Deals " + damage + " damage!";
                }
                return this.name + " attempted [Advanced Question] but lacks Mana!";
            }
            case 2 -> { // Reality Check
                int cost = scaledManaCost(50);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(20);
                    return this.name + " delivers a [Reality Check]!\nRemoves all overpowered buffs from the team!";
                }
                return this.name + " attempted [Reality Check] but lacks Mana!";
            }
            default -> { return this.name + " strokes his beard thoughtfully..."; }
        }
    }
}
