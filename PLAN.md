# Atari Boxing Java + GitHub Pages Plan

## Goal
Recreate an Atari-style boxing game in Java and publish it as a static web build on GitHub Pages.

## Technical Direction
- Engine: LibGDX
- Language: Java 11+
- Target:
  - Web build for GitHub Pages using GWT backend

## Execution Phases

### Phase 1 - Foundation (in progress)
- [x] Create Gradle multi-module layout (`core`, `html`)
- [x] Add web launcher
- [x] Add initial rendering/input game loop

### Phase 2 - Core Gameplay
- [x] Implement movement and ring boundaries
- [x] Implement jab/cross attacks and collision hit checks
- [x] Implement block, stamina, health, timer, and win conditions
- [x] Implement starter AI for single-player mode

### Phase 3 - Atari Feel
- [ ] Replace rectangles with retro sprite sheets
- [ ] Restrict palette and add CRT-like post effect
- [ ] Add punch, block, bell, and crowd audio cues

### Phase 4 - Polish
- [ ] Improve hit feedback (flash, shake, impact pause)
- [ ] Balance damage, cooldowns, AI behavior, and stamina
- [ ] Add menu, pause, rematch flow

### Phase 5 - Deployment
- [x] Add GitHub Actions workflow for Pages deploy
- [ ] Add Gradle wrapper files
- [ ] Push to `main` and enable Pages from Actions
- [ ] Verify live site and fix any asset path issues

## Controls (Current Prototype)
- Player 1: `A` `D` move, `W` jab, `S` cross, `Q` block
- Player 2: arrows move, `Up` jab, `Down` cross, `/` block
- Other: `F1` toggle AI/manual P2, `R` restart round

## Immediate Next Tasks
1. Generate Gradle wrapper files.
2. Compile web module and verify output in `html/build/dist`.
3. Add placeholder pixel art assets and audio stubs.
