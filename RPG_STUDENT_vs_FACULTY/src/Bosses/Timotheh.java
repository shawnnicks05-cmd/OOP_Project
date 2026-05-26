package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Timotheh - Balanced Fighter
 * @author user
 */
public class Timotheh extends GameBoss {

    public Timotheh() {
        super("Timotheh", "Balanced Fighter", "Random", "Adaptive / Physical Strike");
        this.hpBoss = 100;
        this.maxHp = 100;
        this.mana = 75;
        this.maxMana = 75;
        this.rage = 0;
        this.defence = 13;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Quick Reminder", "Class Participation"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Adaptive"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Balanced Fighter"
        };
    }

    @Override
    public int defend() {
        return this.defence + 24;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = 20;
        target.takeDamage(baseDamage);
        this.addRage(12);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Timotheh paces aggressively! Rapid-fire pop questions active!" : "";
        return this.name + " strikes " + target.getName() + " with adaptive precision for " + baseDamage + " damage! " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();

        switch (skillNumber) {
            case 1 -> { // Quick Reminder
                if (this.mana >= 30) {
                    this.mana -= 30;
                    this.addRage(14);
                    return this.name + " gives a [Quick Reminder]!\nInterrupts all charging attacks!";
                }
                return this.name + " attempted [Quick Reminder] but lacks Mana!";
            }
            case 2 -> { // Class Participation
                if (this.mana >= 40) {
                    this.mana -= 40;
                    this.addRage(16);
                    return this.name + " demands [Class Participation]!\nBuffs nearby enemies!";
                }
                return this.name + " attempted [Class Participation] but lacks Mana!";
            }
            default -> { return this.name + " thinks of another pop quiz..."; }
        }
    }
}
