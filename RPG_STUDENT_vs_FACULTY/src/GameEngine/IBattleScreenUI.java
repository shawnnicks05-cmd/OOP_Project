package GameEngine;

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
    void updateSkillButtons(String[] skillNames);
    void setActionButtonsEnabled(boolean enabled);

    // Boss/Character Info
    void setBossImage(String imagePath);
    void setPartyDisplay(String[] studentNames);
    void updateBossSkillButtons(String[] skillNames);
    void updateMoraleBar(int percentage);
}
