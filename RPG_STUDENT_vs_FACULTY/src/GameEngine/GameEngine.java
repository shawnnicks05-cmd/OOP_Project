package GameEngine;

import Bosses.*;
import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import Inventory.Item;
import Inventory.Potions;
import Inventory.EmptyInventoryException;

/**
 * GameEngine manages battle state, boss spawning, turn management, and communicates with the UI
 * @author user
 */
public class GameEngine {
    private int bossesDefeated = 0;
private int totalTurns = 0;
public int getBossesDefeated() { return bossesDefeated; }
public int getTotalTurns() { return totalTurns; }
    private GameBoss currentBoss;
    private ArrayList<GameCharacter> partyStudents;
    private Random rand = new Random();
    private int currentTurn = 0;
    private boolean isPlayerTurn = true;
    private String lastBattleMessage = "";
    private GameState gameState;
    private int bossRound = 0;
    private boolean coupleSecondPending = false;

    // --- Item inventory (ArrayList<Item> based system) ---

    private ArrayList<Item> inventory = new ArrayList<>();
    public enum PotionType { HP, MANA, REVIVE }
    private static final BossType[] RANDOM_BOSSES = {
    BossType.KYARHEY, BossType.TIMOTHEH, BossType.AHZZEE,
    BossType.JEHCEY, BossType.BHARDIAN, BossType.PATZKHEY,
    BossType.JHIEN, BossType.GHUARDIO
    };

    // Pre-generated boss schedule so rounds 1-4 don't repeat bosses
    private BossType[] round1to4Bosses = new BossType[4];
    private boolean scheduleGenerated = false;
    
    public enum GameState {
        MENU, BOSS_SELECT, IN_BATTLE, BOSS_DEFEATED, PARTY_DEFEATED, ESCAPED, GAME_OVER
    }

    private void generateBossScheduleIfNeeded() {
        if (scheduleGenerated) return;

        List<BossType> pool = new ArrayList<>(Arrays.asList(RANDOM_BOSSES));
        Collections.shuffle(pool, rand);

        for (int i = 0; i < round1to4Bosses.length; i++) {
            round1to4Bosses[i] = pool.get(i);
        }
        scheduleGenerated = true;
    }

    public GameBoss spawnNextBoss() {
        // Round system (max 6):
        // 1-4: easy random bosses
        // 5: couple boss (Alyzeh then Bharkyot, same round)
        // 6: Hydra final boss

        generateBossScheduleIfNeeded();

        // If Alyzeh is defeated during round 5, spawn Bharkyot without increasing the round count.
        if (bossRound == 5 && coupleSecondPending) {
            GameBoss boss = spawnBoss(BossType.BHARKYOT);
            this.lastBattleMessage = "[Round 5/6] COUPLE BOSS: " + boss.getName() + " joins the fight!";
            coupleSecondPending = false;
            return boss;
        }

        bossRound++;

        if (bossRound >= 1 && bossRound <= 4) {
            BossType scheduled = round1to4Bosses[bossRound - 1];
            GameBoss boss = spawnBoss(scheduled);
            this.lastBattleMessage = "[Round " + bossRound + "/6] Random encounter: " + boss.getName() + " appears!";
            return boss;
        }

        if (bossRound == 5) {
            GameBoss boss = spawnBoss(BossType.ALYZEH);
            this.lastBattleMessage = "[Round 5/6] COUPLE BOSS: " + boss.getName() + " appears!";
            coupleSecondPending = true;
            return boss;
        }

        if (bossRound == 6) {
            GameBoss boss = spawnBoss(BossType.HYDRA);
            this.lastBattleMessage = "[Round 6/6] *** FINAL BOSS: THE HYDRA AWAKENS! ***";
            return boss;
        }

        // All bosses cleared
        this.gameState = GameState.GAME_OVER;
        this.lastBattleMessage = "All bosses defeated! YOU WIN!";
        return null;
    }

    public int getBossRound() { return bossRound; }
    public boolean isCoupleSecondPending() { return coupleSecondPending; }
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
        // Reset potion stock at the start of each boss battle (tweak as desired)
        inventory.clear();
for (int i = 0; i < 3; i++) inventory.add(new Potions("HP Potion", "HP", 40));
for (int i = 0; i < 3; i++) inventory.add(new Potions("Mana Potion", "MANA", 40));
inventory.add(new Potions("Revive Potion", "REVIVE", 50));
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
    if (actor == null) return "No active character selected.";

