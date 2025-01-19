package owenwang.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import owenwang.game.MainGame;
import owenwang.game.Util;

import java.util.function.Consumer;

public abstract class Entity extends Group {
    protected float stateTime = 0;
    protected boolean redOnDamage = false;
    protected boolean isHurt = false;
    protected Animation<TextureRegion> idle;
    protected Animation<TextureRegion> hurt;
    @Null protected Animation<TextureRegion> death = null;
    public HealthBar healthBar;

    public Entity() {
        healthBar = new HealthBar();
        healthBar.text = "";
        healthBar.setMaxHealth(1);
        healthBar.setHealth(1);
        addActor(healthBar);
    }

    public int takeDamage(int damage) {
        var d = healthBar.takeDamage(damage);
        if (d > 0) {
            damageLabel(String.format("[RED]-%d", d), false);
            if (healthBar.getHealth() > 0) {
                addAction(Actions.delay(0.3f, Actions.run(() -> {
                    stateTime = 0;
                    isHurt = true;
                    playAnimation(hurt);
                })));
            } else {
                die();
            }
        } else if (damage > 0 && d == 0) {
            damageLabel("Blocked", true);
        }
        return d;
    }

    protected void damageLabel(String text, boolean center) {
        var label = new Label(text, MainGame.SKIN, "cardTitle");
        var bounds = getBounds();
        var x = center ? (getWidth() - label.getWidth()) / 2 :
            getTrueX() - getX() + bounds.x + Util.RANDOM.nextFloat(bounds.width - label.getWidth());
        var y = bounds.y + bounds.height;
        float moveY = bounds.height * 0.75f;
        addActor(label);
        label.setOrigin(Align.center);
        label.addAction(Actions.parallel(
            Actions.moveTo(x, y),
            Actions.sequence(
                Actions.fadeOut(1f),
                Actions.removeActor()
            ),
            Actions.moveBy(0, moveY, 1f)
        ));
    }

    public abstract void die();
    private Animation<TextureRegion> animation = null;
    protected boolean returnToIdle = true;

    protected void playAnimation(Animation<TextureRegion> animation) {
        if (animation == null) return;
        this.animation = animation;
        stateTime = 0;
    }

    protected abstract void modifyTextures(Consumer<TextureRegion> f);

    protected float inset() { return 0f; }

    protected float bottomInset() { return inset(); }

    protected float getHeightProportion() {
        return 0.61f;
    }

    protected Rectangle getBounds() {
        var defaultWidth = getHeight() * 0.2f;
        var margin = (getHeight()  - defaultWidth) / 2;
        return new Rectangle(margin, margin, defaultWidth, defaultWidth);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        var hbWidth = getBounds().width;
        var hbHeight = getHeight() * 0.065f;
        var hbY = getHeight() * getHeightProportion();
        healthBar.setPosition(( getWidth() - hbWidth) / 2, hbY);
        healthBar.setSize(hbWidth, hbHeight);
    }

    public void setFlipX(boolean flipX) {
        modifyTextures(t -> {
            if (t.isFlipX() != flipX) {
                t.flip(true, false);
            }
        });
    }

    public float getTrueX() {
        return getX() + (getWidth() - getHeight()) / 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        var color = super.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (this.animation != null && animation.isAnimationFinished(stateTime) && returnToIdle) {
            animation = null;
            stateTime = 0;
        }
        if (this.isHurt && stateTime > 0.4f) {
            isHurt = false;
            stateTime = 0;
        }
        if (redOnDamage && isHurt) batch.setColor(batch.getColor().mul(1f, 0.4f, 0.4f,  1f));
        var t = this.animation == null ? idle.getKeyFrame(stateTime, true) : animation.getKeyFrame(stateTime, false);
        batch.draw(t, getTrueX() + inset() * getHeight(), getY() + bottomInset() * getHeight(), getOriginX(), getOriginY(),
            getHeight() - 2 * inset() * getHeight(), getHeight() - 2 * inset() * getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
