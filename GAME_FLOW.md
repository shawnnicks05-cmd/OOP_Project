# GAME FLOW (STUDENT vs FACULTY)
This document explains the **player flow** (screens) and the **battle loop** (turn system) from start to finish.

---

## 1) Screen / UI Flow

### A) Start
1. App starts at: **`Menu`**
2. Player chooses:
   - **START GAME** → goes to `CharacterSelections`
   - **LOAD GAME** → loads a saved battle into `GameEngine`, then opens `BattleScreen`
   - **QUIT** → exits

### B) Character Selection
1. Player selects a hero (button) → preview updates (stats/skills/image)
2. Player clicks **ADD TO PARTY**
3. Repeat until party size = **3**
4. Positions are assigned automatically:
   - 1st hero → `Front`
   - 2nd hero → `Above`
   - 3rd hero → `Below`
5. Player clicks **START** → `BattleScreen` opens and the first boss wave begins.

---

## 2) Battle Flow (Controller + Engine)

### A) Battle initialization
1. `BattleScreen` creates `BattleController`
2. `GameEngine.initializeParty(party)`
3. `BattleController.spawnNextBoss()` → `GameEngine.spawnNextBoss()` selects the next boss and sets:
   - `GameState = IN_BATTLE`
   - `isPlayerTurn = true`

### B) Turn order (players)
Turn order is UI-driven (not engine-driven):
1. **Front** acts
2. **Above** acts
3. **Below** acts
Then repeats.

Cooldown ticking:
- Cooldowns tick down **once per full party cycle** (when the UI loops back to Front).

### C) Player action phase
On the player’s turn, the UI enables buttons and the player can:
- **ATTACK** (basic attack)
- **Skill 1 / 2 / 3**
- **Defend**
- **Taunt**
- **Use Potions**
- **Flee**
- **Shop** (opens shop dialog; only makes sense between waves)

All actions route through `BattleController`, which calls `GameEngine.executePlayerTurn(...)` or special functions (taunt/flee/potion).

### D) Boss action phase
After a player action, `BattleScreen.triggerBossTurn()` calls:
- `BattleController.executeBossAction()` → `GameEngine.executeBossTurn()`

Boss AI:
- Boss tries to use a skill (if not on cooldown and has mana), otherwise uses basic attack.
- After boss acts, boss skill cooldowns tick down.

### E) Win / Loss / Escape checks
After actions:

**1) Boss defeated**
- Engine sets `GameState = BOSS_DEFEATED`
- Engine increases `bossesDefeated`
- Engine gives gold reward (ex: +25g)
- Controller prints victory text
- After ~2 seconds:
  - Shop can open
  - Next boss spawns

**2) Party defeated**
- Engine sets `GameState = PARTY_DEFEATED`
- Controller shows defeat message (and can open Result screen)

**3) Escape success**
- Engine sets `GameState = ESCAPED`
- Battle screen closes and returns to Menu

---

## 3) Boss/Wave Schedule (6 total)

1. **Round 1–4:** Random bosses (pre-generated schedule so no repeats)
2. **Round 5:** Couple boss (Alyzeh then Bharkyot, same round logic)
3. **Round 6:** Final boss (Hydra)
4. After round 6, the game ends → `Result` summary.

---

## 4) Gold + Shop Flow

Gold:
- Stored in `GameEngine.gold`
- Earned after defeating bosses

Shop:
- Opens between waves
- Uses `spendGold(cost)` to buy potions
- Items are stored in `inventory` and persist across waves

UI:
- Gold is shown on the SHOP button text: `SHOP (###g)`

---

## 5) Save / Load Flow

### Auto-save
The battle controller writes a text save file (default `savegame.txt`) after major events (spawns/actions).

### Load game
Menu → **LOAD GAME**:
1. Select a save file (example: `savegame.txt`)
2. Engine loads state (`GameEngine.loadStateFromFile`)
3. If there is an active boss in the save:
   - BattleScreen opens using that loaded engine state

