package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import owenwang.game.EntityState;
import owenwang.game.MainGame;
import owenwang.game.Util;
import owenwang.game.entity.Entity;
import owenwang.game.entity.Player;
import owenwang.game.ui.IntentLabel;

public abstract class Enemy extends Entity {
    private static final float GLOW_WIDTH_PROPORTION = 0.22f;
    public int turn = 0;
    public boolean targeted;
    private final IntentLabel intentLabel = new IntentLabel(this);

    public enum Intent {
        ATTACK, DEFEND, HEAL, CAST, UNKNOWN, STUN;
    }

    public Intent intent;
    public int intentValue = 0;

    public Enemy(String name) {
        healthBar.text = name;
        addActor(intentLabel);
    }

    protected Player getPlayer() {
        return MainGame.INSTANCE.player;
    }

    protected void turn(float delay, Action action) {
        stateTime = 0;
        addAction(Actions.delay(delay, action));
    }


    public void actionLabel(String text) {
        var label = new Label(text, MainGame.SKIN, "cardTitle");
        label.setBounds(0, healthBar.getY() + healthBar.getHeight(), getWidth(), label.getHeight());
        label.setFontScale(0.5f);
        addActor(label);
        label.setOrigin(Align.center);
        label.setAlignment(Align.center);
        label.setColor(1, 1, 1, 0);
        label.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.fadeIn(0.2f, Interpolation.fastSlow),
            Actions.fadeOut(0.7f, Interpolation.slowFast),
            Actions.removeActor()
        ));
    }


    protected void attack(float delay) {
        attack(delay, "Strike");
    }

    protected void attack(float delay, String label) {
        stateTime = 0;
        addAction(Actions.delay(delay, Actions.run(() -> {
            getPlayer().takeDamage(intentValue);
        })));
        if (label != null) actionLabel(label);
    }

    protected float defend(float delay) {
        return defend(delay, "Defend");
    }

    protected float defend(float delay, String label) {
        addAction(Actions.delay(delay, Actions.run(() -> {
            healthBar.addDefence(intentValue);
        })));
        actionLabel(label);
        return delay + 0.5f;
    }

    public void beginTurn() {
        turn++;
        healthBar.setDefence(0);
    }

    public void playTurn() {
        intent = null;
    }

    public float preTurn() {
        return 0.5f;
    }

    public abstract float postTurn();

    public abstract void decideIntent();

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        intentLabel.setBounds(0, healthBar.getY() + healthBar.getHeight(), getWidth(), getHeight() * 0.1f);
    }



    public void _die(float delay, float duration) {
        addAction(Actions.delay(delay, Actions.sequence(Actions.run(() -> {

            MainGame.INSTANCE.enemies.enemies.remove(this);
        }), Actions.removeActor())));
        MainGame.INSTANCE.onEnemyDied(delay + duration);
    }

    public void die() {
        var deathDuration = death == null ? 0.5f : death.getAnimationDuration();
        _die(0.3f, deathDuration);
        playAnimation(death);
        if (death == null) return;
        death.setPlayMode(Animation.PlayMode.NORMAL);
        returnToIdle = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (targeted) {
            var glowMargin = getHeight() * (1 - GLOW_WIDTH_PROPORTION) / 2;
            batch.setColor(1, 1, 0, 0.5f);
            var rect = getBounds();
            Util.rect(batch, getTrueX() + rect.x, getY() + rect.y, getOriginX(), getOriginY(), rect.width, rect.height, getScaleX(), getScaleY(), getRotation());
        }
        super.draw(batch, parentAlpha);
    }

    protected void stunned() {
        actionLabel("Stunned!");
        turn--;
    }
}
