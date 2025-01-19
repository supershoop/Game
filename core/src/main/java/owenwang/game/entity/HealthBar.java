package owenwang.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import owenwang.game.*;

public class HealthBar extends Group {
    private static final float BAR_HEIGHT_PROPORTION = 0.35f;
    private static final float DEFENCE_BAR_SCALE = 1.05f;
    private static final float DEFENCE_ICON_H_PROPORTION = 0.9f;
    private static final float DEFENCE_ICON_OFFSET_H_PROPORTION = 0.5f;
    private int maxHealth;
    private int health;
    private int defence = 0;

    private final Texture iconDefence;
    private BitmapFont font;
    private int fontSize = 0;
    private BitmapFont defenceFont;
    private int defenceFontSize = 0;
    private static final MainGame g = MainGame.INSTANCE;
    public String text;
    private Image shield = new Image(Assets.texture(Assets.iconDefence2));
    private Label shieldText = new Label("", MainGame.SKIN, "cardTitle");

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

    public HealthBar() {
        iconDefence = Assets.manager.get(Assets.iconDefence);
        addActor(shield);
        addActor(shieldText);
    }

    private void genFont() {
        fontSize = (int) (getHeight() * .3);
        font = Fonts.getBoldFont(fontSize);
        defenceFontSize = (int) (getHeight() * .5);
        defenceFont = Fonts.getBoldFont(defenceFontSize);
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

    private void animateShield() {
        shield.setOrigin(Align.center);
        shieldText.setOrigin(Align.center);
        var act = Actions.sequence(Actions.scaleTo(1.35f, 1.35f, 0.2f, Interpolation.fastSlow),
            Actions.scaleTo(1, 1, 0.2f, Interpolation.fastSlow));
        shield.clearActions();
        shieldText.clear();
        shield.addAction(act);
    }

    public int takeDamage(int damage) {
        clearActions();
        if (damage > 0 && defence > 0) {
            animateShield();
        }
        defence -= damage;
        if (defence < 0) {
            addAction(new HealthChangeAction(health, Math.max(0, health + defence), 0.75f, Interpolation.fastSlow));
            health = Math.max(0, health + defence);
            var d = -defence;
            defence = 0;
            return d;
        } else return 0;
    }

    public void heal(int h) {
        clearActions();
        var initial = health;
        health = Math.min(maxHealth, health + h);
        addAction(new HealthChangeAction(initial, health, 0.75f, Interpolation.linear));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var color = getColor();
        float barHeight = BAR_HEIGHT_PROPORTION * getHeight();
        var c = getColor();

        shield.setVisible(defence > 0);
        shieldText.setVisible(defence > 0);
        if (defence > 0) {
            var w = getWidth() * DEFENCE_BAR_SCALE;
            var h = barHeight + getWidth() * (DEFENCE_BAR_SCALE - 1);
            batch.setColor(Colors.DEFENCE.r * c.r, Colors.DEFENCE.g * c.g, Colors.DEFENCE.b * c.b, c.a * parentAlpha);
            Util.rect(batch, getX() - (w - getWidth()) / 2, getY() - (h - barHeight) / 2, getOriginX(),
                getOriginY(), w, h, getScaleX(), getScaleY(), getRotation());
            var size = getHeight() * DEFENCE_ICON_H_PROPORTION;
            var offset = getHeight() * DEFENCE_ICON_OFFSET_H_PROPORTION;
           // g.shapeRenderer.circle(getX() - offset, getY() + barHeight / 2, size / 2);
            shield.setBounds(-offset, (barHeight - size) / 2, size, size);
            shieldText.setBounds(shield.getX(), shield.getY(), shield.getWidth(), shield.getHeight());
            shieldText.setAlignment(Align.center);
            shieldText.setText(Integer.toString(defence));
        }

        batch.setColor(0, 0, 0, c.a * parentAlpha);
        Util.rect(batch, getX(), getY(), getOriginX(), getOriginY(), getWidth(), barHeight, getScaleX(), getScaleY(), getRotation());
        batch.setColor(c.r, 0, 0, c.a * parentAlpha);
        Util.rect(batch, getX(), getY(), getOriginX(), getOriginY(), healthBarWidth, barHeight, getScaleX(), getScaleY(), getRotation());

        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        Util.rect(batch, getX() + healthBarWidth, getY(), getOriginX(), getOriginY(), healthChangeBarWidth, barHeight, getScaleX(), getScaleY(), getRotation());
        if (font != null) {
            font.setColor(Color.WHITE);
            font.draw(batch, String.format("%d / %d", health, maxHealth),
                getX(), getY() + fontSize, getWidth(), Align.center, false);
            font.draw(batch, text, getX(), getY() + barHeight + fontSize * 1.5f, getWidth(), Align.center, false);
        }
        super.draw(batch, parentAlpha);
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
