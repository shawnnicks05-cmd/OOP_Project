package GameEngine;

import Bosses.GameBoss;
import Characters.GameCharacter;
import Game_UI.Result;
import java.util.ArrayList;
import Inventory.EmptyInventoryException;
/**
 * BattleController manages battle logic and connects to UI
 * @author user
 */
public class BattleController {

    private GameEngine engine;
    private IBattleScreenUI battleScreen;
    private boolean nextBossScheduled = false;

    public BattleController(IBattleScreenUI battleScreen) {
        this.battleScreen = battleScreen;
        this.engine = new GameEngine();
    }

    public void setBattleScreen(IBattleScreenUI battleScreen) {
        this.battleScreen = battleScreen;
    }

    // --- INITIALIZATION ---

    public void initializeBattle(GameEngine.BossType bossType, ArrayList<GameCharacter> partyStudents) {
        engine.initializeParty(partyStudents);
        engine.spawnBoss(bossType);
        updateAllUI();
    }

    public boolean spawnNextBoss() {
    GameBoss next = engine.spawnNextBoss();
    nextBossScheduled = false; // allow scheduling again after a new boss is spawned
    
    if (next == null) {
    appendChatMessage("\n*** ALL BOSSES DEFEATED — CONGRATULATIONS! ***");
    appendChatMessage("Bosses Defeated: " + engine.getBossesDefeated()
        + " | Total Turns: " + engine.getTotalTurns());
    return false; // No next boss available
}

    battleScreen.clearChatBox();
    battleScreen.setBossImage(next.getImagePath());
    updateAllUI();

    appendChatMessage(engine.getLastBattleMessage());
    return true; // Next boss spawned successfully
}

    // --- TURN EXECUTION ---

    public void executePlayerAction(int skillChoice, GameCharacter actor) {
        if (!engine.isPlayerTurn()) {
            appendChatMessage("It's not your turn!");
            return;
        }
        // Prevent spamming clicks after victory from scheduling multiple boss spawns (which skips rounds)
        if (engine.getGameState() == GameEngine.GameState.BOSS_DEFEATED || nextBossScheduled) {
            return;
        }

        String result = engine.executePlayerTurn(skillChoice, actor);
        appendChatMessage(result);

        if (engine.getGameState() == GameEngine.GameState.BOSS_DEFEATED) {
    appendChatMessage("\n*** VICTORY! ***");
    appendChatMessage("Bosses defeated so far: " + engine.getBossesDefeated()
        + " | Turns taken: " + engine.getTotalTurns());
    
    if (engine.getGameState() == GameEngine.GameState.GAME_OVER) return;

    nextBossScheduled = true;
    // Disable actions while spawning next boss
    battleScreen.setActionButtonsEnabled(false);
    battleScreen.updatePotionButtons(engine.getHpPotions(), engine.getManaPotions(), engine.getRevivePotions(), false);

    new java.util.Timer().schedule(new java.util.TimerTask() {
        @Override
        public void run() {
            javax.swing.SwingUtilities.invokeLater(() -> autoSpawnNextBoss());
        }
    }, 2000);
        return;
    }

   }

    public void executePlayerPotion(GameEngine.PotionType potionType, GameCharacter actor) {
    if (!engine.isPlayerTurn()) {
        appendChatMessage("It's not your turn!");
        return;
    }
    if (actor == null) { appendChatMessage("No active character."); return; }

    try {
        String result = engine.usePotion(potionType, actor);
        // Don't end the turn for "can't revive alive" messages
        if (result.startsWith("Can't ") || result.startsWith("There is no")) {
            appendChatMessage(result);
            return;
        }
        appendChatMessage(result);
    } catch (EmptyInventoryException e) {
        appendChatMessage(e.getMessage()); // prints "No HP potions remaining!"
        return; // turn is NOT consumed
    }
    updateAllUI();
}

    public String executeTaunt(GameCharacter actor) {
    if (!engine.isPlayerTurn()) {
        appendChatMessage("It's not your turn!");
        return "It's not your turn!";
    }
    if (actor == null) {
        appendChatMessage("No active character.");
        return "No active character.";
    }

    String result = engine.applyTaunt(actor, 2);
    engine.consumePlayerTurn();
    appendChatMessage(result);
    updateAllUI();
    return result;
}

    public String executeFlee(GameCharacter actor) {
    String result = engine.attemptFlee(actor);
    appendChatMessage(result);
    updateAllUI();
    return result;
}

    private void showResultScreen() {
        GameBoss defeatedBoss = engine.getCurrentBoss();
        String bossName = defeatedBoss != null ? defeatedBoss.getName() : "Unknown Boss";
        
        if (battleScreen instanceof Game_UI.BattleScreen) {
            Game_UI.BattleScreen bs = (Game_UI.BattleScreen) battleScreen;
            Result resultScreen = new Result(this, engine.getPartyStudents(), engine.getBossesDefeated(), 
                                             engine.getTotalTurns(), bossName, false);
            resultScreen.setVisible(true);
            bs.dispose();
        }
    }

