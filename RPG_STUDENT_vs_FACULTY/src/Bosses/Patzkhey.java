package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random Boss: Patzkhey - Tactical Debuffer
 * @author user
 */
public class Patzkhey extends GameBoss {

    public Patzkhey() {
        super("Patzkhey", "Tactical Debuffer", "Random", "Corrosive / Ink Damage");
        this.hpBoss = 88;
        this.maxHp = 88;
        this.mana = 78;
        this.maxMana = 78;
        this.rage = 0;
        this.defence = 11;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Correction Mark", "Heavy Requirement"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Sharp Eye"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Tactical Debuffer"
        };
    }

    @Override
    public int defend() {
        return this.defence + 21;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = scaledDamage(17);
        target.takeDamage(baseDamage);
        this.addRage(11);

        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Red pen bleeds through! Un-cleansable DoT active!" : "";
        return this.name + " marks " + target.getName() + " with an ink stain for " + baseDamage + " damage! " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Correction Mark
                int cost = scaledManaCost(35);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(15);
                    return this.name + " places a [Correction Mark] on " + target.getName() + "!\nReduces attack power!";
                }
                return this.name + " attempted [Correction Mark] but lacks Mana!";
            }
            case 2 -> { // Heavy Requirement
                int cost = scaledManaCost(40);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(17);
                    int damage = scaledDamage(28);
                    target.takeDamage(damage);
                    return this.name + " imposes a [Heavy Requirement] on " + target.getName() + "!\nApplies stamina drain and deals " + damage + " damage!";
                }
                return this.name + " attempted [Heavy Requirement] but lacks Mana!";
            }
            default -> { return this.name + " scribbles furiously..."; }
        }
    }
}
