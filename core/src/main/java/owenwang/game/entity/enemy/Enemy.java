package owenwang.game.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.EntityState;
import owenwang.game.MainGame;
import owenwang.game.Util;
import owenwang.game.entity.Entity;
import owenwang.game.entity.Player;
import owenwang.game.ui.IntentLabel;

import java.awt.*;

public abstract class Enemy extends Entity {
    private static final float GLOW_WIDTH_PROPORTION = 0.22f;
    public boolean targeted;
    private IntentLabel intentLabel = new IntentLabel(this);
    public enum Intent {
        ATTACK, DEFEND;
    }

    public Intent intent;
    public int intentValue = 0;

    public Enemy(MainGame g, String name) {
        super(g);
        healthBar.text = name;
        addActor(intentLabel);
    }

    protected Player getPlayer() {
        return g.player;
    }

    protected void attack(float delay, EntityState state) {
        this.state = state;
        stateTime = 0;
        addAction(Actions.delay(delay, Actions.run(() -> {
            getPlayer().takeDamage(intentValue);
        })));
    }

    protected float defend(float delay, EntityState state) {
        this.state = state;
        addAction(Actions.delay(delay, Actions.run(() -> {
            healthBar.addDefence(intentValue);
        })));
        return delay + 0.5f;
    }

    public void beginTurn() {
        healthBar.setDefence(0);
    }

    public void playTurn() {
        intent = null;
    }

    public abstract float turnLength();

    public abstract void decideIntent(int turn);

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        intentLabel.setBounds(0, healthBar.getY() + healthBar.getHeight(), getWidth(), getHeight() * 0.1f);
    }

    @Override
    public void die() {
        stateTime = 0;
        addAction(Actions.delay(0.5f, Actions.sequence(Actions.fadeOut(1f), Actions.removeActor())));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (targeted) {
            batch.end();

            g.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            Util.glEnableTransparent();
            g.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            var glowMargin = getWidth() * (1 - GLOW_WIDTH_PROPORTION) / 2;
            g.shapeRenderer.setColor(1, 1, 0, 0.5f);
            g.shapeRenderer.rect(getX() + glowMargin, getY() + glowMargin, getWidth() - 2 * glowMargin, getHeight() - 2 * glowMargin);
            g.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }
        super.draw(batch, parentAlpha);
    }
}
