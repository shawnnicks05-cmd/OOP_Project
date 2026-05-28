/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Characters;

import Bosses.GameBoss;
import java.util.ArrayList;

/**
 * Core Abstract Blueprint for all Student Characters.
 * @author user
 */
public abstract class GameCharacter {
    
    private String name;
    private String role;
    private String damageType;
    private String bestStat;
    
    // Primary attributes
    private int intelligence;
    private int analysis;
    private int creativity;
    private int wisdom;
    private int knowledge;
   
    // Main Stats
    private int maxHp;
    private int hp;
    private int maxMana;
    private int mana;
    private int maxStamina;
    private int stamina;
    private int morale;
    private int attackPower;
    private int defensePower;
    
    // Status effects
    protected boolean isPanicked = false;
    protected boolean isStress = false;
    protected boolean isSilenced = false;
    protected boolean isConfused = false;
    private int tauntDuration = 0;

    protected int rollDamage(int baseDamage) {
        java.util.Random rand = new java.util.Random();
        double roll = rand.nextDouble();
        if (roll < 0.10) return -1;              // 10% miss — caller checks for -1
        if (roll < 0.25) return (int)(baseDamage * 1.5); // 15% crit
        return baseDamage;                        // 75% normal
    }

    public boolean isTaunted() {
        return this.tauntDuration > 0;
    }

    public void applyTaunt(int turns) {
        this.tauntDuration = Math.max(this.tauntDuration, turns);
    }

    public void tickTaunt() {
        if (this.tauntDuration > 0) {
            this.tauntDuration--;
        }
    }

    protected String attackBossWithRoll(GameBoss target, int baseDamage, double damageModifier, String attackDescription) {
        int rolled = rollDamage(baseDamage);
        if (rolled == -1) {
            return this.name + " misses " + target.getName() + " with " + attackDescription + ".";
        }
        int actualDamage = (int) (rolled * damageModifier);
        target.takeDamage(actualDamage);
        boolean crit = rolled > baseDamage;
        return this.name + " " + attackDescription + " " + target.getName() + " for " + actualDamage + " damage!" + (crit ? " CRITICAL HIT!" : "");
    }

    // Skill metrics
    // NOTE: Old single skillCooldown field kept for backward compatibility,
    // but cooldowns are now tracked per-skill in rounds.
    protected double skillCooldown;
    protected double accuracy;

    // --- Skill cooldown system (per-skill, in rounds) ---
    // skillNumber is 1..3 externally; internally we map to index 0..2.
    protected final int[] skillCooldownMax = new int[] { 3, 3, 3 };
    protected final int[] skillCooldownRemaining = new int[] { 0, 0, 0 };

    // Character row grid position ("Front", "Above", "Below")
    protected String position;
    
    public GameCharacter(String name, String role, String damageType, String bestStat) {
        this.name = name;
        this.role = role;
        this.damageType = damageType;
        this.bestStat = bestStat;
    }
    
