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

    public void initializeRandomBattle(ArrayList<GameCharacter> partyStudents) {
        engine.initializeParty(partyStudents);
        engine.spawnRandomBoss();
        updateAllUI();
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
            appendChatMessage("\n*** VICTORY! ***\n" + engine.getCurrentBoss().getName() + " has been defeated!");
            return;
        }

        // Boss's turn after a short delay
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                executeBossAction();
            }
        }, 1500);
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
        battleScreen.setRageMeter(boss.getRage());
    }

    private void updatePartyStats() {
        ArrayList<GameCharacter> party = engine.getPartyStudents();
        if (party.isEmpty()) return;

        int totalHP = 0;
        int maxHP = 0;
        int totalMana = 0;
        int maxMana = 0;

        for (GameCharacter student : party) {
            if (student.getHp() > 0) {
                totalHP += student.getHp();
                maxHP += student.getMaxHp();
                totalMana += 50;
                maxMana += 50;
            }
        }

        if (maxHP > 0) {
            int hpPercent = (int) ((totalHP / (double) maxHP) * 100);
            battleScreen.setHPBarStudents(Math.max(0, Math.min(100, hpPercent)));
        }

        if (maxMana > 0) {
            int manaPercent = (int) ((totalMana / (double) maxMana) * 100);
            battleScreen.setManaBarStudents(Math.max(0, Math.min(100, manaPercent)));
        }
    }

    private void updateBattleInfo() {
        String status = engine.getBattleStatus();
        battleScreen.setTurnIndicator("Turn: " + engine.getCurrentTurn() +
                                       " | " + (engine.isPlayerTurn() ? "PLAYER TURN" : "BOSS TURN"));
        battleScreen.updateStatusDisplay(status);
    }

    private void updateActionButtons() {
        GameBoss boss = engine.getCurrentBoss();
        if (boss == null) return;

        String[] skillNames = boss.getSkillname();
        battleScreen.updateSkillButtons(skillNames);

        boolean isPlayerTurn = engine.isPlayerTurn();
        battleScreen.setActionButtonsEnabled(isPlayerTurn);
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
