package GameEngine;

import Bosses.GameBoss;
import Characters.GameCharacter;
import java.io.File;
import java.util.ArrayList;
import Inventory.EmptyInventoryException;
import Inventory.Potions;

/**
 * BattleController manages battle logic and connects to UI
 * @author user
 */
public class BattleController {
    // =========================================================
    // GAME FLOW:
    // BattleScreen input -> BattleController -> GameEngine
    // Controller updates UI after each action + auto-saves
    // =========================================================

    private GameEngine engine;
    private IBattleScreenUI battleScreen;
    private boolean nextBossScheduled = false;
    private static final String DEFAULT_SAVE_FILE = "savegame.txt";

    public BattleController(IBattleScreenUI battleScreen) {
        this.battleScreen = battleScreen;
        this.engine = new GameEngine();
    }

    public BattleController(IBattleScreenUI battleScreen, GameEngine engine) {
        this.battleScreen = battleScreen;
        this.engine = engine != null ? engine : new GameEngine();
    }

    // --- INITIALIZATION ---

    public void initializeBattle(GameEngine.BossType bossType, ArrayList<GameCharacter> partyStudents) {
        engine.initializeParty(partyStudents);
        engine.spawnBoss(bossType);
        updateAllUI();
        autoSave();
    }

   public void spawnNextBoss() {
        GameBoss next = engine.spawnNextBoss();
        nextBossScheduled = false; 
        
        // --- STEP 2 ASSIGNED HERE ---
        if (next == null) {
            appendChatMessage("\n*** ALL BOSSES DEFEATED — CONGRATULATIONS! ***");
            appendChatMessage("Bosses Defeated: " + engine.getBossesDefeated()
                + " | Total Turns: " + engine.getTotalTurns());
            
            int totalTurns = engine.getTotalTurns();
            int bossesDefeated = engine.getBossesDefeated();

            javax.swing.SwingUtilities.invokeLater(() -> {
                // Use the dedicated "game complete" Result constructor
                Game_UI.Result resultWindow = new Game_UI.Result(getPartyStudents(), bossesDefeated, totalTurns);
                resultWindow.setVisible(true);
            });

            if (battleScreen instanceof javax.swing.JFrame) {
                ((javax.swing.JFrame) battleScreen).dispose();
            } else if (battleScreen instanceof javax.swing.JDialog) {
                ((javax.swing.JDialog) battleScreen).dispose();
            }
            return;
        }
        // --- END OF STEP 2 ---

        // The rest of your spawnNextBoss method stays exactly the same:
        battleScreen.clearChatBox();
        battleScreen.setBossImage(next.getImagePath());
        if (battleScreen instanceof Game_UI.BattleScreen) {
            ((Game_UI.BattleScreen) battleScreen).updateBossVisuals();
        }
        updateAllUI();

        appendChatMessage(engine.getLastBattleMessage());
        autoSave();
    }

    private void autoSave() {
        try {
            String dir = System.getProperty("user.dir");
            String path = dir + File.separator + DEFAULT_SAVE_FILE;
            engine.saveStateToFile(path);
        } catch (Exception ignored) {
            // Auto-save should never crash the game.
        }
    }

    /**
     * Manual save (for a dedicated "Save Game" button in the battle UI).
     */
    public String saveNow() {
        try {
            String dir = System.getProperty("user.dir");
            String path = dir + File.separator + DEFAULT_SAVE_FILE;
            engine.saveStateToFile(path);
            return "Game saved!";
        } catch (Exception e) {
            return "Save failed: " + e.getMessage();
        }
    }

    private void offerShopAndSpawnNextBoss() {
        if (engine.getGameState() == GameEngine.GameState.BOSS_DEFEATED && engine.getBossRound() < 6) {
            openShopDialog();
        }
        spawnNextBoss();
    }

    /**
     * Opens the wave shop (JOptionPane). Safe to call from UI (e.g., Shop button).
     * If you want to restrict shop usage, add state checks here.
     */
    public void openShopDialog() {
        
        if (!(battleScreen instanceof Game_UI.BattleScreen)) return;
        String title = "Wave " + engine.getBossRound() + " Shop";
        String[] options = {
            "HP Potion (25g)",
            "Mana Potion (20g)",
            "Revive Potion (40g)",
            "Leave Shop"
        };
        boolean keepShopping = true;

        while (keepShopping) {
            int choice = javax.swing.JOptionPane.showOptionDialog(
                null,
                "Gold: " + engine.getGold() + "\nChoose an item to purchase before the next wave.",
                title,
                javax.swing.JOptionPane.DEFAULT_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice < 0 || choice == 3) {
                keepShopping = false;
                break;
            }

            int cost = 0;
            Potions potion = null;
            switch (choice) {
                case 0 -> {
                    cost = 25;
                    potion = new Potions("HP Potion", "HP", 40);
                }
                case 1 -> {
                    cost = 20;
                    potion = new Potions("Mana Potion", "MANA", 40);
                }
                case 2 -> {
                    cost = 40;
                    potion = new Potions("Revive Potion", "REVIVE", 50);
                }
            }

            if (potion == null || cost <= 0) {
                keepShopping = false;
                break;
            }

            if (!engine.spendGold(cost)) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Not enough gold for that purchase.",
                    "Shop", javax.swing.JOptionPane.WARNING_MESSAGE);
                continue;
            }

