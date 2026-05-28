# SYSTEM FUNCTIONS & PURPOSE (STUDENT vs FACULTY)
This document is a **high-level map** of the game’s codebase: packages, classes, and the most important functions (methods) and what each one is for.

> Project folder: `RPG_STUDENT_vs_FACULTY/src/`

---

## 1) `Game_UI` (Swing screens / View layer)

### `Menu`
**Purpose:** First screen. Start new game or load a saved battle.

Key functions:
- `main(...)` – launches the menu window.
- `StartActionPerformed(...)` – opens `CharacterSelections`.
- `LoadGameActionPerformed(...)` – loads a saved battle file into `GameEngine` and opens `BattleScreen`.
- `QuitActionPerformed(...)` – exits the app.

---

### `CharacterSelections`
**Purpose:** Pick 3 student heroes (Front/Above/Below), preview stats, then start battle.

Key functions:
- `updateDisplay(GameCharacter character, String imagePath)` – updates the preview panel (name/role/stats/skills/image).
- Character buttons (e.g. `jbtnEthanActionPerformed`) – sets `currentSelectedCharacter`.
- `jbtnAddActionPerformed(...)` – adds selected character into `partyMembers` and sets positions:
  - party index 0 → `"Front"`
  - party index 1 → `"Above"`
  - party index 2 → `"Below"`
- `jbtnstartActionPerformed(...)` – creates `new BattleScreen(partyMembers)` and starts the fight.

---

### `BattleScreen` (implements `IBattleScreenUI`)
**Purpose:** The main battle UI (buttons, narration, HP/Mana bars, images).

Important UI-to-controller handlers:
- `jbtnAttack1ActionPerformed(...)` – triggers **basic attack** (`controller.executePlayerAction(0, actor)`).
- `jbtnAllySkill1/2/3ActionPerformed(...)` – triggers skill 1..3.
- `jbtnDefendActionPerformed(...)` – triggers defend text + boss turn.
- `JbtnTauntActionPerformed(...)` – triggers taunt (`controller.executeTaunt(actor)`).
- `JbtnFlee1ActionPerformed(...)` – triggers flee (`controller.executeFlee(actor)`).
- `jbtnShopActionPerformed(...)` – opens shop dialog (`controller.openShopDialog()`).

Important UI update functions (called by controller):
- `appendToChatBox(String)` / `clearChatBox()` – narration control.
- `setHPBarBoss(...)`, `setHPBarStudents(...)`, `setManaBarBoss(...)`, `setManaBarStudents(...)`, `setRageMeter(...)`, `updateMoraleBar(...)`
- `updateSkillButtons(...)` – shows skill names + cooldown text.
- `updateBossSkillButtons(...)`
- `updatePotionButtons(...)`
- `updateGoldDisplay(int gold)` – shows gold on the SHOP button.
- `setBossImage(String imagePath)` – updates boss sprite.
- `getActiveActor()` – returns which hero is currently acting (Front → Above → Below).

Turn/flow helpers:
- `triggerBossTurn()` – calls `controller.executeBossAction()` after a short delay, then advances actor.
- `advanceTurn()` – rotates active hero (Front/Above/Below), ticks cooldowns once per full party cycle.

---

### `Result`
**Purpose:** Shows victory/defeat summary and routes the player back.

Key functions:
- `displayBattleResults(...)` – victory/defeat display for one boss.
- `displayGameSummary(int bossesDefeated, int totalTurns)` – end-of-game summary.
- `jbtnContinueActionPerformed(...)` – continue or return to menu depending on state.
- `jbtnMenuActionPerformed(...)` – go back to `Menu`.

---

## 2) `GameEngine` (Model + combat rules)

### `GameEngine`
**Purpose:** The “truth” of battle state: boss schedule, turn ownership, rewards, inventory, saving/loading.

Core state:
- `GameState` – `MENU`, `BOSS_SELECT`, `IN_BATTLE`, `BOSS_DEFEATED`, `PARTY_DEFEATED`, `ESCAPED`, `GAME_OVER`
- `bossRound` – wave/round counter (1..6)
- `gold` – currency used by shop
- `inventory` – `ArrayList<Item>` (potions etc.)

Core battle functions:
- `initializeParty(ArrayList<GameCharacter>)` – sets your party into the engine.
- `spawnBoss(BossType)` – sets `currentBoss`, resets turn state, and ensures starting potions exist.
- `spawnNextBoss()` – the 6-round boss schedule:
  - rounds 1–4: random schedule (no repeats)
  - round 5: couple boss (Alyzeh then Bharkyot)
  - round 6: Hydra final boss