    private void autoSpawnNextBoss() {
        boolean bossSpawned = spawnNextBoss();
        
        if (bossSpawned) {
            // Next boss spawned - re-enable controls and continue battle
            appendChatMessage("Prepare for the next battle!");
            battleScreen.setActionButtonsEnabled(true);
            battleScreen.updatePotionButtons(engine.getHpPotions(), engine.getManaPotions(), engine.getRevivePotions(), true);
        } else {
            // All bosses defeated - show final result screen
            if (battleScreen instanceof Game_UI.BattleScreen) {
                Game_UI.BattleScreen bs = (Game_UI.BattleScreen) battleScreen;
                Result resultScreen = new Result(engine.getPartyStudents(), engine.getBossesDefeated(), 
                                                 engine.getTotalTurns());
                resultScreen.setVisible(true);
                bs.dispose();
            }
        }
    }

    public void executeBossAction() {
        // Print boss name separator before the boss performs an action (as shown in your sample log)
        GameBoss boss = engine.getCurrentBoss();
        if (boss != null) {
            appendChatMessage("-- " + boss.getName() + " --");
        }

        String result = engine.executeBossTurn();
        appendChatMessage(result);

       if (engine.getGameState() == GameEngine.GameState.PARTY_DEFEATED) {
    appendChatMessage("\n*** DEFEAT! ***");
    appendChatMessage("Bosses Defeated: " + engine.getBossesDefeated()
        + " | Total Turns: " + engine.getTotalTurns());
    
    // Show defeat result screen
    if (battleScreen instanceof Game_UI.BattleScreen) {
        Game_UI.BattleScreen bs = (Game_UI.BattleScreen) battleScreen;
        GameBoss currentBoss = engine.getCurrentBoss();
        String bossName = currentBoss != null ? currentBoss.getName() : "Unknown Boss";
        Result resultScreen = new Result(this, engine.getPartyStudents(), engine.getBossesDefeated(), 
                                         engine.getTotalTurns(), bossName);
        resultScreen.setVisible(true);
        bs.dispose();
    }
    return;
}

        updateAllUI();
    }

    // --- UI UPDATES ---

    public void updateAllUI() {
        updateBossStats();
        updatePartyStats();
        updateBattleInfo();
        updateActionButtons();
    }

    private void updateBossStats() {
    GameBoss boss = engine.getCurrentBoss();
    if (boss == null) return;

    int hpPercent = (int) ((boss.getHp() / (double) boss.getMaxHp()) * 100);
    battleScreen.setHPBarBoss(Math.max(0, Math.min(100, hpPercent)));
    battleScreen.setManaBarBoss(boss.getMana());
    int skillSlots = Math.min(3, boss.getSkillname() != null ? boss.getSkillname().length : 0);
    int[] cds = new int[] {0, 0, 0};
    for (int i = 0; i < skillSlots; i++) {
        cds[i] = boss.getSkillCooldownRemaining(i + 1);
    }
    battleScreen.updateBossSkillButtons(boss.getSkillname(), cds);
    // Show boss rage in the turn indicator area instead of the meter
    battleScreen.setRageMeter(boss.getRage()); // still updates if you add a bar later
}

    private void updatePartyStats() {
        // Update HP/Mana/Morale based on the CURRENT ACTIVE HERO (turn-based)
        GameCharacter actor = battleScreen.getActiveActor();
        if (actor != null) {
            int hpPercent = (int) ((actor.getHp() / (double) actor.getMaxHp()) * 100);
            battleScreen.setHPBarStudents(Math.max(0, Math.min(100, hpPercent)));
            battleScreen.setManaBarStudents(actor.getMana()); // progress bar max is 100 in UI
            battleScreen.updateMoraleBar(actor.getMorale());
        }
}

    private void updateBattleInfo() {
        String status = engine.getBattleStatus();
        battleScreen.setTurnIndicator("Turn: " + engine.getCurrentTurn() +
                                       " | " + (engine.isPlayerTurn() ? "PLAYER TURN" : "BOSS TURN"));
        battleScreen.updateStatusDisplay(status);
    }

    private void updateActionButtons() {
        GameCharacter actor = battleScreen.getActiveActor();

        boolean isTurn = engine.isPlayerTurn();
        battleScreen.setActionButtonsEnabled(isTurn);

        if (actor != null) {
            String[] raw = actor.getSkillname();
            // Skill arrays contain labels mixed in like "--SKILLS--".
            // Slots 1, 3, 5 in the array are the actual skill names (0-indexed).
            String[] skillNames = new String[] {
                raw.length > 1 ? raw[1] : "Skill 1",
                raw.length > 3 ? raw[3] : "Skill 2",
                raw.length > 5 ? raw[5] : "Skill 3"
            };

            int[] cds = new int[] {
                actor.getSkillCooldownRemaining(1),
                actor.getSkillCooldownRemaining(2),
                actor.getSkillCooldownRemaining(3)
            };

            boolean[] enabled = new boolean[] {
                isTurn && cds[0] == 0,
                isTurn && cds[1] == 0,
                isTurn && cds[2] == 0
            };

            battleScreen.updateSkillButtons(skillNames, cds, enabled);
        }

        battleScreen.updatePotionButtons(engine.getHpPotions(), engine.getManaPotions(), engine.getRevivePotions(), isTurn);
}

    public void appendChatMessage(String message) {
        battleScreen.appendToChatBox(message + "\n");
    }

    // --- GETTERS ---

    public GameEngine getEngine() {
        return engine;
    }

    public GameBoss getCurrentBoss() {
        return engine.getCurrentBoss();
    }

    public ArrayList<GameCharacter> getPartyStudents() {
        return engine.getPartyStudents();
    }

    public void resetBattle() {
        engine.resetBattle();
        battleScreen.clearChatBox();
        updateAllUI();
    }
}
