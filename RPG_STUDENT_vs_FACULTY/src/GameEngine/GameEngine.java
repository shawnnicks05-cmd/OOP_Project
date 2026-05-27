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
    private int bossRound = 0;
    private static final BossType[] RANDOM_BOSSES = {
    BossType.KYARHEY, BossType.TIMOTHEH, BossType.AHZZEE,
    BossType.JEHCEY, BossType.BHARDIAN, BossType.PATZKHEY,
    BossType.JHIEN, BossType.GHUARDIO
    };
    
    public enum GameState {
        MENU, BOSS_SELECT, IN_BATTLE, BOSS_DEFEATED, PARTY_DEFEATED, GAME_OVER
    }
    public GameBoss spawnNextBoss() {
    bossRound++;

    if (bossRound <= 3) {
        // Rounds 1-3: random easy bosses
        GameBoss boss = spawnBoss(RANDOM_BOSSES[rand.nextInt(RANDOM_BOSSES.length)]);
        this.lastBattleMessage = "[Round " + bossRound + "/10] Random encounter: " + boss.getName() + " appears!";
        return boss;
    }

    if (bossRound == 4) {
        GameBoss boss = spawnBoss(BossType.MARU);
        this.lastBattleMessage = "[Round 4/10] HYDRA HEAD 1: " + boss.getName() + " emerges!";
        return boss;
    }

    if (bossRound == 5) {
        GameBoss boss = spawnBoss(BossType.JOVEH);
        this.lastBattleMessage = "[Round 5/10] HYDRA HEAD 2: " + boss.getName() + " emerges!";
        return boss;
    }

    if (bossRound == 6) {
        GameBoss boss = spawnBoss(BossType.LERUI);
        this.lastBattleMessage = "[Round 6/10] HYDRA HEAD 3: " + boss.getName() + " emerges!";
        return boss;
    }

    if (bossRound == 7) {
        GameBoss boss = spawnBoss(BossType.JENATEH);
        this.lastBattleMessage = "[Round 7/10] " + boss.getName() + " appears!";
        return boss;
    }

    if (bossRound == 8) {
        GameBoss boss = spawnBoss(BossType.LAPETRALBEY);
        this.lastBattleMessage = "[Round 8/10] " + boss.getName() + " appears!";
        return boss;
    }

    if (bossRound == 9) {
        // Alyzeh first, Bharkyot next round (9b handled by controller)
        GameBoss boss = spawnBoss(BossType.ALYZEH);
        this.lastBattleMessage = "[Round 9/10] COUPLE BOSS: Alyzeh and Bharkyot appear together!";
        return boss;
    }

    if (bossRound == 10) {
        // Bharkyot — second half of the couple
        GameBoss boss = spawnBoss(BossType.BHARKYOT);
        this.lastBattleMessage = "[Round 9b/10] Bharkyot steps forward!";
        return boss;
    }

    if (bossRound == 11) {
        GameBoss boss = spawnBoss(BossType.HYDRA);
        this.lastBattleMessage = "[Round 10/10] *** THE HYDRA AWAKENS! TRUE FINAL BOSS! ***";
        return boss;
    }

    // All bosses cleared
    this.gameState = GameState.GAME_OVER;
    this.lastBattleMessage = "All bosses defeated! YOU WIN!";
    return null;
}

public int getBossRound() { return bossRound; }

public boolean isHydraHeadPhase() {
    return bossRound >= 4 && bossRound <= 6;
}

public boolean isCoupleBossPhase() {
    return bossRound == 9 || bossRound == 10;
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
        
        this.currentBoss.setRage(0);
        this.currentTurn = 0;
        this.isPlayerTurn = true;
        this.gameState = GameState.IN_BATTLE;
        this.lastBattleMessage = this.currentBoss.getName() + " appears!";
        for (GameCharacter s : partyStudents) {   // reset party morale
        s.setMorale(100);
    }
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
    if (!isPlayerTurn || currentBoss == null) return "It's not your turn!";

    // Build the boss list the characters expect
    ArrayList<GameBoss> bossList = new ArrayList<>();
    bossList.add(currentBoss);

    String result;
    if (skillChoice == 0) {
        result = actor.basicAttack(bossList);      // ← uses YOUR character's method
    } else {
        result = actor.useSkills(skillChoice, bossList);  // ← uses YOUR character's skills
    }

    if (currentBoss.getHp() <= 0) {
        this.gameState = GameState.BOSS_DEFEATED;
        return result + "\n" + currentBoss.getName() + " has been defeated!";
    }

    this.isPlayerTurn = false;
    this.currentTurn++;
    return result;
}

    public String executeBossTurn() {
    if (isPlayerTurn || currentBoss == null) return "Boss turn skipped.";

    StringBuilder result = new StringBuilder();

    // Target sequence: Front → Above → Below
    GameCharacter target = null;
    String[] order = {"Front", "Above", "Below"};
    for (String pos : order) {
        for (GameCharacter s : partyStudents) {
            if (s.getPosition().equalsIgnoreCase(pos) && s.getHp() > 0) {
                target = s;
                break;
            }
        }
        if (target != null) break;
    }

    if (target == null) {
        this.gameState = GameState.PARTY_DEFEATED;
        return "All students have been defeated!";
    }

    // Pass only the target as a single-element list
    ArrayList<GameCharacter> targetList = new ArrayList<>();
    targetList.add(target);
    String bossAction = currentBoss.basicAttack(targetList);
    result.append(bossAction).append("\n");

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
