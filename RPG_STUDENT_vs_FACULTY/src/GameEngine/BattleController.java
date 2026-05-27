package GameEngine;

import Bosses.GameBoss;
import Characters.GameCharacter;
import java.util.ArrayList;

/**
 * BattleController manages battle logic and connects to UI
 * @author user
 */
public class BattleController {

    private GameEngine engine;
    private IBattleScreenUI battleScreen;

    public BattleController(IBattleScreenUI battleScreen) {
        this.battleScreen = battleScreen;
        this.engine = new GameEngine();
    }

    // --- INITIALIZATION ---

    public void initializeBattle(GameEngine.BossType bossType, ArrayList<GameCharacter> partyStudents) {
        engine.initializeParty(partyStudents);
        engine.spawnBoss(bossType);
        updateAllUI();
    }

    public void spawnNextBoss() {
    GameBoss next = engine.spawnNextBoss();
    
    if (next == null) {
        appendChatMessage("\n*** ALL BOSSES DEFEATED — CONGRATULATIONS! ***");
        return;
    }

    battleScreen.clearChatBox();
    battleScreen.setBossImage(next.getImagePath());
    updateAllUI();

    if (engine.isHydraHeadPhase()) {
        appendChatMessage("⚠ HYDRA HEAD " + (engine.getBossRound() - 3) + " of 3: "
            + next.getName() + " appears!");
    } else if (engine.isCoupleBossPhase()) {
        appendChatMessage("⚠ " + engine.getLastBattleMessage());
    } else {
        appendChatMessage(engine.getLastBattleMessage());
    }
}

    // --- TURN EXECUTION ---

    public void executePlayerAction(int skillChoice, GameCharacter actor) {
        if (!engine.isPlayerTurn()) {
            appendChatMessage("It's not your turn!");
            return;
        }

        String result = engine.executePlayerTurn(skillChoice, actor);
        appendChatMessage(result);

        if (engine.getGameState() == GameEngine.GameState.BOSS_DEFEATED) {
    String roundMsg = engine.isHydraHeadPhase()
        ? "\n[HYDRA HEAD DESTROYED! The Hydra still lives...]"
        : engine.getBossRound() == 11
            ? "\n*** THE HYDRA IS DEFEATED! YOU WIN! ***"
            : "\n*** VICTORY! ***";

    appendChatMessage(roundMsg);

    if (engine.getGameState() == GameEngine.GameState.GAME_OVER) return;

    new java.util.Timer().schedule(new java.util.TimerTask() {
        @Override
        public void run() {
            javax.swing.SwingUtilities.invokeLater(() -> spawnNextBoss());
        }
    }, 2000);
        return;
    }
    }
    private void executeBossAction() {
        String result = engine.executeBossTurn();
        appendChatMessage(result);

        if (engine.getGameState() == GameEngine.GameState.PARTY_DEFEATED) {
            appendChatMessage("\n*** DEFEAT! ***\nAll students have been defeated!");
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
    battleScreen.updateBossSkillButtons(boss.getSkillname());
    // Show boss rage in the turn indicator area instead of the meter
    battleScreen.setRageMeter(boss.getRage()); // still updates if you add a bar later
}

    private void updatePartyStats() {
    // ... existing HP/Mana logic unchanged ...

    // Add morale update — average morale across alive party members
    int totalMorale = 0, count = 0;
    for (GameCharacter student : engine.getPartyStudents()) {
        if (student.getHp() > 0) {
            totalMorale += student.getMorale();
            count++;
        }
    }
    if (count > 0) {
        battleScreen.updateMoraleBar(totalMorale / count); // calls MoraleMeter directly
    }
}

    private void updateBattleInfo() {
        String status = engine.getBattleStatus();
        battleScreen.setTurnIndicator("Turn: " + engine.getCurrentTurn() +
                                       " | " + (engine.isPlayerTurn() ? "PLAYER TURN" : "BOSS TURN"));
        battleScreen.updateStatusDisplay(status);
    }

    private void updateActionButtons() {
    ArrayList<GameCharacter> party = engine.getPartyStudents();

    GameCharacter actor = party.stream()
        .filter(c -> c.getHp() > 0)
        .findFirst().orElse(null);

    if (actor != null) {
        String[] raw = actor.getSkillname();
        // Your skill arrays have labels mixed in like "--SKILLS--", filter to just the skill names
        // Slots 1, 3, 5 in the array are the actual skill names (0-indexed)
        String[] skillNames = new String[] {
            raw.length > 1 ? raw[1] : "Skill 1",
            raw.length > 3 ? raw[3] : "Skill 2",
            raw.length > 5 ? raw[5] : "Skill 3"
        };
        battleScreen.updateSkillButtons(skillNames);
    }

    battleScreen.setActionButtonsEnabled(engine.isPlayerTurn());
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
