package owenwang.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import owenwang.game.Assets;
import owenwang.game.EntityState;
import owenwang.game.MainGame;
import owenwang.game.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Player extends Entity {
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> swing;
    private final Animation<TextureRegion> hurt;
    public int energy = 3;
    public List<Card> deck = new ArrayList<>();

    public Player(MainGame g) {
        super(g);
        healthBar.setMaxHealth(20);
        healthBar.setHealth(20);
        idle = new Animation<>(0.3f, TextureRegion.split(Assets.manager.get(Assets.playerIdle), 100, 100)[0]);
        swing = new Animation<>(0.1f, TextureRegion.split(Assets.manager.get(Assets.playerSwing), 100, 100)[0]);
        hurt = new Animation<>(0.1f, TextureRegion.split(Assets.manager.get(Assets.playerHurt), 100, 100)[0]);
    }

    @Override
    public void die() {

    }

    @Override
    protected TextureRegion getTexture() {
        if (state == EntityState.SWING && swing.isAnimationFinished(stateTime)) state = EntityState.IDLE;
        if (state == EntityState.HURT && swing.isAnimationFinished(stateTime)) state = EntityState.IDLE;
        return switch (state) {
            case SWING -> swing.getKeyFrame(stateTime);
            case HURT -> hurt.getKeyFrame(stateTime);
            default -> idle.getKeyFrame(stateTime, true);
        };
    }

    @Override
    protected void modifyTextures(Consumer<TextureRegion> f) {
        for (var t : idle.getKeyFrames()) f.accept(t);
        for (var t : swing.getKeyFrames()) f.accept(t);
        for (var t : hurt.getKeyFrames()) f.accept(t);
    }

    public void swing() {
        state = EntityState.SWING;
        stateTime = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    protected float getHeightProportion() {
        return 0.63f;
    }
}
