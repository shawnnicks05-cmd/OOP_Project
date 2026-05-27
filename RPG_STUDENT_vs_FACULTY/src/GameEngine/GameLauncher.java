package GameEngine;

import Characters.GameCharacter;
import java.util.ArrayList;

/**
 * GameLauncher - Usage example showing how to initialize and run battles
 * @author user
 */
public class GameLauncher {

    /**
     * Example: Initialize a battle with a specific boss
     */
    public static void initializeSpecificBossBattle(BattleController controller,
                                                    ArrayList<GameCharacter> partyStudents,
                                                    GameEngine.BossType bossType) {
        controller.initializeBattle(bossType, partyStudents);
        controller.appendChatMessage("Battle started! Prepare yourself!");
    }

    /**
     * Example: Initialize a random boss encounter
     */
    public static void initializeRandomBossBattle(BattleController controller,
                                                  ArrayList<GameCharacter> partyStudents) {
         controller.getEngine().initializeParty(partyStudents);  // ← set party first
    controller.spawnNextBoss();
    }

    /**
     * Example: Execute player action during battle
     */
    public static void playerAction(BattleController controller,
                                    GameCharacter actor,
                                    int skillChoice) {
        controller.executePlayerAction(skillChoice, actor);
    }

    /**
     * Example: Check battle status
     */
    public static boolean isBattleActive(BattleController controller) {
        GameEngine engine = controller.getEngine();
        return engine.getGameState() == GameEngine.GameState.IN_BATTLE;
    }

    /**
     * Example: End battle and reset
     */
    public static void endBattle(BattleController controller) {
        controller.resetBattle();
    }

    /**
     * Integration with BattleScreen:
     *
     * 1. In BattleScreen constructor:
     *    BattleController controller = new BattleController(this);
     *
     * 2. When Start button is clicked:
     *    ArrayList<GameCharacter> party = getPartyFromTeamSelection();
     *    GameLauncher.initializeRandomBossBattle(controller, party);
     *
     * 3. When skill button is clicked:
     *    GameCharacter actor = getCurrentActingStudent();
     *    GameLauncher.playerAction(controller, actor, skillNumber);
     *
     * 4. Monitor battle progress:
     *    if (!GameLauncher.isBattleActive(controller)) {
     *        // Battle ended - check victory/defeat
     *    }
     */
}
