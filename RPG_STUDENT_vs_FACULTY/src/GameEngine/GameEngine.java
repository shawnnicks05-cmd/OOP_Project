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
    // =========================================================
    // GAME FLOW (high level):
    // Menu -> Character select -> Battle start (spawnNextBoss)
    // -> Player turn (executePlayerTurn) -> Boss turn (executeBossTurn)
    // -> Victory/Defeat -> Next wave -> Save/Load
    // =========================================================
    private int bossesDefeated = 0;
    private int totalTurns = 0;
    private int gold = 125;
    public int getGold() { return gold; }
    public void addGold(int amount) { if (amount > 0) gold += amount; }
    public boolean spendGold(int amount) {
        if (amount <= 0 || amount > gold) return false;
        gold -= amount;
        return true;
    }
public int getBossesDefeated() { return bossesDefeated; }
public int getTotalTurns() { return totalTurns; }
    // Active bosses in the current wave (normally 1).
    private final ArrayList<GameBoss> activeBosses = new ArrayList<>();
    private ArrayList<GameCharacter> partyStudents;
    private Random rand = new Random();
    private int currentTurn = 0;
    private boolean isPlayerTurn = true;
    private String lastBattleMessage = "";
    private GameState gameState;
    private int bossRound = 0;
    // Round 5 special case: Alyzeh then Bharkyot (individually).
    // 0 = not started, 1 = Alyzeh spawned (Bharkyot pending), 2 = Bharkyot spawned (done)
    private int couplePhase = 0;

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
        // ----------------------------
        // Battle start / next-wave flow
        // ----------------------------
        // Round system (max 6):
        // 1-4: easy random bosses
        // 5: couple boss (Alyzeh then Bharkyot, same round)
        // 6: Hydra final boss

        generateBossScheduleIfNeeded();

        // If we're in Round 5 and Alyzeh has already been spawned, spawn Bharkyot next
        // WITHOUT advancing to Round 6 (Hydra).
        if (bossRound == 5 && couplePhase == 1) {
            couplePhase = 2;
            GameBoss boss = spawnBoss(BossType.BHARKYOT);
            this.lastBattleMessage = "[Round 5/6] Dual Faculty Boss continues: " + boss.getName() + " appears!";
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
            // Couple wave: spawn Alyzeh first, then Bharkyot after Alyzeh is defeated.
            couplePhase = 1;
            GameBoss boss = spawnBoss(BossType.ALYZEH);
            this.lastBattleMessage = "[Round 5/6] Dual Faculty Boss: " + boss.getName() + " appears!";
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
    public GameEngine() {
        this.partyStudents = new ArrayList<>();
        this.gameState = GameState.MENU;
    }

    // --- INITIALIZATION ---

    public void initializeParty(ArrayList<GameCharacter> students) {
        this.partyStudents = students;
    }

    public GameBoss spawnBoss(BossType bossType) {
        // ----------------------------
        // Spawn a single-boss wave
        // ----------------------------
        GameBoss boss = switch(bossType) {
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

        activeBosses.clear();
        activeBosses.add(boss);

        boss.setRage(0);
        this.currentTurn = 0;
        this.isPlayerTurn = true;
        this.gameState = GameState.IN_BATTLE;
        this.lastBattleMessage = boss.getName() + " appears!";
        ensureStartingInventory();
        for (GameCharacter s : partyStudents) {   // reset party morale
            s.setMorale(100);
        }
        return boss;
    }

    private void ensureStartingInventory() {
        // Keep inventory between waves so shop purchases persist.
        if (inventory.isEmpty()) {
            for (int i = 0; i < 3; i++) inventory.add(new Potions("HP Potion", "HP", 40));
            for (int i = 0; i < 3; i++) inventory.add(new Potions("Mana Potion", "MANA", 40));
            inventory.add(new Potions("Revive Potion", "REVIVE", 50));
        }
    }

    public GameBoss spawnRandomBoss() {
        BossType[] randomBosses = {
            BossType.KYARHEY, BossType.TIMOTHEH, BossType.AHZZEE,
            BossType.JEHCEY, BossType.BHARDIAN, BossType.PATZKHEY,
            BossType.JHIEN, BossType.GHUARDIO
        };
        return spawnBoss(randomBosses[rand.nextInt(randomBosses.length)]);
    }

    public static GameBoss createBossByType(BossType bossType) {
        return switch(bossType) {
            case HYDRA -> new Hydra();
            case MARU -> new Maru();
            case JOVEH -> new Joveh();
            case LERUI -> new Lerui();
            case JENATEH -> new Jenateh();
            case LAPETRALBEY -> new LaPetralbey();
            case KYARHEY -> new Kyarhey();
            case TIMOTHEH -> new Timotheh();
            case AHZZEE -> new Ahzzee();
            case JEHCEY -> new Jehcey();
            case BHARDIAN -> new Bhardian();
            case PATZKHEY -> new Patzkhey();
            case JHIEN -> new Jhien();
            case GHUARDIO -> new Ghuardio();
            case ALYZEH -> new Alyzeh();
            case BHARKYOT -> new Bharkyot();
        };
    }

    public String saveStateToFile(String filePath) throws java.io.IOException {
        // ----------------------------
        // Save / Load
        // ----------------------------
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add("Gold:" + gold);
        // Persist inventory/potions between sessions
        // Format: InventoryItem:Name,Type,Value,Count|Name,Type,Value,Count
        if (inventory != null && !inventory.isEmpty()) {
            java.util.LinkedHashMap<String, Integer> counts = new java.util.LinkedHashMap<>();
            for (Inventory.Item it : inventory) {
                if (it == null) continue;
                String key = it.getName() + "," + it.getType() + "," + it.getValue();
                counts.put(key, counts.getOrDefault(key, 0) + 1);
            }
            if (!counts.isEmpty()) {
                StringBuilder inv = new StringBuilder();
                for (java.util.Map.Entry<String, Integer> e : counts.entrySet()) {
                    if (inv.length() > 0) inv.append("|");
                    inv.append(e.getKey()).append(",").append(e.getValue());
                }
                lines.add("InventoryItem:" + inv);
            }
        }
        lines.add("BossRound:" + bossRound);
        lines.add("CouplePhase:" + couplePhase);
        lines.add("ScheduleGenerated:" + scheduleGenerated);
        lines.add("BossesDefeated:" + bossesDefeated);
        lines.add("TotalTurns:" + totalTurns);
        lines.add("CurrentTurn:" + currentTurn);
        lines.add("GameState:" + (gameState != null ? gameState.name() : "MENU"));
        lines.add("IsPlayerTurn:" + isPlayerTurn);
        if (!activeBosses.isEmpty()) {
            // Active boss list (supports couple wave)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < activeBosses.size(); i++) {
                GameBoss b = activeBosses.get(i);
                if (b == null) continue;
                if (sb.length() > 0) sb.append("|");
                sb.append(b.getClass().getSimpleName().toUpperCase());
            }
            lines.add("ActiveBosses:" + sb);

            // Per-boss state
            for (GameBoss b : activeBosses) {
                if (b == null) continue;
                lines.add("BossState:" + b.getClass().getSimpleName().toUpperCase()
                    + "," + b.getHp()
                    + "," + b.getMaxHp()
                    + "," + b.getMana()
                    + "," + b.getMaxMana()
                    + "," + b.getRage()
                    + "," + b.getSkillCooldownRemaining(1)
                    + "," + b.getSkillCooldownRemaining(2)
                    + "," + b.getSkillCooldownRemaining(3)
                );
            }
        }
        if (scheduleGenerated && round1to4Bosses != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < round1to4Bosses.length; i++) {
                sb.append(round1to4Bosses[i].name());
                if (i < round1to4Bosses.length - 1) sb.append(",");
            }
            lines.add("RoundSchedule:" + sb);
        }
        for (GameCharacter student : partyStudents) {
            StringBuilder studentLine = new StringBuilder("Character:");
            studentLine.append(student.getName()).append(",")
                .append(student.getPosition()).append(",")
                .append(student.getHp()).append(",")
                .append(student.getMaxHp()).append(",")
                .append(student.getMana()).append(",")
                .append(student.getMaxMana()).append(",")
                .append(student.getMorale()).append(",")
                .append(student.isTaunted() ? 1 : 0).append(",")
                .append(student.getSkillCooldownRemaining(1)).append(",")
                .append(student.getSkillCooldownRemaining(2)).append(",")
                .append(student.getSkillCooldownRemaining(3));
            lines.add(studentLine.toString());
        }
        java.nio.file.Files.write(path, lines);
        return path.toAbsolutePath().toString();
    }

    public static GameEngine loadStateFromFile(String filePath) throws java.io.IOException {
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
        GameEngine engine = new GameEngine();
        ArrayList<GameCharacter> loadedParty = new ArrayList<>();
        boolean inventoryLoaded = false;
        // New format supports multiple bosses
        java.util.Map<String, GameBoss> bossMap = new java.util.HashMap<>();
        java.util.List<String> bossOrder = new java.util.ArrayList<>();
        // Backward compatibility (old single-boss saves)
        GameBoss loadedBossLegacy = null;
        for (String rawLine : lines) {
            if (rawLine == null || rawLine.isBlank()) continue;
            String line = rawLine.trim();
            if (line.startsWith("Gold:")) {
                engine.gold = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("InventoryItem:")) {
                // InventoryItem:Name,Type,Value,Count|Name,Type,Value,Count
                // Currently only Potions exist, but this format can be extended later.
                inventoryLoaded = true;
                engine.inventory.clear();
                String raw = line.substring(line.indexOf(":") + 1).trim();
                if (!raw.isBlank()) {
                    String[] entries = raw.split("\\|");
                    for (String ent : entries) {
                        String[] parts = ent.split(",");
                        if (parts.length < 4) continue;
                        // Name may contain commas in theory; we keep it simple for now
                        String name = parts[0].trim();
                        String type = parts[1].trim();
                        int value = Integer.parseInt(parts[2].trim());
                        int count = Integer.parseInt(parts[3].trim());
                        for (int i = 0; i < count; i++) {
                            engine.inventory.add(new Inventory.Potions(name, type, value));
                        }
                    }
                }
            } else if (line.startsWith("BossRound:")) {
                engine.bossRound = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("CouplePhase:")) {
                try {
                    engine.couplePhase = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                } catch (Exception ignored) { }
            } else if (line.startsWith("ScheduleGenerated:")) {
                engine.scheduleGenerated = Boolean.parseBoolean(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("BossesDefeated:")) {
                engine.bossesDefeated = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("TotalTurns:")) {
                engine.totalTurns = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("CurrentTurn:")) {
                engine.currentTurn = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("GameState:")) {
                engine.gameState = GameState.valueOf(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("IsPlayerTurn:")) {
                engine.isPlayerTurn = Boolean.parseBoolean(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("ActiveBosses:")) {
                String raw = line.substring(line.indexOf(":") + 1).trim();
                if (!raw.isBlank()) {
                    String[] names = raw.split("\\|");
                    for (String n : names) {
                        String bossName = n.trim();
                        if (bossName.isEmpty()) continue;
                        try {
                            GameBoss b = createBossByType(BossType.valueOf(bossName));
                            bossMap.put(bossName, b);
                            bossOrder.add(bossName);
                        } catch (Exception ignored) { }
                    }
                }
            } else if (line.startsWith("BossState:")) {
                // BossState:NAME,hp,maxHp,mana,maxMana,rage,cd1,cd2,cd3
                String raw = line.substring(line.indexOf(":") + 1).trim();
                String[] parts = raw.split(",");
                if (parts.length >= 9) {
                    String bossName = parts[0].trim().toUpperCase();
                    GameBoss b = bossMap.get(bossName);
                    if (b == null) {
                        try {
                            b = createBossByType(BossType.valueOf(bossName));
                            bossMap.put(bossName, b);
                            bossOrder.add(bossName);
                        } catch (Exception ignored) { }
                    }
                    if (b != null) {
                        b.setMaxHp(Integer.parseInt(parts[2].trim()));
                        b.setHp(Integer.parseInt(parts[1].trim()));
                        b.setMaxMana(Integer.parseInt(parts[4].trim()));
                        b.setMana(Integer.parseInt(parts[3].trim()));
                        b.setRage(Integer.parseInt(parts[5].trim()));
                        b.setSkillCooldownRemaining(
                            Integer.parseInt(parts[6].trim()),
                            Integer.parseInt(parts[7].trim()),
                            Integer.parseInt(parts[8].trim())
                        );
                    }
                }
            } else if (line.startsWith("CurrentBoss:")) {
                String bossName = line.substring(line.indexOf(":") + 1).trim();
                // Legacy single-boss format
                loadedBossLegacy = createBossByType(BossType.valueOf(bossName));
            } else if (line.startsWith("BossHp:")) {
                if (loadedBossLegacy != null) loadedBossLegacy.setHp(Integer.parseInt(line.substring(line.indexOf(":") + 1).trim()));
            } else if (line.startsWith("BossMana:")) {
                if (loadedBossLegacy != null) loadedBossLegacy.setMana(Integer.parseInt(line.substring(line.indexOf(":") + 1).trim()));
            } else if (line.startsWith("BossRage:")) {
                if (loadedBossLegacy != null) loadedBossLegacy.setRage(Integer.parseInt(line.substring(line.indexOf(":") + 1).trim()));
            } else if (line.startsWith("BossCooldowns:")) {
                if (loadedBossLegacy != null) {
                    String[] cds = line.substring(line.indexOf(":") + 1).trim().split(",");
                    if (cds.length == 3) {
                        loadedBossLegacy.setSkillCooldownRemaining(
                            Integer.parseInt(cds[0].trim()),
                            Integer.parseInt(cds[1].trim()),
                            Integer.parseInt(cds[2].trim())
                        );
                    }
                }
            } else if (line.startsWith("RoundSchedule:")) {
                String[] bossNames = line.substring(line.indexOf(":") + 1).trim().split(",");
                if (bossNames.length == engine.round1to4Bosses.length) {
                    for (int i = 0; i < bossNames.length; i++) {
                        engine.round1to4Bosses[i] = BossType.valueOf(bossNames[i].trim());
                    }
                }
            } else if (line.startsWith("Character:")) {
                String[] parts = line.substring(line.indexOf(":") + 1).split(",");
                if (parts.length >= 11) {
                    GameCharacter character = CharacterFactory.createCharacter(parts[0].trim());
                    if (character != null) {
                        character.setPosition(parts[1].trim());
                        character.setMaxHp(Integer.parseInt(parts[3].trim()));
                        character.setHp(Integer.parseInt(parts[2].trim()));
                        character.setMaxMana(Integer.parseInt(parts[5].trim()));
                        character.setMana(Integer.parseInt(parts[4].trim()));
                        character.setMorale(Integer.parseInt(parts[6].trim()));
                        if (Integer.parseInt(parts[7].trim()) > 0) {
                            character.applyTaunt(Integer.parseInt(parts[7].trim()));
                        }
                        character.setSkillCooldownRemaining(
                            Integer.parseInt(parts[8].trim()),
                            Integer.parseInt(parts[9].trim()),
                            Integer.parseInt(parts[10].trim())
                        );
                        loadedParty.add(character);
                    }
                }
            }
        }
        engine.partyStudents = loadedParty;

        engine.activeBosses.clear();
        if (!bossOrder.isEmpty()) {
            for (String n : bossOrder) {
                GameBoss b = bossMap.get(n);
                if (b != null) engine.activeBosses.add(b);
            }
        } else if (loadedBossLegacy != null) {
            engine.activeBosses.add(loadedBossLegacy);
        }

        // If older saves don't have CouplePhase, infer it.
        if (engine.couplePhase == 0) {
            if (engine.bossRound < 5) engine.couplePhase = 0;
            else if (engine.bossRound > 5) engine.couplePhase = 2;
            else {
                // bossRound == 5
                boolean fightingBharkyot = engine.activeBosses.stream()
                    .anyMatch(b -> b != null && b.getClass().getSimpleName().equalsIgnoreCase("Bharkyot"));
                engine.couplePhase = fightingBharkyot ? 2 : 1;
            }
        }

        // Legacy saves (before inventory was stored): keep the old behavior of giving starter potions
        if (!inventoryLoaded && (engine.inventory == null || engine.inventory.isEmpty())) {
            engine.ensureStartingInventory();
        }
        return engine;
    }

    public String executePlayerTurn(int skillChoice, GameCharacter actor) {
        // ----------------------------
        // Player turn flow
        // ----------------------------
        if (!isPlayerTurn || getCurrentBoss() == null) return "It's not your turn!";
        if (actor == null) return "No active character selected.";

        ArrayList<GameBoss> bossList = new ArrayList<>();
        for (GameBoss b : activeBosses) {
            if (b != null && b.getHp() > 0) bossList.add(b);
        }
        if (bossList.isEmpty()) return "No bosses remain.";

        String result;
        if (skillChoice == 0) {
            result = actor.basicAttack(bossList);
        } else {
            if (actor.isSkillOnCooldown(skillChoice)) {
                return actor.getSkillDisplayName(skillChoice) + " can't be used for "
                    + actor.getSkillCooldownRemaining(skillChoice) + " more rounds.";
            }
            result = actor.useSkills(skillChoice, bossList);
        }

        boolean allBossesDead = true;
        for (GameBoss b : activeBosses) {
            if (b != null && b.getHp() > 0) { allBossesDead = false; break; }
        }
        if (allBossesDead) {
            this.gameState = GameState.BOSS_DEFEATED;
            // Count individual bosses defeated (couple wave counts as 2).
            int defeatedThisWave = activeBosses.size();
            bossesDefeated += defeatedThisWave;
            int rewardGold = 25 * defeatedThisWave;
            addGold(rewardGold);
            return result
                + "\nBoss wave defeated!"
                + "\nYou earned +" + rewardGold + " gold. Total gold: " + gold + "g";
        }

        this.isPlayerTurn = false;
        this.currentTurn++;
        totalTurns++;
        return result;
    }

    public String executeBossTurn() {
        // ----------------------------
        // Boss turn flow
        // ----------------------------
    if (isPlayerTurn || activeBosses.isEmpty()) return "Boss turn skipped.";

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

    // Each alive boss acts once on the boss turn (couple wave = 2 actions).
    for (GameBoss boss : new java.util.ArrayList<>(activeBosses)) {
        if (boss == null || boss.getHp() <= 0) continue;

        // Boss AI: try to use a skill if available and not on cooldown, else basic attack
        String bossAction = null;
        int skillSlots = Math.min(3, boss.getSkillname() != null ? boss.getSkillname().length : 0);
        int usedSkill = 0;

        if (skillSlots > 0 && rand.nextDouble() < 0.65) {
            for (int tries = 0; tries < skillSlots; tries++) {
                int skillChoice = rand.nextInt(skillSlots) + 1;
                if (boss.isSkillOnCooldown(skillChoice)) continue;
                usedSkill = skillChoice;
                bossAction = boss.useSkills(skillChoice, preferredTargets);
                break;
            }
        }

        if (bossAction == null) {
            bossAction = boss.basicAttack(preferredTargets);
        } else {
            // Start cooldown only if the skill really fired (not a "lacks Mana" attempt)
            String lower = bossAction.toLowerCase();
            if (!lower.contains("lacks mana") && !lower.contains("attempted")) {
                boss.startSkillCooldown(usedSkill);
                // Boss skill side-effect: demoralize the party (smaller per-boss so couple wave isn't absurd)
                for (GameCharacter s : alive) {
                    s.reduceMorale(5);
                }
                bossAction += "\nThe party's morale drops by 5!";
            }
        }

        result.append("-- ").append(boss.getName()).append(" --\n");
        result.append(bossAction).append("\n");

        // Boss cooldowns tick down each boss action
        boss.tickCooldowns();
    }

    // Rage effect (global, once per boss-turn):
    // If any alive boss is enraged (Rage >= 80), apply unshieldable "pressure" damage to the whole party.
    // This makes Rage meaningful beyond UI.
    boolean anyEnraged = false;
    for (GameBoss b : activeBosses) {
        if (b != null && b.getHp() > 0 && b.isEnragedDoTActive()) { anyEnraged = true; break; }
    }
    if (anyEnraged) {
        int trueDamage = 6; // tuned small because it's unavoidable
        int affected = 0;
        for (GameCharacter s : partyStudents) {
            if (s != null && s.getHp() > 0) {
                // True damage: bypasses defense + miss/crit rolls by directly reducing HP
                s.setHp(s.getHp() - trueDamage);
                affected++;
            }
        }
        if (affected > 0) {
            result.append("[ENRAGED] The boss' rage burns the party for ")
                .append(trueDamage).append(" true damage each!\n");
        }
    }

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
    return result.toString();
}

    // --- Potion logic ---
    // ----------------------------
    // Inventory / potions flow
    // ----------------------------
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
    // ----------------------------
    // UI-facing getters
    // ----------------------------

    public GameBoss getCurrentBoss() {
        for (GameBoss b : activeBosses) {
            if (b != null && b.getHp() > 0) return b;
        }
        return activeBosses.isEmpty() ? null : activeBosses.get(0);
    }

    public ArrayList<GameBoss> getActiveBosses() {
        return new ArrayList<>(activeBosses);
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

        if (!activeBosses.isEmpty()) {
            status.append("=== BOSSES ===\n");
            for (GameBoss b : activeBosses) {
                if (b == null) continue;
                status.append("- ").append(b.getName()).append(" (").append(b.getDifficulty()).append(")\n");
                for (String stat : b.displayBossStats()) {
                    status.append("  ").append(stat).append("\n");
                }
                status.append("\n");
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
        GameBoss b = getCurrentBoss();
        if (b != null) {
            return b.getSkillname();
        }
        return new String[] {"No boss active"};
    }

    public String[] getBossPassives() {
        GameBoss b = getCurrentBoss();
        if (b != null) {
            return b.getPassivename();
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
    // ----------------------------
    // Utilities (reset, flee, etc.)
    // ----------------------------

    public void resetBattle() {
        this.activeBosses.clear();
        this.currentTurn = 0;
        this.isPlayerTurn = true;
        this.lastBattleMessage = "";
        this.gameState = GameState.BOSS_SELECT;
        this.bossRound = 0;
        this.couplePhase = 0;
        this.scheduleGenerated = false;
        this.bossesDefeated = 0;
        this.totalTurns = 0;
    }

    public boolean isBossDead() {
        GameBoss b = getCurrentBoss();
        return b != null && b.getHp() <= 0;
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
        this.activeBosses.clear();
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
    String bossName = getCurrentBoss() != null ? getCurrentBoss().getName() : "The enemy";
    return actor.getName() + " failed to flee! " + bossName
        + " punishes them for " + penalty + " damage!";
}
}
