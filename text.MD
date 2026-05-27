Minimum rubric-pass plan
I’ll keep this to engine/model work only and avoid UI layout changes.

1) Finish the character model / encapsulation
Files: Characters/GameCharacter.java, each Characters/Character*.java

Make core fields private instead of protected
Add public getters/setters for:
hp, maxHp
mana, maxMana
stamina
morale
attackPower, defensePower
Add explicit attackPower and defensePower fields to GameCharacter
Update each concrete character constructor to set these values
Why:

This closes the “encapsulation” partial mark
It also aligns with rubric requirement for attack/defense stats
2) Standardize damage formulas
Files: Characters/GameCharacter.java, Bosses/GameBoss.java, all concrete boss/character classes

Create shared methods in GameCharacter for:
calculateAttackDamage(GameBoss target)
calculateDefenseReduction(...)
Use attackPower / defensePower in all player attack calculations
Convert current hard-coded base damage into formula using stats
Ensure all boss attacks use attackPlayerWithRoll(...) and taunt penalty logic
Why:

This makes hit/miss/crit consistent across characters
It guarantees the rubric’s “attackPower / defensePower” requirement is actually used
3) Complete the taunt system fully
Files: Characters/GameCharacter.java, GameEngine/GameEngine.java, GameEngine/BattleController.java

Confirm applyTaunt() and isTaunted() are used by all turn logic
Make boss AI always prefer taunted targets
Add explicit taunt hit/miss penalty in Bosses/GameBoss.attackPlayerWithRoll()
Ensure taunt duration ticks down every turn
Why:

You already have most of the system in place
This closes the “taunt system” rubric item without UI changes
4) Confirm inventory/item structure
Files: Inventory/Item.java, Inventory/Potions.java, GameEngine/GameEngine.java

Verify the inventory is really ArrayList<Item>
Verify each potion is stored as an Item object and removed on use
Keep the potion UI hooks unchanged
Why:

This closes the “ArrayList<Item> inventory” rubric item
It’s already mostly done in your code, so this is a quick validation/fix step
5) Add non-layout summary output for battle end
Files: GameEngine/BattleController.java, GameEngine/GameEngine.java

Add a summary method for end-of-battle stats:
bosses defeated
total turns
result type (victory / defeat / fled)
Use JOptionPane or chat-box output only, not new layout panels
Why:

This fixes the “victory/defeat summary” rubric item
It does not require changing existing screen layout
6) Add one explicit try/catch rubric point
Files: relevant input/selection handling code, or GameEngine/BattleController.java

Add safe parsing/catch logic for any menu or action selection that currently could be invalid
If your current UI does not use typed input, add a small defensive catch around battle action execution
Why:

This covers the rubric’s “try/catch invalid selection” expectation
It can be done in code without modifying screen design
Recommended order
GameCharacter encapsulation + attack/defense stats
Damage/roll system standardization
Taunt system completion
Inventory validation
Battle-end summary text/dialog
Try/catch defensive handling
What I will not touch
Existing layout positions
Button placement
panel structure in BattleScreen.java
If you want, I can now implement step 1 first: convert GameCharacter to private fields and add attackPower/defensePower.

Raptor mini (Preview) • 1x
do all and also i add a result jframe