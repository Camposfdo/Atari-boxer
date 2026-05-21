package com.boxinggame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class AtariBoxingGame extends ApplicationAdapter {
    private static final float WORLD_WIDTH = 960f;
    private static final float WORLD_HEIGHT = 540f;
    private static final float RING_LEFT = 100f;
    private static final float RING_RIGHT = 860f;

    private OrthographicCamera camera;
    private ShapeRenderer shapes;
    private SpriteBatch batch;
    private BitmapFont font;

    private Fighter p1;
    private Fighter p2;

    private float roundTime = 99f;
    private boolean aiEnabled = true;
    private String roundResult = "";

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        shapes = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        resetRound();
    }

    @Override
    public void render() {
        float dt = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
        handleMetaInput();

        if (roundResult.isEmpty()) {
            updateRound(dt);
        }

        drawScene();
    }

    private void handleMetaInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetRound();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            aiEnabled = !aiEnabled;
        }
    }

    private void updateRound(float dt) {
        roundTime -= dt;

        updatePlayerOne(dt);
        if (aiEnabled) {
            updateAI(dt);
        } else {
            updatePlayerTwo(dt);
        }

        p1.updateTimers(dt);
        p2.updateTimers(dt);

        resolvePunches(p1, p2);
        resolvePunches(p2, p1);

        if (p1.health <= 0f) {
            roundResult = "P2 wins by KO";
        } else if (p2.health <= 0f) {
            roundResult = aiEnabled ? "AI wins by KO" : "P1 wins by KO";
        } else if (roundTime <= 0f) {
            if (Math.abs(p1.health - p2.health) < 0.01f) {
                roundResult = "Draw";
            } else if (p1.health > p2.health) {
                roundResult = "P1 wins by decision";
            } else {
                roundResult = aiEnabled ? "AI wins by decision" : "P2 wins by decision";
            }
        }
    }

    private void updatePlayerOne(float dt) {
        float move = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) move -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) move += 1f;
        p1.move(move, dt);

        boolean block = Gdx.input.isKeyPressed(Input.Keys.Q);
        p1.setBlocking(block);

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            p1.tryPunch(PunchType.JAB);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            p1.tryPunch(PunchType.CROSS);
        }
    }

    private void updatePlayerTwo(float dt) {
        float move = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) move -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) move += 1f;
        p2.move(move, dt);

        boolean block = Gdx.input.isKeyPressed(Input.Keys.SLASH);
        p2.setBlocking(block);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            p2.tryPunch(PunchType.JAB);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            p2.tryPunch(PunchType.CROSS);
        }
    }

    private void updateAI(float dt) {
        float distance = p1.x - p2.x;
        float absDistance = Math.abs(distance);
        float moveDirection = absDistance > 110f ? Math.signum(distance) : 0f;
        p2.move(moveDirection, dt);

        boolean shouldBlock = p1.attackTimer > 0.08f && absDistance < 100f;
        p2.setBlocking(shouldBlock);

        if (p2.cooldown <= 0f && absDistance < 90f) {
            if (MathUtils.randomBoolean(0.65f)) {
                p2.tryPunch(PunchType.JAB);
            } else {
                p2.tryPunch(PunchType.CROSS);
            }
        }
    }

    private void resolvePunches(Fighter attacker, Fighter defender) {
        if (attacker.pendingPunch == PunchType.NONE) {
            return;
        }

        Rectangle hit = attacker.getHitBox();
        Rectangle hurt = defender.getBody();
        if (!hit.overlaps(hurt)) {
            attacker.pendingPunch = PunchType.NONE;
            return;
        }

        float damage = attacker.pendingPunch == PunchType.JAB ? 6f : 10f;
        if (defender.blocking) {
            damage *= 0.35f;
            defender.stamina = Math.max(0f, defender.stamina - 8f);
        }
        defender.health = Math.max(0f, defender.health - damage);
        attacker.pendingPunch = PunchType.NONE;
    }

    private void drawScene() {
        Gdx.gl.glClearColor(0.95f, 0.9f, 0.74f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapes.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(new Color(0.88f, 0.76f, 0.5f, 1f));
        shapes.rect(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);

        shapes.setColor(new Color(0.72f, 0.18f, 0.18f, 1f));
        shapes.rect(RING_LEFT - 20f, 120f, (RING_RIGHT - RING_LEFT) + 40f, 8f);
        shapes.rect(RING_LEFT - 20f, 412f, (RING_RIGHT - RING_LEFT) + 40f, 8f);

        drawFighter(p1, new Color(0.12f, 0.25f, 0.85f, 1f));
        drawFighter(p2, new Color(0.88f, 0.22f, 0.1f, 1f));

        shapes.end();

        batch.begin();
        font.setColor(Color.BLACK);
        font.draw(batch, "ATARI BOXING PROTOTYPE", 20f, 520f);
        font.draw(batch, "Timer: " + (int) Math.max(0f, roundTime), 430f, 520f);
        font.draw(batch, aiEnabled ? "Mode: P1 vs AI (F1 to toggle)" : "Mode: P1 vs P2 (F1 to toggle)", 20f, 500f);
        font.draw(batch, "P1 health: " + (int) p1.health + " stamina: " + (int) p1.stamina, 20f, 35f);
        font.draw(batch, (aiEnabled ? "AI" : "P2") + " health: " + (int) p2.health + " stamina: " + (int) p2.stamina, 630f, 35f);
        font.draw(batch, "P1 controls: A/D move, W jab, S cross, Q block", 20f, 80f);
        font.draw(batch, "P2 controls: arrows move, Up jab, Down cross, / block", 20f, 60f);

        if (!roundResult.isEmpty()) {
            font.getData().setScale(1.4f);
            font.draw(batch, roundResult + " | Press R to restart", 250f, 280f);
            font.getData().setScale(1f);
        }
        batch.end();
    }

    private void drawFighter(Fighter fighter, Color color) {
        shapes.setColor(color);
        shapes.rect(fighter.x - fighter.width / 2f, fighter.y, fighter.width, fighter.height);

        if (fighter.blocking) {
            shapes.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
            shapes.rect(fighter.x - (fighter.width / 2f) + 4f, fighter.y + fighter.height * 0.55f, fighter.width - 8f, 10f);
        }

        if (fighter.attackTimer > 0f) {
            Rectangle punchBox = fighter.getHitBox();
            shapes.setColor(new Color(1f, 0.95f, 0.3f, 1f));
            shapes.rect(punchBox.x, punchBox.y, punchBox.width, punchBox.height);
        }
    }

    private void resetRound() {
        p1 = new Fighter(260f, 170f, true);
        p2 = new Fighter(700f, 170f, false);
        roundTime = 99f;
        roundResult = "";
    }

    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }

    private enum PunchType {
        NONE,
        JAB,
        CROSS
    }

    private static final class Fighter {
        private static final float SPEED = 240f;
        private static final float WIDTH = 42f;
        private static final float HEIGHT = 110f;

        private float x;
        private float y;
        private float width;
        private float height;
        private boolean facingRight;

        private float health = 100f;
        private float stamina = 100f;
        private float cooldown = 0f;
        private float attackTimer = 0f;
        private boolean blocking;
        private PunchType pendingPunch = PunchType.NONE;

        private Fighter(float x, float y, boolean facingRight) {
            this.x = x;
            this.y = y;
            this.facingRight = facingRight;
            this.width = WIDTH;
            this.height = HEIGHT;
        }

        private void move(float axis, float dt) {
            x += axis * SPEED * dt;
            x = MathUtils.clamp(x, RING_LEFT, RING_RIGHT);
        }

        private void setBlocking(boolean enabled) {
            blocking = enabled && stamina > 2f;
        }

        private void tryPunch(PunchType type) {
            if (cooldown > 0f || stamina < 8f) {
                return;
            }

            pendingPunch = type;
            attackTimer = type == PunchType.JAB ? 0.16f : 0.24f;
            cooldown = type == PunchType.JAB ? 0.28f : 0.45f;
            stamina = Math.max(0f, stamina - (type == PunchType.JAB ? 8f : 14f));
        }

        private void updateTimers(float dt) {
            attackTimer = Math.max(0f, attackTimer - dt);
            cooldown = Math.max(0f, cooldown - dt);

            float regen = blocking ? 5f : 12f;
            stamina = Math.min(100f, stamina + regen * dt);

            if (attackTimer <= 0f) {
                pendingPunch = PunchType.NONE;
            }
        }

        private Rectangle getBody() {
            return new Rectangle(x - width / 2f, y, width, height);
        }

        private Rectangle getHitBox() {
            float reach = pendingPunch == PunchType.JAB ? 26f : 42f;
            float direction = facingRight ? 1f : -1f;
            float hx = direction > 0f ? x + width / 2f : x - width / 2f - reach;
            return new Rectangle(hx, y + height * 0.45f, reach, 20f);
        }
    }
}