    // --- SAFE GETTERS & SETTERS ---
    public int getHp() {
        return this.hp; // Clean, simple text-safe reading loop
    }
    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, this.maxHp));
    }
    public int getMaxHp() {
        return this.maxHp;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(0, maxHp);
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }
    public int getMana() {
        return this.mana;
    }
    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, this.maxMana));
    }
    public int getMaxMana() {
        return this.maxMana;
    }
    public void setMaxMana(int maxMana) {
        this.maxMana = Math.max(0, maxMana);
        if (this.mana > this.maxMana) {
            this.mana = this.maxMana;
        }
    }
    public int getStamina() {
        return this.stamina;
    }
    public void setStamina(int stamina) {
        this.stamina = Math.max(0, stamina);
    }
    public int getMaxStamina() {
        return this.maxStamina;
    }
    public void setMaxStamina(int maxStamina) {
        this.maxStamina = Math.max(0, maxStamina);
    }
    public int getMorale() {
        return morale;
    }
    public void setMorale(int morale) {
        // Clamp morale to [0, 100]
        if (morale < 0) morale = 0;
        if (morale > 100) morale = 100;
        this.morale = morale;
    }
    public int getAttackPower() {
        return this.attackPower;
    }
    public void setAttackPower(int attackPower) {
        this.attackPower = Math.max(0, attackPower);
    }
    public int getDefensePower() {
        return this.defensePower;
    }
    public void setDefensePower(int defensePower) {
        this.defensePower = Math.max(0, defensePower);
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() { return name; }
    public String getDamageType() { return damageType; }
    public String getBestStat() { return bestStat; }
    public String getRole() { return role; }

    public void reduceMorale(int amount) {
        if (amount <= 0) return;
        setMorale(this.morale - amount);
    }

    public void increaseMorale(int amount) {
        if (amount <= 0) return;
        setMorale(this.morale + amount);
    }

    public void restoreMana(int amount) {
        if (amount <= 0) return;
        this.mana += amount;
        if (this.mana > this.maxMana) this.mana = this.maxMana;
    }

    public void tickStatusEffects() {
        tickTaunt();
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    public void revive(int hpAmount, int manaAmount) {
        if (!isDead()) return;
        this.hp = Math.min(Math.max(hpAmount, 1), this.maxHp);
        this.mana = Math.min(Math.max(manaAmount, 0), this.maxMana);
        // Give some baseline morale so a revived character doesn't instantly collapse
        this.morale = Math.max(this.morale, 50);
    }

    // --- Cooldown helpers ---
    public void setSkillCooldowns(int cd1, int cd2, int cd3) {
        skillCooldownMax[0] = Math.max(0, cd1);
        skillCooldownMax[1] = Math.max(0, cd2);
        skillCooldownMax[2] = Math.max(0, cd3);
    }

    public void setSkillCooldownRemaining(int cd1, int cd2, int cd3) {
        skillCooldownRemaining[0] = Math.max(0, cd1);
        skillCooldownRemaining[1] = Math.max(0, cd2);
        skillCooldownRemaining[2] = Math.max(0, cd3);
    }

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

    protected void startSkillCooldown(int skillNumber) {
        int idx = skillNumber - 1;
        if (idx < 0 || idx >= 3) return;
        skillCooldownRemaining[idx] = skillCooldownMax[idx];
    }

    protected boolean trySpendMana(int amount) {
        if (this.mana < amount) return false;
        this.mana -= amount;
        return true;
    }

    /**
     * Starts a skill usage (checks cooldown + mana, consumes mana, and starts cooldown).
     * Returns false if the skill can't be used.
     */
    protected boolean tryStartSkill(int skillNumber, int manaCost) {
        if (isSkillOnCooldown(skillNumber)) return false;
        if (!trySpendMana(manaCost)) return false;
        startSkillCooldown(skillNumber);
        return true;
    }

    /**
     * Best-effort skill display name for UI/logging.
     * Uses the existing getSkillname() array convention: indices 1,3,5 are skill names.
     */
    public String getSkillDisplayName(int skillNumber) {
        try {
            String[] raw = getSkillname();
            int rawIdx = switch (skillNumber) {
                case 1 -> 1;
                case 2 -> 3;
                case 3 -> 5;
                default -> -1;
            };
            if (rawIdx >= 0 && raw != null && raw.length > rawIdx) return raw[rawIdx];
        } catch (Exception ignored) { }
        return "Skill " + skillNumber;
    }
    public int getAttack() {
        return intelligence + knowledge / 2;
    }

    // --- DAMAGE EVALUATION LOGIC CHART ---
    public double calculateDamageModifier(String enemyTargetType) {
        if (this.damageType.equals("Knowledge") && enemyTargetType.equalsIgnoreCase("Exams")) return 1.5;
        if (this.damageType.equals("Precision") && enemyTargetType.equalsIgnoreCase("Quizzes")) return 1.5;
        if (this.damageType.equals("Tech") && enemyTargetType.equalsIgnoreCase("Projects")) return 1.5;
        if (this.damageType.equals("Tactical") && enemyTargetType.equalsIgnoreCase("Activities")) return 1.5;
        if (this.damageType.equals("Support Energy") && (enemyTargetType.equalsIgnoreCase("Stress") || enemyTargetType.equalsIgnoreCase("Panic"))) return 2.0;
        return 1.0; 
    }

    public int calculateAttackDamage(GameBoss target) {
        if (target == null) return Math.max(5, this.attackPower);
        int statBonus = (this.intelligence + this.knowledge) / 4;
        int rawAttack = this.attackPower + statBonus;
        int effectiveDefense = Math.max(0, target.getDefence() / 2);
        int damage = Math.max(5, rawAttack - effectiveDefense);
        return damage;
    }

    public int calculateDefenseReduction(int incomingDamage) {
        int reduced = incomingDamage - (this.defensePower / 2);
        return Math.max(0, reduced);
    }
    
    // --- HEALTH RESOURCE PROCESSOR ---
    public void takeDamage(int ammount) {
        if (ammount <= 0) {
            return;
        }

        int rolled = rollDamage(ammount);
        if (rolled == -1) {
            return;
        }

        int finalDamage = calculateDefenseReduction(rolled);
        this.hp -= finalDamage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void heal(int ammount) {
        this.hp += ammount;
        // Cap healing strictly to your character's explicit maximum health setting
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }
    
    
    // --- THE ABSTRACT CONTRACT FOR SUBCLASSES ---
    public abstract double[] getPassiveValue();
    public abstract String[] displayStats();
    public abstract String[] getSkillname();
    public abstract String[] getPassivename();
    
    // Aligned to pass an ArrayList of active Boss targets and return String responses for UI Logs!
    public abstract String useSkills(int skillNumber, ArrayList<GameBoss> activeBosses);   
    public abstract String basicAttack(ArrayList<GameBoss> activeBosses);
    public abstract String defend(); 
}
