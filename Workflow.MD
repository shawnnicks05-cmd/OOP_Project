# 2D Turn-Based RPG Battle Engine Workflow

This document outlines the architectural blueprint and state machine lifecycle for the 2D turn-based combat system. It handles the decoupling of the Java Swing UI layer (`BattleScreen.java`) from the underlying object-oriented mechanics layout (`GameCharacter.java`).

---

## 1. System Architecture Overview

The battle system utilizes a Model-View-Controller (MVC) adaptation tailored for Java Swing applications:

* **Model (`Game_Logic`):** Contains `GameCharacter.java` (Abstract Base) and individual hero/enemy classes (e.g., `CharacterShawn`, `CharacterEthan`, `EnemyBoss`). Tracks persistent state variables ($HP$, $Mana$, $Morale$).
* **View (`Game_UI`):** `BattleScreen.java` (NetBeans Generated JFrame Form). Handles component layouts, button event listeners, and live display text streams.
* **Controller (`Game_Engine`):** Orchestrates structural math processing, status updates, turn mechanics, and win/loss verification loops.

---

## 2. Core Turn-Based Lifecycle

Every individual battle loop sequence adheres to the following structural pipeline:

### Phase 1: Initiation & Turn Start
1. The engine scans the active combatants.
2. Passive traits are evaluated via polymorphism calls to `.getPassiveValue()`.
3. Multipliers are applied cumulatively to character attributes.
4. Active status effect timers tick down.

### Phase 2: Action Deferral (State: Awaiting Input)
* **Player Character Turn:** 1. UI controls illuminate (`JButton.setEnabled(true)`).
    2. The engine halts processing and listens for button click actions.
* **Enemy / Boss Turn:**
    1. UI controls freeze (`JButton.setEnabled(false)`).
    2. The engine runs the AI determination module to select an action strategy.

### Phase 3: Selection Popups (Target Isolation)
1. When a targeted action occurs (Basic Attack or Single-Target Skill), a modal dialogue breaks the focus path:
```java
int choice = JOptionPane.showOptionDialog(... enemyBosses ...);