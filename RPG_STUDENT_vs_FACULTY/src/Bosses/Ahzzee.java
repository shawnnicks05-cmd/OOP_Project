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
        // IMPORTANT: no trailing space in name, otherwise the image path becomes "/assets/Ahzzee .png"
        super("Ahzzee", "Tech Boss", "Random", "Digital / System Damage");
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
        int baseDamage = scaledDamage(18);
        String result = attackPlayerWithRoll(target, baseDamage, "corrupts", target.isTaunted());
        this.addRage(11);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Server overload! Screen-glitch DoT active!" : "";
        return result + " " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        switch (skillNumber) {
            case 1 -> { // System Crash
                int cost = scaledManaCost(45);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(18);
                    return this.name + " triggers [System Crash]!\nDisables all skills temporarily!";
                }
                return this.name + " attempted [System Crash] but lacks Mana!";
            }
            case 2 -> { // Lag Spike
                int cost = scaledManaCost(40);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(16);
                    int damage = scaledDamage(25);
                    StringBuilder damageLog = new StringBuilder();
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            damageLog.append(attackPlayerWithRoll(student, damage, "hits", student.isTaunted())).append("\n");
                        }
                    }
                    return this.name + " creates a [Lag Spike]!\nSlows entire team and deals " + damage + " damage!\n" + damageLog;
                }
                return this.name + " attempted [Lag Spike] but lacks Mana!";
            }
            default -> { return this.name + " loads..."; }
        }
    }
}