    // Build the boss list the characters expect
    ArrayList<GameBoss> bossList = new ArrayList<>();
    bossList.add(currentBoss);

    String result;
    if (skillChoice == 0) {
        result = actor.basicAttack(bossList);      // ← uses YOUR character's method
    } else {
        if (actor.isSkillOnCooldown(skillChoice)) {
            return actor.getSkillDisplayName(skillChoice) + " can't be used for "
                + actor.getSkillCooldownRemaining(skillChoice) + " more rounds.";
        }
        result = actor.useSkills(skillChoice, bossList);  // ← uses YOUR character's skills
    }

    if (currentBoss.getHp() <= 0) {
        this.gameState = GameState.BOSS_DEFEATED;
        bossesDefeated++;
        return result + "\n" + currentBoss.getName() + " has been defeated!";
    }

    this.isPlayerTurn = false;
    this.currentTurn++;
    totalTurns++;
    return result;
}



    public String executeBossTurn() {
    if (isPlayerTurn || currentBoss == null) return "Boss turn skipped.";

    StringBuilder result = new StringBuilder();

    // Tick player status effects first so taunt durations decay properly.
    for (GameCharacter s : partyStudents) {
        s.tickStatusEffects();
    }

    // Alive targets
    ArrayList<GameCharacter> alive = new ArrayList<>();
    for (GameCharacter s : partyStudents) {
        if (s.getHp() > 0) alive.add(s);
    }

    if (alive.isEmpty()) {
        this.gameState = GameState.PARTY_DEFEATED;
        return "All students have been defeated!";
    }

    // Force bosses to prioritize taunting characters if they are active.
    ArrayList<GameCharacter> preferredTargets = getPreferredTargets(alive);

    // Snapshot alive before action (for death messages)
    java.util.HashSet<String> aliveBefore = new java.util.HashSet<>();
    for (GameCharacter s : alive) aliveBefore.add(s.getName());

    // Boss AI: try to use a skill if available and not on cooldown, else basic attack
    String bossAction = null;
    int skillSlots = Math.min(3, currentBoss.getSkillname() != null ? currentBoss.getSkillname().length : 0);
    int usedSkill = 0;

    if (skillSlots > 0 && rand.nextDouble() < 0.65) {
        // Try up to skillSlots times to find a skill not on cooldown
        for (int tries = 0; tries < skillSlots; tries++) {
            int skillChoice = rand.nextInt(skillSlots) + 1;
            if (currentBoss.isSkillOnCooldown(skillChoice)) continue;
            usedSkill = skillChoice;
            bossAction = currentBoss.useSkills(skillChoice, preferredTargets);
            break;
        }
    }

    if (bossAction == null) {
        bossAction = currentBoss.basicAttack(preferredTargets);
    } else {
        // Start cooldown only if the skill really fired (not a "lacks Mana" attempt)
        String lower = bossAction.toLowerCase();
        if (!lower.contains("lacks mana") && !lower.contains("attempted")) {
            currentBoss.startSkillCooldown(usedSkill);
            // Boss skill side-effect: demoralize the party
            for (GameCharacter s : alive) {
                s.reduceMorale(10);
            }
            bossAction += "\nThe party's morale drops by 10!";
        }
    }

    result.append(bossAction).append("\n");

    // Death messages
    for (GameCharacter s : partyStudents) {
        if (aliveBefore.contains(s.getName()) && s.getHp() <= 0) {
            result.append(s.getName()).append(" died!\n");
        }
    }

    boolean anyAlive = partyStudents.stream().anyMatch(s -> s.getHp() > 0);
    if (!anyAlive) {
        this.gameState = GameState.PARTY_DEFEATED;
        this.lastBattleMessage = "All students have been defeated!";
        // Don't append the defeat text here, the controller/UI will print it once.
        return result.toString();
    }

    this.isPlayerTurn = true;
    // Boss cooldowns tick down each boss action
    currentBoss.tickCooldowns();
    return result.toString();
}

    // --- Potion logic ---
    public int getHpPotions() {
    return (int) inventory.stream().filter(p -> p.getType().equals("HP")).count();
}
public int getManaPotions() {
    return (int) inventory.stream().filter(p -> p.getType().equals("MANA")).count();
}
public int getRevivePotions() {
    return (int) inventory.stream().filter(p -> p.getType().equals("REVIVE")).count();
}

    public String usePotion(PotionType potionType, GameCharacter actor) throws EmptyInventoryException {
    if (potionType == null) return "No potion selected.";

    switch (potionType) {
        case HP -> {
            Item item = inventory.stream()
                .filter(x -> x.getType().equals("HP")).findFirst().orElse(null);
            if (!(item instanceof Potions p)) throw new EmptyInventoryException("HP");
            if (actor.isDead()) return "Can't heal a defeated character. Use a revive potion.";
            inventory.remove(p);
            actor.heal(p.getValue());
            return actor.getName() + " uses " + p.getName() + " and restores " + p.getValue() + " HP!";
        }
        case MANA -> {
            Item item = inventory.stream()
                .filter(x -> x.getType().equals("MANA")).findFirst().orElse(null);
            if (!(item instanceof Potions p)) throw new EmptyInventoryException("Mana");
            if (actor.isDead()) return "Can't restore mana to a defeated character.";
            inventory.remove(p);
            actor.restoreMana(p.getValue());
            return actor.getName() + " uses " + p.getName() + " and restores " + p.getValue() + " Mana!";
        }
        case REVIVE -> {
            Item item = inventory.stream()
                .filter(x -> x.getType().equals("REVIVE")).findFirst().orElse(null);
            if (!(item instanceof Potions p)) throw new EmptyInventoryException("Revive");
            GameCharacter target = partyStudents.stream()
                .filter(GameCharacter::isDead).findFirst().orElse(null);
            if (target == null) return "There is no defeated teammate to revive!";
            inventory.remove(p);
            target.revive(p.getValue(), p.getValue());
            return actor.getName() + " uses " + p.getName() + "! " + target.getName() + " returns to battle!";
        }
        default -> { return "Unknown potion type."; }
    }
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

    public void consumePlayerTurn() {
        this.isPlayerTurn = false;
        this.currentTurn++;
        this.totalTurns++;
    }

    // --- UTILITIES ---

    public void resetBattle() {
        this.currentBoss = null;
        this.currentTurn = 0;
        this.isPlayerTurn = true;
        this.lastBattleMessage = "";
        this.gameState = GameState.BOSS_SELECT;
        this.bossRound = 0;
        this.coupleSecondPending = false;
        this.scheduleGenerated = false;
        this.bossesDefeated = 0;
        this.totalTurns = 0;
    }

    public boolean isBossDead() {
        return currentBoss != null && currentBoss.getHp() <= 0;
    }

    public boolean isPartyDead() {
        return partyStudents.stream().allMatch(s -> s.getHp() <= 0);
    }

    private ArrayList<GameCharacter> getPreferredTargets(ArrayList<GameCharacter> alive) {
        ArrayList<GameCharacter> taunted = new ArrayList<>();
        for (GameCharacter student : alive) {
            if (student.isTaunted()) {
                taunted.add(student);
            }
        }
        return taunted.isEmpty() ? alive : taunted;
    }

    public String applyTaunt(GameCharacter actor, int turns) {
        if (actor == null || actor.isDead()) {
            return "No valid taunt target.";
        }
        actor.applyTaunt(turns);
        return actor.getName() + " challenges the boss and draws its attention for " + turns + " turn(s)!";
    }

    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    public ArrayList<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public int getItemCountByType(String type) {
        return (int) inventory.stream().filter(i -> i.getType().equals(type)).count();
    }

    public enum BossType {
        HYDRA, MARU, JOVEH, LERUI, JENATEH, LAPETRALBEY,
        KYARHEY, TIMOTHEH, AHZZEE, JEHCEY, BHARDIAN, PATZKHEY, JHIEN, GHUARDIO,
        ALYZEH, BHARKYOT
    }
    public String attemptFlee(GameCharacter actor) {
    double fleeChance = 0.40;
    if (rand.nextDouble() < fleeChance) {
        this.lastBattleMessage = actor.getName() + " successfully fled the battle!";
        this.gameState = GameState.ESCAPED;
        this.currentBoss = null;
        this.isPlayerTurn = false;
        this.currentTurn++;
        this.totalTurns++;
        return actor.getName() + " successfully fled the battle!";
    }
    int penalty = 20;
    actor.takeDamage(penalty);
    if (actor.isDead()) {
        this.gameState = GameState.PARTY_DEFEATED;
    }
    isPlayerTurn = false;
    currentTurn++;
    totalTurns++;
    return actor.getName() + " failed to flee! " + currentBoss.getName()
        + " punishes them for " + penalty + " damage!";
}
}
