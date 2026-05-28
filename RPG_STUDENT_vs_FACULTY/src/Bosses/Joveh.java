package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 3 Boss: Joveh - Speed Examiner
 * @author user
 */
public class Joveh extends GameBoss {

    public Joveh() {
        super("Joveh", "Speed Examiner", "Medium", "Time-Based / Speed Damage");
        this.hpBoss = 90;
        this.maxHp = 90;
        this.mana = 75;
        this.maxMana = 75;
        this.rage = 0;
        this.defence = 11;
        applyDifficultyScaling();
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
                int damage = scaledDamage(12);
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
                int cost = scaledManaCost(35);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(16);
                    int damage = scaledDamage(28);
                    target.takeDamage(damage);
                    int second = scaledDamage(15);
                    target.takeDamage(second); // Second hit
                    return this.name + " launches [Speed Quiz] - a multi-hit assault on " + target.getName() + "!\nDeals " + (damage + second) + " total damage!";
                }
                return this.name + " attempted [Speed Quiz] but lacks Mana!";
            }
            case 2 -> { // Time Limit
                int cost = scaledManaCost(40);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(17);
                    return this.name + " declares [Time Limit]!\nAll students' cooldown recovery is reduced!";
                }
                return this.name + " attempted [Time Limit] but lacks Mana!";
            }
            case 3 -> { // Unexpected Question
                int cost = scaledManaCost(45);
                if (this.mana >= cost) {
                    this.mana -= cost;
                    this.addRage(19);
                    int damage = scaledDamage(38);
                    target.takeDamage(damage);
                    return this.name + " springs an [Unexpected Question] on " + target.getName() + "!\nCritical strike! Deals " + damage + " damage!";
                }
                return this.name + " attempted [Unexpected Question] but lacks Mana!";
            }
            default -> { return this.name + " checks the clock..."; }
        }
    }
}
