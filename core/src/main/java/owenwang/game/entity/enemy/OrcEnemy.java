package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import owenwang.game.Assets;
import owenwang.game.EntityState;
import owenwang.game.MainGame;
import owenwang.game.Util;

import java.util.function.Consumer;

public class OrcEnemy extends Enemy {
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> hurt;
    private final Animation<TextureRegion> death;
    private final Animation<TextureRegion> swing;
    public OrcEnemy(MainGame g) {
        super(g, "Orc");
        healthBar.setMaxHealth(25);
        healthBar.setHealth(25);
        idle = new Animation<>(0.18f,
            TextureRegion.split(Assets.manager.get(Assets.orcIdle), 100, 100)[0]);
        hurt = new Animation<>(0.1f,
            TextureRegion.split(Assets.manager.get(Assets.orcHurt), 100, 100)[0]);
        death = new Animation<>(0.1f,
            TextureRegion.split(Assets.manager.get(Assets.orcDeath), 100, 100)[0]);
        swing = new Animation<>(0.1f,
            TextureRegion.split(Assets.manager.get(Assets.orcSwing), 100, 100)[0]);
    }

    @Override
    protected TextureRegion getTexture() {
        if (state == EntityState.HURT && hurt.isAnimationFinished(stateTime)) state = EntityState.IDLE;
        if (state == EntityState.SWING && swing.isAnimationFinished(stateTime)) state = EntityState.IDLE;

        return switch (state) {
            case DIE -> death.getKeyFrame(stateTime);
            case HURT -> hurt.getKeyFrame(stateTime);
            case SWING -> swing.getKeyFrame(stateTime);
            default -> idle.getKeyFrame(stateTime, true);
        };
    }

    @Override
    protected void modifyTextures(Consumer<TextureRegion> f) {
        for (var t : idle.getKeyFrames()) f.accept(t);
        for (var t : hurt.getKeyFrames()) f.accept(t);
        for (var t : death.getKeyFrames()) f.accept(t);
        for (var t : swing.getKeyFrames()) f.accept(t);
    }

    @Override
    public void playTurn() {
        switch (intent) {
            case ATTACK -> attack(0.2f, EntityState.SWING);
            case DEFEND -> defend(0, EntityState.IDLE);
        }
        super.playTurn();
    }

    @Override
    public float turnLength() {
        return switch (intent) {
            case ATTACK -> 0.5f;
            case DEFEND -> 0.2f;
        };
    }

    @Override
    public void decideIntent(int turn) {
        turn /= 2;
        if (turn % 2 == 0) {
            intent = Intent.ATTACK;
            intentValue = Util.RANDOM.nextInt(3, 5);
        } else {
            intent = Intent.DEFEND;
            intentValue = Util.RANDOM.nextInt(3, 5);
        }
    }
}
