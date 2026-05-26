package GameEngine;

import Bosses.*;
import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

/**
 * GameEngine manages battle state, boss spawning, turn management, and communicates with the UI
 * @author user
 */
public class GameEngine {

    private GameBoss currentBoss;
    private ArrayList<GameCharacter> partyStudents;
    private Random rand = new Random();
    private int currentTurn = 0;
    private boolean isPlayerTurn = true;
    private String lastBattleMessage = "";
    private GameState gameState;

    public enum GameState {
        MENU, BOSS_SELECT, IN_BATTLE, BOSS_DEFEATED, PARTY_DEFEATED, GAME_OVER
    }

    public GameEngine() {
        this.partyStudents = new ArrayList<>();
        this.gameState = GameState.MENU;
    }

    // --- INITIALIZATION ---

    public void initializeParty(ArrayList<GameCharacter> students) {
        this.partyStudents = students;
    }

    public GameBoss spawnBoss(BossType bossType) {
        this.currentBoss = switch(bossType) {
            case HYDRA -> new Hydra();
            case MARU -> new Maru();
            case JOVEH-> new Joveh();
            case LERUI -> new Lerui();
            case JENATEH -> new Jenateh();
            case LAPETRALBEY -> new LaPetralbey();
            case KYARHEY -> new Kyarhey();
            case TIMOTHEH -> new Timotheh();
            case AHZZEE -> new Ahzzee();
            case JEHCEY -> new Jehcey();
            case BHARDIAN -> new Bhardian();
            case PATZKHEY -> new Patzkhey();
            case JHIEN -> new Jhien ();
            case GHUARDIO -> new Ghuardio();
            case ALYZEH -> new Alyzeh();
            case BHARKYOT -> new Bharkyot();
        };

        this.currentTurn = 0;
        this.isPlayerTurn = true;
        this.gameState = GameState.IN_BATTLE;
        this.lastBattleMessage = this.currentBoss.getName() + " appears!";
        return this.currentBoss;
    }

    public GameBoss spawnRandomBoss() {
        BossType[] randomBosses = {
            BossType.KYARHEY, BossType.TIMOTHEH, BossType.AHZZEE,
            BossType.JEHCEY, BossType.BHARDIAN, BossType.PATZKHEY,
            BossType.JHIEN, BossType.GHUARDIO
        };
        return spawnBoss(randomBosses[rand.nextInt(randomBosses.length)]);
    }

    // --- TURN MANAGEMENT ---

    public String executePlayerTurn(int skillChoice, GameCharacter actor) {
        if (!isPlayerTurn || currentBoss == null) {
            return "It's not your turn!";
        }

        StringBuilder result = new StringBuilder();

        // Player skill execution
        if (skillChoice == 0) {
            // Basic attack
            int damage = actor.getAttack() + rand.nextInt(10);
            currentBoss.takeDamage(damage);
            result.append(actor.getName()).append(" attacks the boss for ").append(damage).append(" damage!\n");
        } else {
            // Special skill
            result.append(actor.getName()).append(" uses a special skill!\n");
        }

        // Check if boss is defeated
        if (currentBoss.getHp() <= 0) {
            this.gameState = GameState.BOSS_DEFEATED;
            this.lastBattleMessage = currentBoss.getName() + " has been defeated!";
            return result.toString() + "\n" + this.lastBattleMessage;
        }

        this.isPlayerTurn = false;
        this.currentTurn++;
        return result.toString();
    }

    public String executeBossTurn() {
        if (isPlayerTurn || currentBoss == null) {
            return "Boss turn skipped.";
        }

        StringBuilder result = new StringBuilder();

        // Random skill selection
        int skillChoice = rand.nextInt(currentBoss.getSkillname().length);
        String bossAction = currentBoss.useSkills(skillChoice + 1, partyStudents);
        result.append(bossAction).append("\n");

        // Check if any students are alive
        boolean anyAlive = partyStudents.stream().anyMatch(s -> s.getHp() > 0);
        if (!anyAlive) {
            this.gameState = GameState.PARTY_DEFEATED;
            this.lastBattleMessage = "All students have been defeated!";
            return result.toString() + "\n" + this.lastBattleMessage;
        }

        this.isPlayerTurn = true;
        return result.toString();
    }

    // --- GETTERS ---

    public GameBoss getCurrentBoss() {
        return this.currentBoss;
    }

    public ArrayList<GameCharacter> getPartyStudents() {
        return this.partyStudents;
    }

    public boolean isPlayerTurn() {
        return this.isPlayerTurn;
    }

    public int getCurrentTurn() {
        return this.currentTurn;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public String getLastBattleMessage() {
        return this.lastBattleMessage;
    }

    public String getBattleStatus() {
        StringBuilder status = new StringBuilder();

        if (currentBoss != null) {
            status.append("=== BOSS: ").append(currentBoss.getName()).append(" ===\n");
            for (String stat : currentBoss.displayBossStats()) {
                status.append(stat).append("\n");
            }
        }

        status.append("\n=== PARTY ===\n");
        for (GameCharacter student : partyStudents) {
            if (student.getHp() > 0) {
                status.append(student.getName()).append(" - HP: ").append(student.getHp()).append("\n");
            }
        }

        status.append("\n=== TURN: ").append(currentTurn);
        status.append(" | ").append(isPlayerTurn ? "PLAYER TURN" : "BOSS TURN").append(" ===\n");

        return status.toString();
    }

    public String[] getBossSkills() {
        if (currentBoss != null) {
            return currentBoss.getSkillname();
        }
        return new String[] {"No boss active"};
    }

    public String[] getBossPassives() {
        if (currentBoss != null) {
            return currentBoss.getPassivename();
        }
        return new String[] {"No boss active"};
    }

    // --- STATE SETTERS ---

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public void setBattleMessage(String message) {
        this.lastBattleMessage = message;
    }

    // --- UTILITIES ---

    public void resetBattle() {
        this.currentBoss = null;
        this.currentTurn = 0;
        this.isPlayerTurn = true;
        this.lastBattleMessage = "";
        this.gameState = GameState.BOSS_SELECT;
    }

    public boolean isBossDead() {
        return currentBoss != null && currentBoss.getHp() <= 0;
    }

    public boolean isPartyDead() {
        return partyStudents.stream().allMatch(s -> s.getHp() <= 0);
    }

    public enum BossType {
        HYDRA, MARU, JOVEH, LERUI, JENATEH, LAPETRALBEY,
        KYARHEY, TIMOTHEH, AHZZEE, JEHCEY, BHARDIAN, PATZKHEY, JHIEN, GHUARDIO,
        ALYZEH, BHARKYOT
    }
}