            engine.addItem(potion);
            appendChatMessage("Purchased " + potion.getName() + " for " + cost + " gold.");
            if (battleScreen instanceof Game_UI.BattleScreen screen) {
                screen.updateGoldDisplay(engine.getGold());
            }
        }
    }

    // --- TURN EXECUTION ---

    public void executePlayerAction(int skillChoice, GameCharacter actor) {
        if (!engine.isPlayerTurn()) {
            appendChatMessage("It's not your turn!");
            return;
        }
        // Prevent spamming clicks after victory from scheduling multiple boss spawns (which skips rounds)
        if (engine.getGameState() == GameEngine.GameState.BOSS_DEFEATED || nextBossScheduled) {
            appendChatMessage("Please wait... preparing the next wave.");
            return;
        }

        String result = engine.executePlayerTurn(skillChoice, actor);
        appendChatMessage(result);
        autoSave();

        if (engine.getGameState() == GameEngine.GameState.BOSS_DEFEATED) {
            appendChatMessage("\n*** VICTORY! ***");
            appendChatMessage("Bosses defeated so far: " + engine.getBossesDefeated()
                + " | Turns taken: " + engine.getTotalTurns());
            
            if (engine.getGameState() == GameEngine.GameState.GAME_OVER) return;

            nextBossScheduled = true;
            // Disable actions while waiting for the next boss
            battleScreen.setActionButtonsEnabled(false);
            battleScreen.updatePotionButtons(engine.getHpPotions(), engine.getManaPotions(), engine.getRevivePotions(), false);

            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javax.swing.SwingUtilities.invokeLater(() -> offerShopAndSpawnNextBoss());
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
        autoSave();
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
        autoSave();
        return result;
    }

    // --- THE FLEE AND ACTION HANDLERS ---

  public String executeFlee(GameCharacter actor) {
        if (!engine.isPlayerTurn()) {
            appendChatMessage("It's not your turn!");
            return "It's not your turn!";
        }
        if (actor == null) {
            appendChatMessage("No active character.");
            return "No active character.";
        }

        // 1. Attempt the escape calculation in the engine
        String result = engine.attemptFlee(actor);
        appendChatMessage(result);
        updateAllUI();
        autoSave();

        // If the player successfully fled -> close battle and show the run summary (Result screen).
        if (result.contains("successfully fled")) {
            int totalTurns = engine.getTotalTurns();
            int bossesDefeated = engine.getBossesDefeated();

            // Safely close the UI window
            if (battleScreen instanceof javax.swing.JFrame) {
                ((javax.swing.JFrame) battleScreen).dispose();
            } else if (battleScreen instanceof javax.swing.JDialog) {
                ((javax.swing.JDialog) battleScreen).dispose();
            }

            javax.swing.SwingUtilities.invokeLater(() -> {
                Game_UI.Result resultWindow = new Game_UI.Result(
                    getPartyStudents(),
                    bossesDefeated,
                    totalTurns,
                    "You safely escaped the classroom."
                );
                resultWindow.setVisible(true);
            });
        }

        return result;
    }

    /**
     * Call this method from your Swing UI class when the FLEE button is clicked!
     */
    public void handleFleeButtonClick() {
        GameCharacter activeActor = battleScreen.getActiveActor();
        executeFlee(activeActor);
    }

    public void executeBossAction() {
        String result = engine.executeBossTurn();
        appendChatMessage(result);

        if (engine.getGameState() == GameEngine.GameState.PARTY_DEFEATED) {
            appendChatMessage("\n*** DEFEAT! ***");
            appendChatMessage("Bosses Defeated: " + engine.getBossesDefeated()
                + " | Total Turns: " + engine.getTotalTurns());
            javax.swing.JOptionPane.showMessageDialog(null,
                "GAME OVER!\nBosses Defeated: " + engine.getBossesDefeated()
                + "\nTotal Turns: " + engine.getTotalTurns(),
                "DEFEAT", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        updateAllUI();
        autoSave();
    }

    // --- UI UPDATES ---

    public void updateAllUI() {
        updateBossStats();
        updatePartyStats();
        updateBattleInfo();
        updateActionButtons();
    }

    private void updateBossStats() {
        ArrayList<GameBoss> bosses = engine.getActiveBosses();
        if (bosses == null || bosses.isEmpty()) return;

        int totalHp = 0, totalMaxHp = 0;
        double manaRatioSum = 0.0;
        int manaRatioCount = 0;
        int maxRage = 0;

        for (GameBoss b : bosses) {
            if (b == null) continue;
            totalHp += Math.max(0, b.getHp());
            totalMaxHp += Math.max(1, b.getMaxHp());
            if (b.getMaxMana() > 0) {
                manaRatioSum += (b.getMana() / (double) b.getMaxMana());
                manaRatioCount++;
            }
            maxRage = Math.max(maxRage, b.getRage());
        }

        int hpPercent = (int) ((totalHp / (double) totalMaxHp) * 100);
        battleScreen.setHPBarBoss(Math.max(0, Math.min(100, hpPercent)));

        int manaPercent = 0;
        if (manaRatioCount > 0) {
            manaPercent = (int) Math.round((manaRatioSum / manaRatioCount) * 100);
        }
        battleScreen.setManaBarBoss(Math.max(0, Math.min(100, manaPercent)));

        // Boss skill buttons: show the first alive boss (keeps UI simple for couple wave)
        GameBoss primary = engine.getCurrentBoss();
        if (primary != null) {
            int skillSlots = Math.min(3, primary.getSkillname() != null ? primary.getSkillname().length : 0);
            int[] cds = new int[] {0, 0, 0};
            for (int i = 0; i < skillSlots; i++) {
                cds[i] = primary.getSkillCooldownRemaining(i + 1);
            }
            battleScreen.updateBossSkillButtons(primary.getSkillname(), cds);
            battleScreen.setRageMeter(maxRage);
        }
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
