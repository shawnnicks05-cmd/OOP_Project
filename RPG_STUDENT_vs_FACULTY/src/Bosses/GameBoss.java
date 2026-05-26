/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;

/**
 * Core Abstract Blueprint for all Faculty Bosses.
 * @author user
 */
public abstract class GameBoss {
    
    // Description
    protected String name;
    protected String classification;
    protected String difficulty;
    protected String damageType;
    
    // Main Stats
    protected int hpBoss;
    protected int maxHp;
    protected int mana;
    protected int maxMana;
    protected int rage;
    protected int defence; // Matches Jenateh's spelling!

    public GameBoss(String name, String classification, String difficulty, String damageType) {
        this.name = name;
        this.classification = classification;
        this.difficulty = difficulty;
        this.damageType = damageType;
    }
    
    // --- GETTERS ---
    public String getName() { return name; }
    public String getClassification() { return classification; }
    public String getDifficulty() { return this.difficulty; }
    public String getDamageType() { return this.damageType; }
    public int getMana() { return this.mana; }
    public int getDefence() { return this.defence; }
    public int getHp() { return this.hpBoss; }
    public int getMaxHp() { return this.maxHp; }
    public int getRage() { return this.rage; }
    
    // --- COMBAT STAT MODIFIERS ---

    public void takeDamage(int ammount) {
        this.hpBoss -= ammount;
        
        // Prevent negative health values, clamp minimum to 0
        if (this.hpBoss < 0) {
            this.hpBoss = 0;
        }
    }
    
    public void addRage(int ammount) {
        this.rage += ammount;
        
        // Cap the maximum rage at 100 instead of resetting it to 0
        if (this.rage > 100) {
            this.rage = 100;
        }
    }

    public void reduceRage(int ammount) {
        this.rage -= ammount;
        
        // Prevent negative rage scores, clamp minimum to 0
        if (this.rage < 0) {
            this.rage = 0;
        }
    }

    public boolean isEnragedDoTActive() {
        return this.rage >= 80;
    }
   
    // --- THE ABSTRACT CONTRACT FOR SUBCLASSES ---
    public abstract String[] getSkillname();
    public abstract String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents);
    public abstract String basicAttack(ArrayList<GameCharacter> partyStudents);
    public abstract String[] displayBossStats();
    public abstract String[] getPassivename();
    public abstract int defend();
}