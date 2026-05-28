package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Level 1 Boss: Hydra of Academics
 * Multi-Headed Composite Boss made of Maru, Joveh, and Lerui as its three heads
 * @author user
 */
public class Hydra extends GameBoss {

    private GameBoss maruHead;
    private GameBoss jovehHead;
    private GameBoss leruiHead;
    private Random rand = new Random();

    public Hydra() {
        super("Hydra", "Multi-Headed Academic Monster", "Easy-Medium", "Mental / Psychological");
        this.maruHead = new Maru();
        this.jovehHead = new Joveh();
        this.leruiHead = new Lerui();

        // Combined health metrics for the ultimate composite boss entity
        this.hpBoss = 300; 
        this.maxHp = 300;
        this.mana = 100;
        this.maxMana = 100;
        this.rage = 0;
        this.defence = 12;
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        return new String[] {"Maru's Strategic Move", "Joveh's Speed Blitz", "Lerui's Heavy Slam"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Multiple Heads"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[]{
            "Main Body HP: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "[Head 1] Maru HP: " + this.maruHead.getHp() + "/" + this.maruHead.getMaxHp(),
            "[Head 2] Joveh HP: " + this.jovehHead.getHp() + "/" + this.jovehHead.getMaxHp(),
            "[Head 3] Lerui HP: " + this.leruiHead.getHp() + "/" + this.leruiHead.getMaxHp()
        };
    }

    @Override
    public int defend() {
        return this.defence + 22;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets to attack.";

        // Pick a random living head to perform the basic attack strategy!
        int activeHead = rand.nextInt(3) + 1;
        this.addRage(11);

        switch (activeHead) {
            case 1 -> {
                return "[MARU HEAD BASIC] " + this.maruHead.basicAttack(partyStudents);
            }
            case 2 -> {
                return "[JOVEH HEAD BASIC] " + this.jovehHead.basicAttack(partyStudents);
            }
            default -> {
                return "[LERUI HEAD BASIC] " + this.leruiHead.basicAttack(partyStudents);
            }
        }
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";
        
        // Randomly select skill slot 1, 2, or 3 belonging to the internal sub-bosses
        int randomSkillSelection = rand.nextInt(3) + 1; 

        switch (skillNumber) {
            case 1 -> { // Maru Head attack sequence
                this.addRage(12);
                return "[MARU HEAD SKILL] " + this.maruHead.useSkills(randomSkillSelection, partyStudents);
            }
            case 2 -> { // Joveh Head attack sequence
                this.addRage(13);
                return "[JOVEH HEAD SKILL] " + this.jovehHead.useSkills(randomSkillSelection, partyStudents);
            }
            case 3 -> { // Lerui Head attack sequence
                this.addRage(14);
                return "[LERUI HEAD SKILL] " + this.leruiHead.useSkills(randomSkillSelection, partyStudents);
            }
            default -> { return this.name + " hesitates..."; }
        }
    }

    /**
     * Allocates targeted damage directly to sub-boss components and 
     * syncs structural health changes back to the main layout frame tracker.
     */
    public void damageHead(String headName, int damage) {
        switch (headName.toLowerCase()) {
            case "maru" -> {
                this.maruHead.takeDamage(damage);
                this.hpBoss = Math.max(0, this.hpBoss - damage);
            }
            case "joveh" -> {
                this.jovehHead.takeDamage(damage);
                this.hpBoss = Math.max(0, this.hpBoss - damage);
            }
            case "lerui" -> {
                this.leruiHead.takeDamage(damage);
                this.hpBoss = Math.max(0, this.hpBoss - damage);
            }
        }
    }

    public GameBoss getMaruHead() { return this.maruHead; }
    public GameBoss getJovehHead() { return this.jovehHead; }
    public GameBoss getLeruiHead() { return this.leruiHead; }
}
