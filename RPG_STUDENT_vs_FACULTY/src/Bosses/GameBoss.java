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

    // --- Boss skill cooldown system (per-skill, in turns/rounds) ---
    // skillNumber is 1..3 externally; internally we map to index 0..2.
    protected final int[] skillCooldownMax = new int[] { 3, 3, 3 };
    protected final int[] skillCooldownRemaining = new int[] { 0, 0, 0 };

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
    public int getSkillCooldownRemaining(int skillNumber) {
        int idx = skillNumber - 1;
        if (idx < 0 || idx >= 3) return 0;
        return skillCooldownRemaining[idx];
    }

    public boolean isSkillOnCooldown(int skillNumber) {
        return getSkillCooldownRemaining(skillNumber) > 0;
    }

    public void tickCooldowns() {
        for (int i = 0; i < skillCooldownRemaining.length; i++) {
            if (skillCooldownRemaining[i] > 0) skillCooldownRemaining[i]--;
        }
    }

    public void setSkillCooldowns(int cd1, int cd2, int cd3) {
        skillCooldownMax[0] = Math.max(0, cd1);
        skillCooldownMax[1] = Math.max(0, cd2);
        skillCooldownMax[2] = Math.max(0, cd3);
    }

    public void startSkillCooldown(int skillNumber) {
        int idx = skillNumber - 1;
        if (idx < 0 || idx >= 3) return;
        skillCooldownRemaining[idx] = skillCooldownMax[idx];
    }
    
    // --- COMBAT STAT MODIFIERS ---
    public void setRage(int rage) {
    this.rage = rage;
}
    public String getImagePath() {
    return "/assets/" + getName() + ".png";
}

    protected int rollDamage(int baseDamage, boolean tauntPenalty) {
        double roll = Math.random();
        double missChance = tauntPenalty ? 0.20 : 0.10;
        double critChance = 0.15;
        if (roll < missChance) return -1;
        if (roll < missChance + critChance) return (int)(baseDamage * 1.5);
        return baseDamage;
    }

    protected String attackPlayerWithRoll(GameCharacter target, int baseDamage, String attackDescription, boolean tauntPenalty) {
        int rolled = rollDamage(baseDamage, tauntPenalty);
        if (rolled == -1) {
            return this.name + " misses " + target.getName() + " with " + attackDescription + ".";
        }
        target.takeDamage(rolled);
        boolean crit = rolled > baseDamage;
        return this.name + " " + attackDescription + " " + target.getName() + " for " + rolled + " damage!" + (crit ? " CRITICAL STRIKE!" : "");
    }

    public void takeDamage(int ammount) {
        if (ammount <= 0) {
            return;
        }
        int rolled = rollDamage(ammount, false);
        if (rolled == -1) {
            return;
        }
        this.hpBoss -= rolled;
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
