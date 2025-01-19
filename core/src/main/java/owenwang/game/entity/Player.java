package owenwang.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.Util;
import owenwang.game.card.Card;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Player extends Entity {
    private final Animation<TextureRegion> swing;
    public int energy = 3;
    public List<Card> deck = new ArrayList<>();
    private boolean strength = false;
    private boolean fortitude = false;
    private boolean bulwark = false;

    private TooltipManager manager = new TooltipManager();
    private Table status = new Table();

    public Player() {
        idle = new Animation<>(0.3f, TextureRegion.split(Assets.manager.get(Assets.playerIdle), 100, 100)[0]);
        swing = new Animation<>(0.1f, TextureRegion.split(Assets.manager.get(Assets.playerSwing), 100, 100)[0]);
        hurt = new Animation<>(0.1f, TextureRegion.split(Assets.manager.get(Assets.playerHurt), 100, 100)[0]);
        death = new Animation<>(0.15f, TextureRegion.split(Assets.manager.get(Assets.playerDeath), 100, 100)[0]);
        status.align(Align.left);
        addActor(status);
        manager.initialTime = Timings.INITIAL_TOOLTIP_TIME;
        manager.resetTime = 0;
    }

    public void updateStatus() {
        status.clear();
        manager.hideAll();
        var size = getHeight() / 40;
        var pad = size / 4;
        if (strength) {
            status.add(new Image(Assets.texture(Assets.statusStrength)))
                .width(size).height(size).padRight(pad).getActor()
                .addListener(new TextTooltip(
                    "[ATTACK]Strength:[BLACK] Your next attack will deal double damage.", manager, MainGame.SKIN
                ));
        }
        if (bulwark) {
            status.add(new Image(Assets.texture(Assets.statusBulwark)))
                .width(size).height(size).padRight(pad).getActor()
                .addListener(new TextTooltip(
                    "[ABILITY]Bulwark:[BLACK] The next time you take damage, reduced it to 1.", manager, MainGame.SKIN
                ));

        }
        if (fortitude) {
            status.add(new Image(Assets.texture(Assets.statusFortitude)))
                .width(size).height(size).padRight(pad).getActor()
                .addListener(new TextTooltip(
                    "[#b66a0c]Fortitude:[BLACK] Your defence is retained each turn.", manager, MainGame.SKIN
                ));
        }
    }

    @Override
    public void die() {
        MainGame.INSTANCE.endGame();
        playAnimation(death);
        returnToIdle = false;
    }

    @Override
    protected void modifyTextures(Consumer<TextureRegion> f) {
        for (var t : idle.getKeyFrames()) f.accept(t);
        for (var t : swing.getKeyFrames()) f.accept(t);
        for (var t : hurt.getKeyFrames()) f.accept(t);
    }

    public void swing() {
        playAnimation(swing);
    }

    @Override
    protected float getHeightProportion() {
        return 0.63f;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        status.setBounds(healthBar.getX(), healthBar.getY() + healthBar.getHeight(), healthBar.getWidth(), status.getHeight());
        manager.maxWidth = healthBar.getWidth();

    }

    @Override
    public int takeDamage(int damage) {
        if (bulwark && damage > healthBar.getDefence()) {
            damage = healthBar.getDefence() + 1;
            setBulwark(false);
        }
        return super.takeDamage(damage);
    }

    public void beginTurn() {
        energy = 3;
        if (!fortitude) healthBar.setDefence(0);
    }

    public void beginCombat() {
        strength = false;
        fortitude = false;
        bulwark = false;
        updateStatus();
    }

    public boolean isBulwark() {
        return bulwark;
    }

    public void setBulwark(boolean bulwark) {
        var old = this.bulwark;
        this.bulwark = bulwark;
        if (old != bulwark) updateStatus();
    }

    public boolean isFortitude() {
        return fortitude;
    }

    public void setFortitude(boolean fortitude) {
        var old = this.fortitude;
        this.fortitude = fortitude;
        if (old != fortitude) updateStatus();
    }

    public boolean isStrength() {
        return strength;
    }

    public void setStrength(boolean strength) {
        var old = this.strength;
        this.strength = strength;
        if (old != strength) updateStatus();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var c = getColor();
        batch.setColor(0, 0, 0, c.a * parentAlpha);
        for (var cell : status.getCells()) {
            float borderSize = 2;
            Util.rect(batch, getX() + status.getX() + cell.getActorX() - borderSize / 2, getY() + status.getY() + cell.getActorY() - borderSize / 2,
                getOriginX(), getOriginY(),
                cell.getActorWidth() + borderSize, cell.getActorHeight() + borderSize, getScaleX(),
                getScaleY(), getRotation());
        }
        super.draw(batch, parentAlpha);
    }

    public void initialState() {
        healthBar.setMaxHealth(75);
        healthBar.setHealth(75);
        healthBar.setDefence(0);
        returnToIdle = true;
    }
}
