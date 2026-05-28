package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Ghuardio - Defensive Tank Boss
 * @author user
 */
public class Ghuardio extends GameBoss {

    public Ghuardio() {
        super("Ghuardio", "Defensive Tank Boss", "Random", "Blunt Heavy / Block Barrier Damage");
        this.hpBoss = 125;
        this.maxHp = 125;
        this.mana = 70;
        this.maxMana = 70;
        this.rage = 0;
        this.defence = 20;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Wall of Requirements", "Knowledge Lock"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Iron Discipline"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Defensive Tank Boss"
        };
    }

    @Override
    public int defend() {
        return this.defence + 40;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = scaledDamage(20);
        String result = attackPlayerWithRoll(target, baseDamage, "retaliates against", target.isTaunted());
        this.addRage(10);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Friction-burn damage reflected! DoT active!" : "";
        return result + " " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        switch (skillNumber) {
            case 1 -> { // Wall of Requirements
                int cost = scaledManaCost(50);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(15);
                    return this.name + " erects a [Wall of Requirements]!\nMassive shield reduces all incoming damage!";
                }
                return this.name + " attempted [Wall of Requirements] but lacks Mana!";
            }
            case 2 -> { // Knowledge Lock
                int cost = scaledManaCost(45);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(14);
                    return this.name + " activates [Knowledge Lock]!\nPrevents healing temporarily!";
                }
                return this.name + " attempted [Knowledge Lock] but lacks Mana!";
            }
            default -> { return this.name + " stands firm..."; }
        }
    }
}
