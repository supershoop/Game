package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import owenwang.game.Assets;
import owenwang.game.Timings;
import owenwang.game.Util;

import java.util.Arrays;
import java.util.function.Consumer;

public class CrabEnemy extends Enemy {
    private final Animation<TextureRegion> attack;
    private final Animation<TextureRegion> ability;

    public CrabEnemy() {
        super("Crab");
        healthBar.setMaxHealth(Util.RANDOM.nextInt(12, 18));
        healthBar.setHealth(healthBar.getMaxHealth());
        var textures = TextureRegion.split(Assets.texture(Assets.crab), 64, 64);
        idle = new Animation<>(0.15f, Assets.limit(4, textures[0]));
        death = new Animation<>(0.15f, Assets.limit(5, textures[3]));
        attack = new Animation<>(0.1f, Assets.limit(6, textures[1]));
        ability = new Animation<>(0.1f, textures[2]);
        hurt = new Animation<>(0.1f, Assets.limit(3, textures[4]));
        redOnDamage = true;
    }

    @Override
    public float postTurn() {
        switch (intent) {
            case DEFEND: return ability.getAnimationDuration();
            case ATTACK: return attack.getAnimationDuration();
            default: return Timings.STUN_TIME;
        }
    }

    @Override
    public void decideIntent() {
        if (Util.RANDOM.nextInt(3) == 0) {
            intent = Intent.DEFEND;
            intentValue = Util.RANDOM.nextInt(5, 8);
        } else {
            intent = Intent.ATTACK;
            intentValue = Util.RANDOM.nextInt(3, 13);
        }
    }

    @Override
    public void playTurn() {
        if (intent == Intent.STUN) {
            stunned();
        }
        else if (intent == Intent.ATTACK) {
            attack(0, "Pincer Strike");
            playAnimation(attack);
        } else {
            defend(0, "Harden Shell");
        }
        super.playTurn();
    }

    @Override
    protected float inset() {
        return 0.4f;
    }

    @Override
    protected float getHeightProportion() {
        return 0.6f;
    }

    @Override
    protected void modifyTextures(Consumer<TextureRegion> f) {
        Arrays.stream(idle.getKeyFrames()).forEach(f);
        Arrays.stream(death.getKeyFrames()).forEach(f);
        Arrays.stream(ability.getKeyFrames()).forEach(f);
        Arrays.stream(attack.getKeyFrames()).forEach(f);
        Arrays.stream(hurt.getKeyFrames()).forEach(f);
    }
}
