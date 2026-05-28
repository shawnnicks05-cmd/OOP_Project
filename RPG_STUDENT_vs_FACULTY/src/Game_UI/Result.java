/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Game_UI;
import GameEngine.BattleController;
import Characters.GameCharacter;
import java.util.ArrayList;
<<<<<<< HEAD
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
=======
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679

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

<<<<<<< HEAD
    /**
     * Creates Result screen for escaping (FLEE).
     * Shows a game/session summary without forcing the player back to menu immediately.
     */
    public Result(ArrayList<GameCharacter> party, int bossesDefeated, int totalTurns, String escapeMessage) {
        initComponents();
        configureWindow();
        this.party = party;
        this.isGameComplete = true; // treat as an end-of-run screen (no NEXT BOSS)
        this.battleController = null;
        displayEscapeSummary(bossesDefeated, totalTurns, escapeMessage);
    }

=======
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
    private void configureWindow() {
        setLocationRelativeTo(null);
        setResizable(false);
        setExtendedState(javax.swing.JFrame.NORMAL);
<<<<<<< HEAD
        setTitle("Battle Result");
        // Rebuild the layout at runtime (cleaner than the NetBeans-generated GroupLayout)
        // while still reusing the same components.
        rebuildResultLayout();
        applyNiceResultStyling();
    }

    /**
     * Makes the Result window cleaner and easier to read without changing the NetBeans layout.
     * (Bigger fonts, centered key fields, nicer labels, and readable padding.)
     */
    private void applyNiceResultStyling() {
        try {
            Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
            Font subFont   = new Font("Segoe UI", Font.BOLD, 12);
            Font cardFont  = new Font("Segoe UI", Font.BOLD, 18);
            Font bodyFont  = new Font("Consolas", Font.PLAIN, 13);

            // Improve label text
            jLabel1.setText("BOSS");
            jLabel2.setText("TOTAL TURNS");
            jLabel3.setText("PARTY SUMMARY");
            jLabel1.setFont(subFont);
            jLabel2.setFont(subFont);
            jLabel3.setFont(subFont);

            // Light theme colors
            Color bg = new Color(245, 247, 250);
            Color cardBg = Color.WHITE;
            Color border = new Color(220, 225, 232);
            Color text = new Color(25, 28, 31);

            getContentPane().setBackground(bg);

            // Header
            stylePaneAsTitle(jtxtBattleResult, titleFont);
            jtxtBattleResult.setBackground(cardBg);
            jtxtBattleResult.setForeground(text);

            // Cards
            stylePaneAsCard(jtxtBossDefeated, cardFont, true);
            stylePaneAsCard(jtxtTurnTaken, cardFont, true);
            jtxtBossDefeated.setBackground(cardBg);
            jtxtTurnTaken.setBackground(cardBg);
            jtxtBossDefeated.setForeground(text);
            jtxtTurnTaken.setForeground(text);

            // Party summary
            stylePaneAsBody(jtxtPartySummary, bodyFont);
            jtxtPartySummary.setBackground(cardBg);
            jtxtPartySummary.setForeground(text);

            // Borders / padding
            javax.swing.border.Border pad = BorderFactory.createEmptyBorder(10, 12, 10, 12);
            javax.swing.border.Border cardBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            );

            jtxtBattleResult.setBorder(pad);
            jtxtBossDefeated.setBorder(pad);
            jtxtTurnTaken.setBorder(pad);
            jtxtPartySummary.setBorder(pad);

            if (jScrollPane1 != null) jScrollPane1.setBorder(cardBorder);
            if (jScrollPane2 != null) jScrollPane2.setBorder(cardBorder);
            if (jScrollPane3 != null) jScrollPane3.setBorder(cardBorder);
            if (jScrollPane4 != null) jScrollPane4.setBorder(cardBorder);

            if (jScrollPane1 != null) jScrollPane1.getViewport().setBackground(cardBg);
            if (jScrollPane2 != null) jScrollPane2.getViewport().setBackground(cardBg);
            if (jScrollPane3 != null) jScrollPane3.getViewport().setBackground(cardBg);
            if (jScrollPane4 != null) jScrollPane4.getViewport().setBackground(cardBg);

            // Buttons
            Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
            jbtnContinue.setFont(btnFont);
            jbtnMenu.setFont(btnFont);

            // Final size
            pack();
            setSize(920, 560);
        } catch (Exception ignored) { }
    }

    /**
     * Replace the NetBeans GroupLayout with a simple "cards" layout:
     * Header (big title) -> stats cards row -> party summary -> buttons row.
     */
    private void rebuildResultLayout() {
        try {
            // Keep using existing components created by initComponents().
            getContentPane().removeAll();
            getContentPane().setLayout(new BorderLayout(12, 12));
            ((javax.swing.JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

            // Header card (result title/message)
            if (jScrollPane1 != null) {
                jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                jScrollPane1.setPreferredSize(new Dimension(820, 150));
            }

            // Row of small cards: Boss + Turns
            JPanel cardsRow = new JPanel(new GridLayout(1, 2, 12, 12));
            cardsRow.setOpaque(false);
            if (jScrollPane2 != null) {
                jScrollPane2.setPreferredSize(new Dimension(260, 110));
                cardsRow.add(wrapWithHeader("BOSS", jScrollPane2));
            }
            if (jScrollPane3 != null) {
                jScrollPane3.setPreferredSize(new Dimension(260, 110));
                cardsRow.add(wrapWithHeader("TOTAL TURNS", jScrollPane3));
            }

            // Party summary card (bigger)
            JPanel partyCard = new JPanel(new BorderLayout(0, 8));
            partyCard.setOpaque(false);
            partyCard.add(makeSectionLabel("PARTY SUMMARY"), BorderLayout.NORTH);
            if (jScrollPane4 != null) {
                jScrollPane4.setPreferredSize(new Dimension(820, 220));
                partyCard.add(jScrollPane4, BorderLayout.CENTER);
            }

            // Buttons
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
            buttons.setOpaque(false);
            buttons.add(jbtnContinue);
            buttons.add(jbtnMenu);

            JPanel center = new JPanel(new BorderLayout(0, 12));
            center.setOpaque(false);
            center.add(cardsRow, BorderLayout.NORTH);
            center.add(partyCard, BorderLayout.CENTER);

            getContentPane().add(jScrollPane1, BorderLayout.NORTH);
            getContentPane().add(center, BorderLayout.CENTER);
            getContentPane().add(buttons, BorderLayout.SOUTH);

            revalidate();
            repaint();
        } catch (Exception ignored) { }
    }

    private JPanel wrapWithHeader(String title, JScrollPane pane) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.add(makeSectionLabel(title), BorderLayout.NORTH);
        p.add(pane, BorderLayout.CENTER);
        return p;
    }

    private JLabel makeSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setHorizontalAlignment(SwingConstants.LEFT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(70, 78, 88));
        return l;
    }

    private void stylePaneAsCard(javax.swing.JTextPane pane, java.awt.Font font, boolean center) {
        if (pane == null) return;
        pane.setFont(font);
        pane.setBackground(new java.awt.Color(245, 245, 245));
        pane.setForeground(java.awt.Color.BLACK);
        pane.setEditable(false);
        if (center) {
            javax.swing.text.StyledDocument doc = pane.getStyledDocument();
            javax.swing.text.SimpleAttributeSet attrs = new javax.swing.text.SimpleAttributeSet();
            javax.swing.text.StyleConstants.setAlignment(attrs, javax.swing.text.StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
        }
    }

    private void stylePaneAsBody(javax.swing.JTextPane pane, java.awt.Font font) {
        if (pane == null) return;
        pane.setFont(font);
        pane.setBackground(new java.awt.Color(250, 250, 250));
        pane.setForeground(java.awt.Color.BLACK);
        pane.setEditable(false);
    }

    private void stylePaneAsTitle(javax.swing.JTextPane pane, java.awt.Font font) {
        if (pane == null) return;
        pane.setFont(font);
        // For the light theme, the background is set in applyNiceResultStyling()
        pane.setEditable(false);
        javax.swing.text.StyledDocument doc = pane.getStyledDocument();
        javax.swing.text.SimpleAttributeSet attrs = new javax.swing.text.SimpleAttributeSet();
        javax.swing.text.StyleConstants.setAlignment(attrs, javax.swing.text.StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
=======
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
    }

    public void displayGameSummary(int bossesDefeated, int totalTurns) {
        StringBuilder gameSummary = new StringBuilder();
<<<<<<< HEAD
        gameSummary.append("GAME COMPLETE - VICTORY!\n");
        gameSummary.append("----------------------------------\n\n");
        gameSummary.append("All boss rounds have been cleared.\n");
        gameSummary.append("Your party emerged victorious!\n");

        jtxtBattleResult.setText(gameSummary.toString());
        jtxtBattleResult.setCaretPosition(0);
=======
        gameSummary.append("🎮 ═══════════════════════════════ 🎮\n");
        gameSummary.append("      GAME COMPLETE - VICTORY!\n");
        gameSummary.append("🎮 ═══════════════════════════════ 🎮\n\n");
        gameSummary.append("All boss rounds have been cleared!\n");
        gameSummary.append("Your party emerged victorious!\n");

        jtxtBattleResult.setText(gameSummary.toString());
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
        jtxtBossDefeated.setText("ALL BOSSES");
        jtxtTurnTaken.setText(String.valueOf(totalTurns));

        StringBuilder finalStats = new StringBuilder();
<<<<<<< HEAD
        finalStats.append("FINAL PARTY STATUS\n");
        finalStats.append("------------------\n\n");
        if (party != null) {
            for (GameCharacter character : party) {
                finalStats.append(character.getName()).append("\n");
=======
        finalStats.append("━━━ FINAL PARTY STATUS ━━━\n\n");
        if (party != null) {
            for (GameCharacter character : party) {
                finalStats.append("⚔ ").append(character.getName()).append("\n");
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
                finalStats.append("  HP: ").append(character.getHp()).append("/").append(character.getMaxHp()).append("\n");
                finalStats.append("  Mana: ").append(character.getMana()).append("/").append(character.getMaxMana()).append("\n");
                finalStats.append("  Morale: ").append(character.getMorale()).append("\n\n");
            }
        }
<<<<<<< HEAD
        finalStats.append("\nSESSION STATS\n");
        finalStats.append("------------\n");
=======
        finalStats.append("\n━━━ SESSION STATS ━━━\n");
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
        finalStats.append("Bosses Defeated: ").append(bossesDefeated).append("\n");
        finalStats.append("Total Turns: ").append(totalTurns).append("\n");

        jtxtPartySummary.setText(finalStats.toString());
<<<<<<< HEAD
        jtxtPartySummary.setCaretPosition(0);
=======
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679

        // Hide next boss button, show only menu
        jbtnContinue.setVisible(false);
        jbtnMenu.setText("RETURN TO MENU");
    }

<<<<<<< HEAD
    public void displayEscapeSummary(int bossesDefeated, int totalTurns, String escapeMessage) {
        StringBuilder summary = new StringBuilder();
        summary.append("ESCAPED!\n");
        summary.append("----------------------------------\n\n");
        summary.append(escapeMessage != null && !escapeMessage.isBlank()
            ? escapeMessage.trim()
            : "You fled the battle and lived to fight another day.");
        summary.append("\n");

        jtxtBattleResult.setText(summary.toString());
        jtxtBattleResult.setCaretPosition(0);
        jtxtBossDefeated.setText("ESCAPED");
        jtxtTurnTaken.setText(String.valueOf(totalTurns));

        StringBuilder finalStats = new StringBuilder();
        finalStats.append("FINAL PARTY STATUS\n");
        finalStats.append("------------------\n\n");
        if (party != null) {
            for (GameCharacter character : party) {
                finalStats.append(character.getName()).append("\n");
                finalStats.append("  HP: ").append(character.getHp()).append("/").append(character.getMaxHp()).append("\n");
                finalStats.append("  Mana: ").append(character.getMana()).append("/").append(character.getMaxMana()).append("\n");
                finalStats.append("  Morale: ").append(character.getMorale()).append("\n\n");
            }
        }
        finalStats.append("\nSESSION STATS\n");
        finalStats.append("------------\n");
        finalStats.append("Bosses Defeated: ").append(bossesDefeated).append("\n");
        finalStats.append("Total Turns: ").append(totalTurns).append("\n");

        jtxtPartySummary.setText(finalStats.toString());
        jtxtPartySummary.setCaretPosition(0);

        jbtnContinue.setVisible(false);
        jbtnMenu.setText("RETURN TO MENU");
    }

=======
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
    public void displayBattleResults(int bossesDefeated, int totalTurns, String bossName, boolean gameComplete, boolean isVictory) {
        String resultTitle = isVictory ? "BATTLE VICTORY!" : "BATTLE DEFEAT!";
        String resultMessage = isVictory 
            ? "You have successfully defeated " + bossName + "!"
            : "Your party has been defeated by " + bossName + ".";
        
<<<<<<< HEAD
        jtxtBattleResult.setText(resultTitle + "\n----------------------------------\n\n" + resultMessage);
        jtxtBattleResult.setCaretPosition(0);
=======
        jtxtBattleResult.setText(resultTitle + "\n\n" + resultMessage);
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
        jtxtBossDefeated.setText(bossName);
        jtxtTurnTaken.setText(String.valueOf(totalTurns));
        
        StringBuilder partySummary = new StringBuilder();
        if (party != null) {
            for (GameCharacter character : party) {
<<<<<<< HEAD
                partySummary.append(character.getName()).append("\n")
                    .append("  HP: ").append(character.getHp()).append("/").append(character.getMaxHp()).append("\n")
                    .append("  Mana: ").append(character.getMana()).append("/").append(character.getMaxMana()).append("\n")
                    .append("  Morale: ").append(character.getMorale()).append("\n\n");
            }
        }
        jtxtPartySummary.setText(partySummary.toString());
        jtxtPartySummary.setCaretPosition(0);
=======
                partySummary.append(character.getName())
                    .append("\n  HP: ").append(character.getHp()).append("/").append(character.getMaxHp())
                    .append("\n  Mana: ").append(character.getMana()).append("/").append(character.getMaxMana())
                    .append("\n");
            }
        }
        jtxtPartySummary.setText(partySummary.toString());
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
        
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
<<<<<<< HEAD
                // Create a fresh battle screen bound to the same engine state, then spawn the next boss.
                BattleScreen battleScreen = new BattleScreen(battleController.getEngine());
                battleScreen.setVisible(true);
                battleScreen.getController().spawnNextBoss();
=======
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
>>>>>>> 1edc1a11529033349a2dfde7b66893e10f7a2679
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
