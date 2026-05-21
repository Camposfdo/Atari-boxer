# Atari Boxing 2 (Java + GitHub Pages)

This project recreates an Atari-inspired boxing game in Java using LibGDX.

## Project Layout
- `core`: shared gameplay logic
- `html`: browser build target for GitHub Pages

## Current State
- Prototype with movement, attacks, block, stamina, health, AI, timer, and win states.

## Build Web Output
Run:

`gradlew.bat html:dist`

Output directory:

`html/build/dist`

## Deploy to GitHub Pages
1. Push to `main`.
2. In repo settings, set Pages source to GitHub Actions.
3. Workflow `.github/workflows/deploy-pages.yml` builds and publishes the web output.

## Controls
- P1: `A` `D` move, `W` jab, `S` cross, `Q` block
- P2: arrow keys move, `Up` jab, `Down` cross, `/` block
- `F1`: toggle AI/manual P2
- `R`: restart round
