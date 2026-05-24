/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game_Logic;

/**
 *
 * @author user
 */
class CharacterShawn extends Character {
    
    public CharacterShawn(String name,String role, String damageType, String bestStat) {
        
        super(name, "Support/Buffer", "Mental / Tactical Damage","Intelligence");
    }

    
    
    @Override
    double getPassiveValue() {
        
        return 1.5; 
    }

    @Override
    void useSkills() {
        System.out.println(name + " casts Fireball!");
    }

    @Override
    void displayStats() {
        System.out.println(name + "'s Magic power is overflowing.");
    }
}