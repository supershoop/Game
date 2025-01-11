package owenwang.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.Constants;
import owenwang.game.MainGame;
import owenwang.game.Util;

public class HealthBar extends Actor {
    private static final float BAR_HEIGHT_PROPORTION = 0.35f;
    private static final float BAR_PADDING_HEIGHT_PROPORTION = 0.025f;
    private static final float DEFENCE_BAR_SCALE = 1.05f;
    private static final float DEFENCE_ICON_W_PROPORTION = 0.25f;
    private static final float DEFENCE_ICON_OFFSET_W_PROPORTION = 0.11f;
    private int maxHealth;
    private int health;
    private int defence = 0;

    private final Texture iconDefence;
    private BitmapFont font;
    private int fontSize = 0;
    private BitmapFont defenceFont;
    private int defenceFontSize = 0;
    private MainGame g;
    public String text;

    private float healthBarWidth;
    private float healthChangeBarWidth = 0;

    private class HealthChangeAction extends TemporalAction {
        private float initialHealth;
        private float finalHealth;

        public HealthChangeAction(float initialHealth, float finalHealth, float duration, Interpolation interpolation) {
            super(duration, interpolation);
            this.initialHealth = initialHealth;
            this.finalHealth = finalHealth;
        }

        @Override
        protected void update(float percent) {
            if (initialHealth > finalHealth) {
                healthBarWidth = calculateBarWidth(health);
                healthChangeBarWidth = calculateBarWidth(initialHealth - finalHealth) * (1 - percent);
            } else {
                healthBarWidth = calculateBarWidth(initialHealth) + calculateBarWidth(finalHealth - initialHealth) * percent;
            }
        }

        @Override
        public void finish() {
            healthChangeBarWidth = 0;
            healthBarWidth = calculateBarWidth(health);
        }
    }

    public HealthBar(MainGame g) {
        this.g = g;
        iconDefence = Assets.manager.get(Assets.iconDefence);
    }

    private void genFont() {
        fontSize = (int) (getHeight() * .3);
        font = Assets.getBoldFont(fontSize);
        defenceFontSize = (int) (getHeight() * .5);
        defenceFont = Assets.getBoldFont(defenceFontSize);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void addDefence(int defence) {
        this.defence += defence;
    }


    public void setHealth(int health) {
        this.health = health;
        this.healthBarWidth = calculateBarWidth(health);
    }

    public int getHealth() {
        return health;
    }

    public int takeDamage(int damage) {
        clearActions();
        defence -= damage;
        if (defence < 0) {
            addAction(new HealthChangeAction(health, Math.max(0, health + defence), 0.75f, Interpolation.fastSlow));
            health = Math.max(0, health + defence);
            var d = -defence;
            defence = 0;
            return d;
        } else return 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        var color = getColor();
        g.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        Util.glEnableTransparent();
        g.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float barHeight = BAR_HEIGHT_PROPORTION * getHeight();

        if (defence > 0) {
            var w = getWidth() * DEFENCE_BAR_SCALE;
            var h = barHeight + getWidth() * (DEFENCE_BAR_SCALE - 1);
            g.shapeRenderer.setColor(Constants.DEFENCE);
            g.shapeRenderer.rect(getX() - (w - getWidth()) / 2, getY() - (h - barHeight) / 2, w, h);
            var size = getWidth() * DEFENCE_ICON_W_PROPORTION;
            var offset = getWidth() * DEFENCE_ICON_OFFSET_W_PROPORTION;
            g.shapeRenderer.circle(getX() - offset, getY() + barHeight / 2, size / 2);
        }

        g.shapeRenderer.setColor(Color.BLACK);
        g.shapeRenderer.rect(getX(), getY(), getWidth(), barHeight);
        g.shapeRenderer.setColor(Color.RED);
        g.shapeRenderer.rect(getX(), getY(), healthBarWidth, barHeight);
        g.shapeRenderer.setColor(Color.WHITE);
        g.shapeRenderer.rect(getX() + healthBarWidth, getY(), healthChangeBarWidth, barHeight);
        g.shapeRenderer.end();
        Util.glDisableTransparent();
        batch.begin();
        var c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        if (font != null) {
            font.setColor(Color.WHITE);
            font.draw(batch, String.format("%d / %d", health, maxHealth),
                getX(), getY() + fontSize, getWidth(), Align.center, false);
            font.draw(batch, text, getX(), getY() + barHeight + fontSize * 1.5f, getWidth(), Align.center, false);
        }
        if (defence > 0) {
            if (defenceFont != null) {
                var offset = getWidth() * DEFENCE_ICON_OFFSET_W_PROPORTION;
                var size = getWidth() * DEFENCE_ICON_W_PROPORTION;
                defenceFont.draw(batch, Integer.toString(defence), getX() - offset - size/2, getY() + defenceFontSize * .7f, size, Align.center, false);
            }
        }
    }

    private float calculateBarWidth(float h) {
        return getWidth() * h / maxHealth;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        genFont();
        healthBarWidth = calculateBarWidth(health);
    }
}
