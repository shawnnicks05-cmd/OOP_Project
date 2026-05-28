/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Game_UI;
import GameEngine.BattleController;
import Characters.GameCharacter;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Result extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Result.class.getName());
    private BattleController battleController;
    private ArrayList<GameCharacter> party;
    private boolean isGameComplete = false;

    /**
     * Creates new form Result
     */
    public Result() {
        initComponents();
        configureWindow();
    }

    /**
     * Creates Result screen with battle data
     */
    public Result(BattleController controller, ArrayList<GameCharacter> party, int bossesDefeated, int totalTurns, String bossName, boolean gameComplete) {
        initComponents();
        configureWindow();
        this.battleController = controller;
        this.party = party;
        this.isGameComplete = gameComplete;
        displayBattleResults(bossesDefeated, totalTurns, bossName, gameComplete, true);
    }

    /**
     * Creates Result screen for defeat
     */
    public Result(BattleController controller, ArrayList<GameCharacter> party, int bossesDefeated, int totalTurns, String bossName) {
        initComponents();
        configureWindow();
        this.battleController = controller;
        this.party = party;
        this.isGameComplete = false;
        displayBattleResults(bossesDefeated, totalTurns, bossName, false, false);
    }

    /**
     * Creates Result screen for game completion with summary
     */
    public Result(ArrayList<GameCharacter> party, int bossesDefeated, int totalTurns) {
        initComponents();
        configureWindow();
        this.party = party;
        this.isGameComplete = true;
        this.battleController = null;
        displayGameSummary(bossesDefeated, totalTurns);
    }

    private void configureWindow() {
        setLocationRelativeTo(null);
        setResizable(false);
        setExtendedState(javax.swing.JFrame.NORMAL);
    }

    public void displayGameSummary(int bossesDefeated, int totalTurns) {
        StringBuilder gameSummary = new StringBuilder();
        gameSummary.append("🎮 ═══════════════════════════════ 🎮\n");
        gameSummary.append("      GAME COMPLETE - VICTORY!\n");
        gameSummary.append("🎮 ═══════════════════════════════ 🎮\n\n");
        gameSummary.append("All boss rounds have been cleared!\n");
        gameSummary.append("Your party emerged victorious!\n");

        jtxtBattleResult.setText(gameSummary.toString());
        jtxtBossDefeated.setText("ALL BOSSES");
        jtxtTurnTaken.setText(String.valueOf(totalTurns));

        StringBuilder finalStats = new StringBuilder();
        finalStats.append("━━━ FINAL PARTY STATUS ━━━\n\n");
        if (party != null) {
            for (GameCharacter character : party) {
                finalStats.append("⚔ ").append(character.getName()).append("\n");
                finalStats.append("  HP: ").append(character.getHp()).append("/").append(character.getMaxHp()).append("\n");
                finalStats.append("  Mana: ").append(character.getMana()).append("/").append(character.getMaxMana()).append("\n");
                finalStats.append("  Morale: ").append(character.getMorale()).append("\n\n");
            }
        }
        finalStats.append("\n━━━ SESSION STATS ━━━\n");
        finalStats.append("Bosses Defeated: ").append(bossesDefeated).append("\n");
        finalStats.append("Total Turns: ").append(totalTurns).append("\n");

        jtxtPartySummary.setText(finalStats.toString());

        // Hide next boss button, show only menu
        jbtnContinue.setVisible(false);
        jbtnMenu.setText("RETURN TO MENU");
    }

    public void displayBattleResults(int bossesDefeated, int totalTurns, String bossName, boolean gameComplete, boolean isVictory) {
        String resultTitle = isVictory ? "BATTLE VICTORY!" : "BATTLE DEFEAT!";
        String resultMessage = isVictory 
            ? "You have successfully defeated " + bossName + "!"
            : "Your party has been defeated by " + bossName + ".";
        
        jtxtBattleResult.setText(resultTitle + "\n\n" + resultMessage);
        jtxtBossDefeated.setText(bossName);
        jtxtTurnTaken.setText(String.valueOf(totalTurns));
        
        StringBuilder partySummary = new StringBuilder();
        if (party != null) {
            for (GameCharacter character : party) {
                partySummary.append(character.getName())
                    .append("\n  HP: ").append(character.getHp()).append("/").append(character.getMaxHp())
                    .append("\n  Mana: ").append(character.getMana()).append("/").append(character.getMaxMana())
                    .append("\n");
            }
        }
        jtxtPartySummary.setText(partySummary.toString());
        
        // Update button text and visibility based on game state
        if (!isVictory) {
            // Show only menu button on defeat
            jbtnContinue.setVisible(false);
            jbtnMenu.setText("RETURN TO MENU");
        } else if (gameComplete) {
            jbtnContinue.setText("RETURN TO MENU");
        } else {
            jbtnContinue.setText("NEXT BOSS");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtBattleResult = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtxtBossDefeated = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtxtTurnTaken = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtxtPartySummary = new javax.swing.JTextPane();
        jbtnContinue = new javax.swing.JButton();
        jbtnMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jtxtBattleResult);

        jLabel1.setText("BOSS DEFEATED");

        jScrollPane2.setViewportView(jtxtBossDefeated);

        jLabel2.setText("TOTAL TURN'S TAKEN");

        jScrollPane3.setViewportView(jtxtTurnTaken);

        jLabel3.setText("PARTIES SUMMARY");

        jScrollPane4.setViewportView(jtxtPartySummary);

        // Set all text panes as read-only
        jtxtBattleResult.setEditable(false);
        jtxtBossDefeated.setEditable(false);
        jtxtTurnTaken.setEditable(false);
        jtxtPartySummary.setEditable(false);

        jbtnContinue.setText("NEXT BOSS");
        jbtnContinue.addActionListener(evt -> jbtnContinueActionPerformed(evt));

        jbtnMenu.setText("RETURN TO MENU");
        jbtnMenu.addActionListener(evt -> jbtnMenuActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(273, 273, 273)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)))
                .addContainerGap(389, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jbtnContinue, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jbtnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnContinue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnContinueActionPerformed(java.awt.event.ActionEvent evt) {
        if (isGameComplete) {
            // Go back to menu
            Menu menu = new Menu();
            menu.setVisible(true);
            this.dispose();
        } else {
            // Continue to next boss
            if (battleController != null) {
                // Spawn the next boss and check if successful
                boolean bossSpawned = battleController.spawnNextBoss();
                
                if (bossSpawned) {
                    // Show the battle screen with the next boss
                    BattleScreen battleScreen = new BattleScreen(battleController);
                    battleScreen.setVisible(true);
                } else {
                    // All bosses defeated - show game summary
                    Result gameSummary = new Result(battleController.getPartyStudents(), 
                                                    battleController.getEngine().getBossesDefeated(), 
                                                    battleController.getEngine().getTotalTurns());
                    gameSummary.setVisible(true);
                }
                this.dispose();
            }
        }
    }

    private void jbtnMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Menu menu = new Menu();
        menu.setVisible(true);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Result().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtnContinue;
    private javax.swing.JButton jbtnMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextPane jtxtBattleResult;
    private javax.swing.JTextPane jtxtBossDefeated;
    private javax.swing.JTextPane jtxtPartySummary;
    private javax.swing.JTextPane jtxtTurnTaken;
    // End of variables declaration//GEN-END:variables
}
