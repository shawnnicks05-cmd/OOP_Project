# Adding a New Boss (Faculty Boss)

This project uses Java Swing + a turn-based battle engine. To add a new boss that can appear in battles, you normally update **4 areas**:

1. A new `BossX.java` class (the boss logic)
2. Assets (the boss image)
3. `GameEngine.java` (so the boss can spawn)
4. *(Optional but recommended)* Save/Load + docs

---

## 1) Create the new boss class

### File location
Create a new file here:

`RPG_STUDENT_vs_FACULTY/src/Bosses/YourBoss.java`

### Naming rule (IMPORTANT)
The game loads boss images using the **class name**:

```java
return "/assets/" + cls + ".png";
```

So if your class is named `YourBoss`, your image should be:

`/assets/YourBoss.png`

### Template
Copy an existing boss (example: `Ahzzee.java` or `Jenateh.java`) and edit it.

Here is a clean template you can start from:

```java
package Bosses;

import Characters.GameCharacter;
import java.util.ArrayList;
import java.util.Random;

public class YourBoss extends GameBoss {

    private final Random rand = new Random();

    public YourBoss() {
        super("YourBoss", "Some Classification", "Medium", "Some Damage Type");

        // Base stats (set BOTH current and max!)
        this.hpBoss = 120;
        this.maxHp = 120;
        this.mana = 100;
        this.maxMana = 100;
        this.rage = 0;
        this.defence = 12;

        // Applies difficulty multipliers (HP/Mana/Damage) and ensures full HP after scaling.
        applyDifficultyScaling();
    }

    @Override
    public String[] getSkillname() {
        // IMPORTANT: the Battle UI expects 3 skills for bosses
        return new String[] {"Skill One", "Skill Two", "Skill Three"};
    }

    @Override
    public String[] getPassivename() {
        return new String[] {"Passive One"};
    }

    @Override
    public String[] displayBossStats() {
        return new String[] {
            "Hp: " + this.hpBoss + "/" + this.maxHp,
            "Mana: " + this.mana + "/" + this.maxMana,
            "Rage: " + this.rage + "%",
            "Classification: " + this.classification
        };
    }

    @Override
    public int defend() {
        // Used when boss defends (if you ever call it)
        return this.defence + 20;
    }

    @Override
    public String basicAttack(ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets.";
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        int damage = scaledDamage(25);
        String result = attackPlayerWithRoll(target, damage, "strikes", target.isTaunted());

        this.addRage(12);
        if (isEnragedDoTActive()) {
            result += " [ENRAGED]";
        }
        return result;
    }

    @Override
    public String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents) {
        if (partyStudents.isEmpty()) return this.name + " has no targets left.";
        GameCharacter target = partyStudents.get(rand.nextInt(partyStudents.size()));

        switch (skillNumber) {
            case 1 -> {
                int cost = scaledManaCost(40);
                if (this.mana < cost) return this.name + " attempted [Skill One] but lacks Mana!";
                this.mana -= cost;
                this.addRage(18);
                int dmg = scaledDamage(45);
                target.takeDamage(dmg);
                return this.name + " uses [Skill One] on " + target.getName() + " for " + dmg + " damage!";
            }
            case 2 -> {
                int cost = scaledManaCost(35);
                if (this.mana < cost) return this.name + " attempted [Skill Two] but lacks Mana!";
                this.mana -= cost;
                this.addRage(15);
                return this.name + " uses [Skill Two]!";
            }
            case 3 -> {
                int cost = scaledManaCost(50);
                if (this.mana < cost) return this.name + " attempted [Skill Three] but lacks Mana!";
                this.mana -= cost;
                this.addRage(22);
                int dmg = scaledDamage(55);
                target.takeDamage(dmg);
                return this.name + " uses [Skill Three] on " + target.getName() + " for " + dmg + " damage!";
            }
            default -> {
                return this.name + " hesitates...";
            }
        }
    }
}
```

---

## 2) Add the boss image (assets)

Put the image in:

`RPG_STUDENT_vs_FACULTY/src/assets/`

Name it exactly:

`YourBoss.png`

Notes:
- PNG with transparent background works best.
- The build output also contains assets, but you normally only edit `src/assets/` and your IDE/build will copy it.

---

## 3) Make the boss spawn in the game

### A) Add it to the BossType enum
Edit:

`RPG_STUDENT_vs_FACULTY/src/GameEngine/GameEngine.java`

Find:

```java
public enum BossType { ... }
```

Add your boss:

```java
YOURBOSS
```

(Convention: enum values are uppercase.)

### B) Add it to createBossByType(...) and spawnBoss(...)
In the same file, add:

```java
case YOURBOSS -> new YourBoss();
```

in both:
- `spawnBoss(BossType bossType)`
- `createBossByType(BossType bossType)`

### C) If you want it in random rounds (1–4)
Add your enum to the `RANDOM_BOSSES` list near the top:

```java
private static final BossType[] RANDOM_BOSSES = {
   ...,
   BossType.YOURBOSS
};
```

---

## 4) (Optional) Save/Load compatibility

Save files store boss names using:

```java
b.getClass().getSimpleName().toUpperCase()
```

So as long as your enum name matches (example: `YourBoss` class + `BossType.YOURBOSS`), load will work.

---

## Quick checklist (common issues)

- ✅ New class is in `src/Bosses/`
- ✅ Constructor sets BOTH `hpBoss/maxHp` and `mana/maxMana`, then calls `applyDifficultyScaling()`
- ✅ `getSkillname()` returns **exactly 3** boss skills (UI expects 3)
- ✅ Image exists: `src/assets/YourBoss.png`
- ✅ Added `BossType.YOURBOSS`
- ✅ Added `case YOURBOSS -> new YourBoss();` in both factory switches

