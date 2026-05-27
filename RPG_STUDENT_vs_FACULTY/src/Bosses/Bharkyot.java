package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Hard Mode Couple Boss: Bharkyot (Part 2)
 * @author user
 */
public class Bharkyot extends GameBoss {

    private boolean hasPartner = false;

    public Bharkyot() {
        super("Bharkyot", "Dual Faculty Bosses", "EXTREME", "Synchronized Ultimate / Multi-Element Trauma");
        this.hpBoss = 150;
        this.maxHp = 150;
        this.mana = 120;
        this.maxMana = 120;
        this.rage = 0;
        this.defence = 18;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Surprise Practical Exam", "Information Overload", "Group Pressure"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Unending Tasks"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Dual Faculty Boss (Bharkyot)"
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
        String result = attackPlayerWithRoll(target, baseDamage, "strikes", target.isTaunted());
        this.addRage(16);

        String partnerBonus = hasPartner ? " [Tan gains bonus speed!]" : "";
        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Nightmare corridor closes in! Unshieldable DoT active!" : "";
        return result + partnerBonus + " " + rageAlert;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Surprise Practical Exam
                if (this.mana >= 55) {
                    this.mana -= 55;
                    this.addRage(22);
                    int damage = 42;
                    StringBuilder damageLog = new StringBuilder();
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            damageLog.append(attackPlayerWithRoll(student, damage, "is hit by", student.isTaunted())).append("\n");
                        }
                    }
                    return this.name + " springs a [Surprise Practical Exam]!\nRandom AoE damage of " + damage + " to all!\n" + damageLog;
                }
                return this.name + " attempted [Surprise Practical Exam] but lacks Mana!";
            }
            case 2 -> { // Information Overload
                if (this.mana >= 60) {
                    this.mana -= 60;
                    this.addRage(24);
                    int damage = 30;
                    StringBuilder damageLog = new StringBuilder();
                    for (GameCharacter student : partyStudents) {
                        if (student.getHp() > 0) {
                            damageLog.append(attackPlayerWithRoll(student, damage, "is flooded by", student.isTaunted())).append("\n");
                        }
                    }
                    return this.name + " creates [Information Overload]!\nApplies confusion and panic! Deals " + damage + " to all!\n" + damageLog;
                }
                return this.name + " attempted [Information Overload] but lacks Mana!";
            }
            case 3 -> { // Group Pressure
                if (this.mana >= 65) {
                    this.mana -= 65;
                    this.addRage(26);
                    return this.name + " exerts [Group Pressure]!\nEntire team loses stamina!";
                }
                return this.name + " attempted [Group Pressure] but lacks Mana!";
            }
            default -> { return this.name + " loads up more assignments..."; }
        }
    }
}