- `executePlayerTurn(int skillChoice, GameCharacter actor)` – applies basic attack or skill, handles boss defeat, gives gold reward.
- `executeBossTurn()` – boss AI action (skill vs basic), applies morale drop, checks party defeat.

Rewards / shop currency:
- `getGold()`, `addGold(int)`, `spendGold(int)` – gold balance APIs.

Items / potions:
- `addItem(Item)` – adds item to inventory.
- `usePotion(PotionType, GameCharacter)` – consumes potion with validation.
- `getHpPotions()`, `getManaPotions()`, `getRevivePotions()`

Taunt / targeting:
- `applyTaunt(GameCharacter actor, int turns)` – marks a hero as taunting.

Flee:
- `attemptFlee(GameCharacter actor)` – success sets state to `ESCAPED`, failure punishes the actor.

Save / Load:
- `saveStateToFile(String filePath)` – writes current engine state into a text file.
- `static loadStateFromFile(String filePath)` – recreates engine + party + boss from file.

---

### `BattleController`
**Purpose:** Connects `GameEngine` to the UI, enforces turn rules, updates the UI, and handles popups.

Key functions:
- `spawnNextBoss()` – asks engine for the next boss and updates UI; opens `Result` when game ends.
- `executePlayerAction(int skillChoice, GameCharacter actor)` – runs player action, handles victory transition + shop timing.
- `executeBossAction()` – runs boss turn, handles defeat, updates UI.
- `executeTaunt(GameCharacter actor)` – applies taunt and consumes the player’s turn.
- `executeFlee(GameCharacter actor)` – calls engine flee; on success returns to Menu; on failure boss turn continues (handled by UI).
- `openShopDialog()` – `JOptionPane` shop, buys potions using gold.
- `updateAllUI()` – refreshes bars/buttons/text based on engine state.
- `appendChatMessage(String)` – routes narration to UI.

Autosave:
- `autoSave()` – writes engine state to `savegame.txt` automatically (best-effort).

---

### `CharacterFactory`
**Purpose:** Used mainly by **Load Game** to rebuild party members by name.

Key function:
- `createCharacter(String characterName)` – returns `new CharacterX()` for known heroes.

---

### `IBattleScreenUI`
**Purpose:** Interface used by controller so it can update the UI without depending on concrete Swing types.

Key functions:
- HP/Mana/Rage/Morale setters, chat append/clear, button update methods, `getActiveActor()`.

---

### `GameLauncher`
**Purpose:** Helper/example methods showing how to start battles and call controller actions.

---

## 3) `Characters` (Student heroes)

### `GameCharacter` (abstract base)
**Purpose:** Common hero stats + cooldown logic + status effects.

Core systems:
- Skill cooldowns per skill (1..3): `isSkillOnCooldown(...)`, `startSkillCooldown(...)`, `tickCooldowns()`
- Status effects: panic/stress/silence/confuse + taunt duration (`applyTaunt`, `tickTaunt`)
- Basic combat helpers: damage roll, `attackBossWithRoll(...)`

Abstract functions each hero must implement:
- `String[] getSkillname()`
- `String useSkills(int skillNumber, ArrayList<GameBoss> activeBosses)`
- `String basicAttack(ArrayList<GameBoss> activeBosses)`
- `String defend()`
- `String[] getPassivename()`, `double[] getPassiveValue()`
- `String[] displayStats()`

### `CharacterShawn`, `CharacterEthan`, `CharacterDwight`, `CharacterOmar`, `CharacterPrincess`
**Purpose:** Concrete hero implementations (skills + stats + passives).

---

## 4) `Bosses` (Faculty enemies)

### `GameBoss` (abstract base)
**Purpose:** Boss stats, skill cooldowns, rage meter, and damage roll logic.

Key functions:
- `getSkillCooldownRemaining(...)`, `startSkillCooldown(...)`, `tickCooldowns()`
- `getImagePath()` – default image path is `/assets/{bossName}.png`
- `takeDamage(int)` / rage utilities

Abstract functions each boss must implement:
- `String[] getSkillname()`
- `String useSkills(int skillNumber, ArrayList<GameCharacter> partyStudents)`
- `String basicAttack(ArrayList<GameCharacter> partyStudents)`
- `String[] displayBossStats()`
- `String[] getPassivename()`
- `int defend()`

### Concrete bosses
Examples: `Hydra`, `Meruh` (class file `Maru.java`), `Alyzeh`, `Bharkyot`, etc.

---

## 5) `Inventory` (Items)

### `Item` (abstract)
**Purpose:** Base inventory item (name/type/value).

### `Potions` (extends `Item`)
**Purpose:** HP/MANA/REVIVE potions.

### `EmptyInventoryException`
**Purpose:** Thrown when a potion type is requested but none exist.

