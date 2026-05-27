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
    
    protected String name;
    protected String role;
    protected String damageType;
    protected String bestStat;
    
    // Primary attributes
    protected int intelligence;
    protected int analysis;
    protected int creativity;
    protected int wisdom;
    protected int knowledge;
   
    // Main Stats
    protected int maxHp;
    protected int hp;
    protected int maxMana;
    protected int mana;
    protected int maxStamina;
    protected int stamina;
    protected int morale;
    
    // Status effects
    protected boolean isPanicked = false;
    protected boolean isStress = false;
    protected boolean isSilenced = false;
    protected boolean isConfused = false;
    
    // Skill metrics
    protected double skillCooldown;
    protected double accuracy;
    
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
    public int getMaxHp() { return maxHp; }
    public int getMorale()
    {
        return morale;
    }
     public void setMorale(int morale) {
    this.morale = morale;
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
    
    // --- HEALTH RESOURCE PROCESSOR ---
    public void takeDamage(int ammount) {
        this.hp -= ammount;
        
        // Prevent negative values safely, without testing upper limit cap here
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