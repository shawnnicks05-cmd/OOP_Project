package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 3 Boss: Cuizon - Speed Examiner
 * @author user
 */
public class Cuizon extends GameBoss {

    public Cuizon() {
        super("Cuizon", "Speed Examiner", "Medium", "Time-Based / Speed Damage");
        this.hpBoss = 90;
        this.maxHp = 90;
        this.mana = 75;
        this.maxMana = 75;
        this.rage = 0;
        this.defence = 11;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Speed Quiz", "Time Limit", "Unexpected Question"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Fast Thinker"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: Speed Examiner"
        };
    }

    @Override
    public int defend() {
        return this.defence + 22;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        Random rand = new Random();
        int hits = 2;
        int totalDamage = 0;

        StringBuilder result = new StringBuilder();
        result.append(this.name + " delivers rapid attacks!\n");

        for (int i = 0; i < hits && !partyStudents.isEmpty(); i++) {
            GameCharacter target = null;
            for (GameCharacter student : partyStudents) {
                if (student.getHp() > 0) {
                    target = student;
                    break;
                }
            }

            if (target != null) {
                int damage = 12;
                target.takeDamage(damage);
                totalDamage += damage;
                result.append("- Hit ").append(i + 1).append(" on ").append(target.getName()).append(" for ").append(damage).append(" damage!\n");
            }
        }

        this.addRage(14);
        String rageAlert = this.isEnragedDoTActive() ? "[WARN] Time pressure intensifies! DoT active!" : "";
        result.append("Total damage: ").append(totalDamage).append(". ").append(rageAlert);

        return result.toString();
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        Random rand = new Random();
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> { // Speed Quiz
                if (this.mana >= 35) {
                    this.mana -= 35;
                    this.addRage(16);
                    int damage = 28;
                    target.takeDamage(damage);
                    target.takeDamage(15); // Second hit
                    return this.name + " launches [Speed Quiz] - a multi-hit assault on " + target.getName() + "!\nDeals " + (damage + 15) + " total damage!";
                }
                return this.name + " attempted [Speed Quiz] but lacks Mana!";
            }
            case 2 -> { // Time Limit
                if (this.mana >= 40) {
                    this.mana -= 40;
                    this.addRage(17);
                    return this.name + " declares [Time Limit]!\nAll students' cooldown recovery is reduced!";
                }
                return this.name + " attempted [Time Limit] but lacks Mana!";
            }
            case 3 -> { // Unexpected Question
                if (this.mana >= 45) {
                    this.mana -= 45;
                    this.addRage(19);
                    int damage = 38;
                    target.takeDamage(damage);
                    return this.name + " springs an [Unexpected Question] on " + target.getName() + "!\nCritical strike! Deals " + damage + " damage!";
                }
                return this.name + " attempted [Unexpected Question] but lacks Mana!";
            }
            default -> { return this.name + " checks the clock..."; }
        }
    }
}
