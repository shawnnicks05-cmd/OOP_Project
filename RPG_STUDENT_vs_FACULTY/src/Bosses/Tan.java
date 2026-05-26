package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Hard Mode Couple Boss: Tan (Part 1)
 * @author user
 */
public class Tan extends GameBoss {

    private boolean hasPartner = false;

    public Tan() {
        super("Tan", "Dual Faculty Bosses", "EXTREME", "Synchronized Ultimate / Multi-Element Trauma");
        this.hpBoss = 150;
        this.maxHp = 150;
        this.mana = 120;
        this.maxMana = 120;
        this.rage = 0;
        this.defence = 18;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Terror Recitation", "Impossible Standards", "Sharp Criticism"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Dominance Aura"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Dual Faculty Boss (Tan)"
        };
    }

    @Override
    public int defend() {
        return this.defence + 32;
    }

    public void setHasPartner(boolean hasPartner) {
        this.hasPartner = hasPartner;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));
        int baseDamage = 28;
        target.takeDamage(baseDamage);
        this.addRage(16);

        String partnerBonus = hasPartner ? " [Bayucot gains bonus speed!]" : "";
        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Nightmare corridor closes in! Unshieldable DoT active!" : "";
        return this.name + " strikes " + target.getName() + " with authority for " + baseDamage + " damage!" + partnerBonus + " " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Terror Recitation
                if (this.mana >= 55) {
                    this.mana -= 55;
                    this.addRage(22);
                    int damage = 50;
                    target.takeDamage(damage);
                    return this.name + " delivers [Terror Recitation] on " + target.getName() + "!\nMassive confidence damage! Deals " + damage + "!";
                }
                return this.name + " attempted [Terror Recitation] but lacks Mana!";
            }
            case 2 -> { // Impossible Standards
                if (this.mana >= 60) {
                    this.mana -= 60;
                    this.addRage(24);
                    return this.name + " sets [Impossible Standards]!\nReduces all healing effects for the team!";
                }
                return this.name + " attempted [Impossible Standards] but lacks Mana!";
            }
            case 3 -> { // Sharp Criticism
                if (this.mana >= 50) {
                    this.mana -= 50;
                    this.addRage(20);
                    int damage = 45;
                    target.takeDamage(damage);
                    return this.name + " unleashes [Sharp Criticism] on " + target.getName() + "!\nHeavy single-target damage! Deals " + damage + "!";
                }
                return this.name + " attempted [Sharp Criticism] but lacks Mana!";
            }
            default -> { return this.name + " observes coldly..."; }
        }
    }
}
