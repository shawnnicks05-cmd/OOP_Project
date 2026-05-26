package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Jin - Fast Assassin Boss
 * @author user
 */
public class Jin extends GameBoss {

    public Jin() {
        super("Jin", "Fast Assassin Boss", "Random", "Burst / Speed Pierce");
        this.hpBoss = 80;
        this.maxHp = 80;
        this.mana = 70;
        this.maxMana = 70;
        this.rage = 0;
        this.defence = 10;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Speed Submission", "Instant Deadline"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Swift Movement"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Fast Assassin Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 17;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = 19;
        target.takeDamage(baseDamage);
        this.addRage(12);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Lightning-fast deadlines! DoT active!" : "";
        return this.name + " strikes " + target.getName() + " with blinding speed for " + baseDamage + " damage! " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Speed Submission
                if (this.mana >= 40) {
                    this.mana -= 40;
                    this.addRage(16);
                    int damage = 30;
                    target.takeDamage(damage);
                    target.takeDamage(15); // Combo
                    return this.name + " executes [Speed Submission] on " + target.getName() + "!\nFast combo attacks! Total damage: " + (damage + 15) + "!";
                }
                return this.name + " attempted [Speed Submission] but lacks Mana!";
            }
            case 2 -> { // Instant Deadline
                if (this.mana >= 50) {
                    this.mana -= 50;
                    this.addRage(20);
                    int damage = 48;
                    target.takeDamage(damage);
                    return this.name + " delivers an [Instant Deadline] to " + target.getName() + "!\nImmediate burst damage! Deals " + damage + "!";
                }
                return this.name + " attempted [Instant Deadline] but lacks Mana!";
            }
            default -> { return this.name + " types at lightning speed..."; }
        }
    }
}
