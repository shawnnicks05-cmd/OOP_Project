package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Ahzzee - Tech Boss
 * @author user
 */
public class Ahzzee extends GameBoss {

    public Ahzzee () {
        super("Ahzzee ", "Tech Boss", "Random", "Digital / System Damage");
        this.hpBoss = 95;
        this.maxHp = 95;
        this.mana = 80;
        this.maxMana = 80;
        this.rage = 0;
        this.defence = 12;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"System Crash", "Lag Spike"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Digital Mind"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Tech Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 20;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = 18;
        target.takeDamage(baseDamage);
        this.addRage(11);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Server overload! Screen-glitch DoT active!" : "";
        return this.name + " corrupts " + target.getName() + "'s UI for " + baseDamage + " damage! " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        switch (skillNumber) {
            case 1 -> { // System Crash
                if (this.mana >= 45) {
                    this.mana -= 45;
                    this.addRage(18);
                    return this.name + " triggers [System Crash]!\nDisables all skills temporarily!";
                }
                return this.name + " attempted [System Crash] but lacks Mana!";
            }
            case 2 -> { // Lag Spike
                if (this.mana >= 40) {
                    this.mana -= 40;
                    this.addRage(16);
                    int damage = 25;
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            student.takeDamage(damage);
                        }
                    }
                    return this.name + " creates a [Lag Spike]!\nSlows entire team and deals " + damage + " damage!";
                }
                return this.name + " attempted [Lag Spike] but lacks Mana!";
            }
            default -> { return this.name + " loads..."; }
        }
    }
}
