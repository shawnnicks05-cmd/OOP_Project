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

    // --- Difficulty scaling (applied once per boss instance) ---
    private boolean difficultyScalingApplied = false;
    private double difficultyHpMultiplier = 1.0;
    private double difficultyManaMultiplier = 1.0;
    private double difficultyDamageMultiplier = 1.0;

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

    /**
     * Apply difficulty-based scaling to a boss AFTER its constructor has assigned base stats.
     * This keeps all bosses balanced using their "difficulty" label consistently.
     */
    protected final void applyDifficultyScaling() {
        if (difficultyScalingApplied) return;
        difficultyScalingApplied = true;

        // Snapshot "base" max values so we can detect whether this boss was initialized at full HP/Mana
        // BEFORE scaling. Most constructors set hpBoss=maxHp and mana=maxMana; if so, keep it full after scaling.
        final int baseMaxHp = this.maxHp;
        final int baseMaxMana = this.maxMana;

        String d = (this.difficulty == null ? "" : this.difficulty.trim()).toUpperCase();
        // Normalize common variants
        d = d.replace("_", " ");
        d = d.replace("  ", " ");

        // Multipliers tuned so Random bosses are baseline and higher difficulties ramp up smoothly.
        // Note: These values intentionally affect HP/Mana and outgoing damage.
        switch (d) {
            case "EASY" -> {
                difficultyHpMultiplier = 0.90;
                difficultyManaMultiplier = 0.90;
                difficultyDamageMultiplier = 0.90;
            }
            case "EASY-MEDIUM", "EASY MEDIUM" -> {
                difficultyHpMultiplier = 1.05;
                difficultyManaMultiplier = 1.00;
                difficultyDamageMultiplier = 1.00;
            }
            case "MEDIUM" -> {
                difficultyHpMultiplier = 1.15;
                difficultyManaMultiplier = 1.10;
                difficultyDamageMultiplier = 1.10;
            }
            case "MEDIUM-HARD", "MEDIUM HARD" -> {
                difficultyHpMultiplier = 1.30;
                difficultyManaMultiplier = 1.20;
                difficultyDamageMultiplier = 1.20;
            }
            case "HARD" -> {
                difficultyHpMultiplier = 1.45;
                difficultyManaMultiplier = 1.30;
                difficultyDamageMultiplier = 1.30;
            }
            case "VERY HARD", "VERYHARD" -> {
                difficultyHpMultiplier = 1.65;
                difficultyManaMultiplier = 1.45;
                difficultyDamageMultiplier = 1.45;
            }
            case "EXTREME" -> {
                difficultyHpMultiplier = 1.85;
                difficultyManaMultiplier = 1.60;
                difficultyDamageMultiplier = 1.60;
            }
            // "Random" or unknown -> baseline (no scaling)
            default -> {
                difficultyHpMultiplier = 1.0;
                difficultyManaMultiplier = 1.0;
                difficultyDamageMultiplier = 1.0;
            }
        }

        // Apply HP/Mana scaling to the already-assigned base values.
        // We clamp to reasonable minimums to prevent weird edge cases.
        int newMaxHp = Math.max(1, (int) Math.round(this.maxHp * difficultyHpMultiplier));
        int newMaxMana = Math.max(0, (int) Math.round(this.maxMana * difficultyManaMultiplier));

        this.maxHp = newMaxHp;
        // If constructor set the boss to full HP (hpBoss == baseMaxHp), keep it full after scaling.
        if (this.hpBoss == baseMaxHp) {
            this.hpBoss = newMaxHp;
        } else {
            this.hpBoss = Math.min(this.hpBoss, newMaxHp);
            if (this.hpBoss <= 0) this.hpBoss = newMaxHp; // safety: fresh boss should start full HP
        }

        this.maxMana = newMaxMana;
        // If constructor set the boss to full Mana (mana == baseMaxMana), keep it full after scaling.
        if (this.mana == baseMaxMana) {
            this.mana = newMaxMana;
        } else {
            this.mana = Math.min(this.mana, newMaxMana);
        }
        if (this.mana < 0) this.mana = 0;

        // Defense scales mildly with HP so tankier bosses feel tankier.
        this.defence = Math.max(0, (int) Math.round(this.defence * (1.0 + (difficultyHpMultiplier - 1.0) * 0.5)));
    }

    protected final int scaledDamage(int baseDamage) {
        // Rage-based escalation:
        // When Rage is high enough (>=80), bosses hit harder.
        double rageMultiplier = isEnragedDoTActive() ? 1.25 : 1.0; // +25% damage while enraged
        return Math.max(0, (int) Math.round(baseDamage * difficultyDamageMultiplier * rageMultiplier));
    }

    protected final int scaledManaCost(int baseManaCost) {
        return Math.max(0, (int) Math.round(baseManaCost * difficultyManaMultiplier));
    }

    public final double getDifficultyDamageMultiplier() {
        return difficultyDamageMultiplier;
    }
    
    // --- GETTERS ---
    public String getName() { return name; }
    public String getClassification() { return classification; }
    public String getDifficulty() { return this.difficulty; }
    public String getDamageType() { return this.damageType; }
    public int getMana() { return this.mana; }
    public int getMaxMana() { return this.maxMana; }
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

    public void setHp(int hp) {
        this.hpBoss = Math.max(0, Math.min(hp, this.maxHp));
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
        if (this.hpBoss > this.maxHp) {
            this.hpBoss = this.maxHp;
        }
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, this.maxMana));
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = Math.max(0, maxMana);
        if (this.mana > this.maxMana) {
            this.mana = this.maxMana;
        }
    }

    public void setSkillCooldownRemaining(int cd1, int cd2, int cd3) {
        this.skillCooldownRemaining[0] = Math.max(0, cd1);
        this.skillCooldownRemaining[1] = Math.max(0, cd2);
        this.skillCooldownRemaining[2] = Math.max(0, cd3);
    }

    public String getImagePath() {
        // Use the class name for assets so display-name changes don't break image loading.
        String cls = this.getClass().getSimpleName();
        if ("Hydra".equalsIgnoreCase(cls)) {
            return "/assets/Hydra1.png";
        }
        return "/assets/" + cls + ".png";
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
