package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import owenwang.game.*;

import java.util.function.Consumer;

public class OrcEnemy extends Enemy {
    private final Animation<TextureRegion> swing;
    public OrcEnemy() {
        super("Orc");
        healthBar.setMaxHealth(Util.RANDOM.nextInt(20, 28));
        healthBar.setHealth(healthBar.getMaxHealth());
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
    protected void modifyTextures(Consumer<TextureRegion> f) {
        for (var t : idle.getKeyFrames()) f.accept(t);
        for (var t : hurt.getKeyFrames()) f.accept(t);
        for (var t : death.getKeyFrames()) f.accept(t);
        for (var t : swing.getKeyFrames()) f.accept(t);
    }

    @Override
    public void playTurn() {
        switch (intent) {
            case ATTACK:
                attack(0.2f);
                playAnimation(swing);
                break;
            case DEFEND: defend(0); break;
            case STUN: stunned(); break;
        }
        super.playTurn();
    }

    @Override
    public float postTurn() {
        switch (intent) {
            case ATTACK: return swing.getAnimationDuration();
            case DEFEND: return 0.2f;
            default: return Timings.STUN_TIME;
        }
    }

    @Override
    public void decideIntent() {
        if (turn % 2 == 0) {
            intent = Intent.ATTACK;
            intentValue = Util.RANDOM.nextInt(4, 9);
        } else {
            intent = Intent.DEFEND;
            intentValue = Util.RANDOM.nextInt(4, 7);
        }
    }
}
