package owenwang.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.EntityState;
import owenwang.game.MainGame;

import java.util.function.Consumer;

public abstract class Entity extends Group {
    private static final float HEALTHBAR_WIDTH_PROPORTION = 0.22f;
    protected EntityState state = EntityState.IDLE;
    protected float stateTime = 0;
    public HealthBar healthBar;
    protected final MainGame g;

    public Entity(MainGame g) {
        this.g = g;
        healthBar = new HealthBar(g);
        healthBar.text = "";
        healthBar.setMaxHealth(1);
        healthBar.setHealth(1);
        addActor(healthBar);
    }

    public void takeDamage(int damage) {
        if (getHeight() == 0) return;
        if (healthBar.takeDamage(damage) > 0) {
            if (healthBar.getHealth() > 0) {
                addAction(Actions.delay(0.3f, Actions.run(() -> {
                    stateTime = 0;
                    state = EntityState.HURT;
                })));
            } else {
                state = EntityState.DIE;
                die();
            }
        }

    }

    public abstract void die();
    protected abstract TextureRegion getTexture();

    protected abstract void modifyTextures(Consumer<TextureRegion> f);

    protected float getHeightProportion() {
        return 0.61f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        var hbWidth = getWidth() * HEALTHBAR_WIDTH_PROPORTION;
        var hbHeight = getHeight() * 0.065f;
        var hbY = getHeight() * getHeightProportion();
        healthBar.setPosition( (getWidth() - hbWidth) / 2, hbY);
        healthBar.setSize(hbWidth, hbHeight);
    }

    public void setFlipX(boolean flipX) {
        modifyTextures(t -> {
            if (t.isFlipX() != flipX) {
                t.flip(true, false);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        var color = super.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(getTexture(), getX(), getY(), getOriginX(), getOriginY(), getWidth(),
            getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
