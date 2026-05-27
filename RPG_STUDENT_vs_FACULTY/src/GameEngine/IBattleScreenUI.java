package GameEngine;

import Characters.GameCharacter;

/**
 * Interface defining required UI update methods for BattleScreen
 * Implement these methods in BattleScreen or use as a bridge
 * @author user
 */
public interface IBattleScreenUI {

    // HP/Mana Bar Updates
    void setHPBarBoss(int percentage);
    void setManaBarBoss(int value);
    void setHPBarStudents(int percentage);
    void setManaBarStudents(int percentage);
    void setRageMeter(int percentage);

    // Text Updates
    void setTurnIndicator(String text);
    void updateStatusDisplay(String text);
    void appendToChatBox(String message);
    void clearChatBox();

    // Button Updates
    /**
     * Update the 3 player skill buttons.
     * @param skillNames 3 skill names (already filtered)
     * @param cooldownRemaining 3 cooldown values in rounds (0 = ready)
     * @param enabled 3 enabled flags (after cooldown + turn checks)
     */
    void updateSkillButtons(String[] skillNames, int[] cooldownRemaining, boolean[] enabled);
    void setActionButtonsEnabled(boolean enabled);
    void updatePotionButtons(int hpCount, int manaCount, int reviveCount, boolean enabled);

    // Boss/Character Info
    void setBossImage(String imagePath);
    void setPartyDisplay(String[] studentNames);
    void updateBossSkillButtons(String[] skillNames, int[] cooldownRemaining);
    void updateMoraleBar(int percentage);

    // Active actor display (UI owns turn order, engine doesn't)
    GameCharacter getActiveActor();
}
