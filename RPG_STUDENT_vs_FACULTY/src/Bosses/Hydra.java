package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 1 Boss: Hydra of Academics
 * Multi-Headed Composite Boss made of Miro, Cuizon, and Leeroy as its three heads
 * @author user
 */
public class Hydra extends GameBoss {

    private GameBoss miroHead;
    private GameBoss cuizonHead;
    private GameBoss leeroyHead;
    private Random rand = new Random();

    public Hydra() {
        super("Hydra", "Multi-Headed Academic Monster", "Easy-Medium", "Mental / Psychological");
        this.miroHead = new Miro();
        this.cuizonHead = new Cuizon();
        this.leeroyHead = new Leeroy();

        this.hpBoss = 100;
        this.maxHp = 100;
        this.mana = 80;
        this.maxMana = 80;
        this.rage = 0;
        this.defence = 12;
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Miro Head", "Cuizon Head", "Leeroy Head"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Multiple Heads"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Miro Head HP: " + this.miroHead.getHp() + "/" + this.miroHead.getHp(),
            "Cuizon Head HP: " + this.cuizonHead.getHp() + "/" + this.cuizonHead.getHp(),
            "Leeroy Head HP: " + this.leeroyHead.getHp() + "/" + this.leeroyHead.getHp()
        };
    }

    @Override
    public int defend() {
        return this.defence + 22;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets to attack.";

        GameCharacter chosenTarget = null;
        for (GameCharacter student : partyStudents) {
            if (student.getPosition().equalsIgnoreCase("Front") && student.getHp() > 0) {
                chosenTarget = student;
                break;
            }
        }

        if (chosenTarget == null) {
            for (GameCharacter student : partyStudents) {
                if (student.getHp() > 0) {
                    chosenTarget = student;
                    break;
                }
            }
        }

        if (chosenTarget != null) {
            int baseDamage = 18;
            chosenTarget.takeDamage(baseDamage);
            this.addRage(11);

            String rageAlert = this.isEnragedDoTActive() ? " \n[WARN] Hydra is ENRAGED! Stress DoT active!" : "";
            return this.name + " attacks with all three heads on " + chosenTarget.getName() + " for " + baseDamage + " damage!" + rageAlert;
        }

        return this.name + " has no targets to attack.";
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";

        switch (skillNumber) {
            case 1 -> { // Miro Head attack
                String result = this.miroHead.basicAttack(partyStudents);
                this.addRage(12);
                return "[MIRO HEAD] " + result;
            }
            case 2 -> { // Cuizon Head attack
                String result = this.cuizonHead.basicAttack(partyStudents);
                this.addRage(13);
                return "[CUIZON HEAD] " + result;
            }
            case 3 -> { // Leeroy Head attack
                String result = this.leeroyHead.basicAttack(partyStudents);
                this.addRage(14);
                return "[LEEROY HEAD] " + result;
            }
            default -> { return this.name + " hesitates..."; }
        }
    }

    public void damageHead(String headName, int damage) {
        switch (headName.toLowerCase()) {
            case "miro" -> this.miroHead.takeDamage(damage);
            case "cuizon" -> this.cuizonHead.takeDamage(damage);
            case "leeroy" -> this.leeroyHead.takeDamage(damage);
        }
    }

    public GameBoss getMiroHead() { return this.miroHead; }
    public GameBoss getCuizonHead() { return this.cuizonHead; }
    public GameBoss getLeeroyHead() { return this.leeroyHead; }
}
