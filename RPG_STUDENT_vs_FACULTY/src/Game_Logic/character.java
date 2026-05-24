/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game_Logic;

/**
 *
 * @author user
 */
abstract class Character {
    
    protected String name;
    protected String role;
    protected String damageType;
    protected String bestStat;
    
    //Primary attributes
    protected int intelligence;
    protected int analysis;
    protected int creativity;
    protected int wisdom;
    protected int knowledge;
   
    //Secondary combat
    protected int maxHp;
    protected int hp;
    protected int maxMana;
    protected int mana;
    protected int maxStamina;
    protected int stamina;
    protected int morale;
    
    //status effect
    protected boolean isPanicked = false;
    protected boolean isStress = false;
    protected boolean isSilenced = false;
    protected boolean isConfused = false;
    
    //skill effect
    protected double skillCooldown;
    protected double accuracy;
    
    
   
    
    
    
    public Character(String name,String role, String damageType, String bestStat) {
        this.name = name;
        this.damageType = damageType;
        this.bestStat = bestStat;
        this.role = role;
    }

    
    public String getName() { return name; }
    public String getDamageType() { return damageType; }
    public String getBestStat() { return bestStat; }
    public String getRole(){
        return role;
    }
    public double calculateDamageModifier(String enemyTargetType) {
        if (this.damageType.equals("Knowledge") && enemyTargetType.equalsIgnoreCase("Exams")) return 1.5;
        if (this.damageType.equals("Precision") && enemyTargetType.equalsIgnoreCase("Quizzes")) return 1.5;
        if (this.damageType.equals("Tech") && enemyTargetType.equalsIgnoreCase("Projects")) return 1.5;
        if (this.damageType.equals("Tactical") && enemyTargetType.equalsIgnoreCase("Activities")) return 1.5;
        if (this.damageType.equals("Support Energy") && (enemyTargetType.equalsIgnoreCase("Stress") || enemyTargetType.equalsIgnoreCase("Panic"))) return 2.0;
        
        return 1.0; // Normal damage scaling
    }
    
    public abstract void displayskills();
    public abstract double[] getPassiveValue();
    public abstract String useSkills(int skillNumber,String[] enemyBosses);   
    public abstract void displayStats();
    public abstract void basicAttack();
    public abstract void defend();
    public abstract String[] getSkillname();
    public abstract String[] getPassivename();
}
