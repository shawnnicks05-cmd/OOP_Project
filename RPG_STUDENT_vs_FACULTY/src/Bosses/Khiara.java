package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Khiara - Trickster Boss
 * @author user
 */
public class Khiara extends GameBoss {

    public Khiara() {
        super("Khiara", "Trickster Boss", "Random", "Chaos / Randomized Damage");
        this.hpBoss = 85;
        this.maxHp = 85;
        this.mana = 70;
        this.maxMana = 70;
        this.rage = 0;
        this.defence = 9;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Mood Shift", "Confusing Question"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Unpredictable"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Trickster Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 18;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = 15 + rand.nextInt(10);
        target.takeDamage(baseDamage);
        this.addRage(11);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Chaos aura shifts into overdrive! Element-swapping DoT active!" : "";
        return this.name + " strikes " + target.getName() + " with unpredictable chaos for " + baseDamage + " damage! " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Mood Shift
                if (this.mana >= 35) {
                    this.mana -= 35;
                    this.addRage(15);
                    return this.name + " shifts her mood with [Mood Shift]!\nBattle effects change randomly!";
                }
                return this.name + " attempted [Mood Shift] but lacks Mana!";
            }
            case 2 -> { // Confusing Question
                if (this.mana >= 40) {
                    this.mana -= 40;
                    this.addRage(17);
                    int damage = 32;
                    target.takeDamage(damage);
                    return this.name + " asks a [Confusing Question] to " + target.getName() + "!\nCauses confusion and deals " + damage + " damage!";
                }
                return this.name + " attempted [Confusing Question] but lacks Mana!";
            }
            default -> { return this.name + " giggles mischievously..."; }
        }
    }
}
