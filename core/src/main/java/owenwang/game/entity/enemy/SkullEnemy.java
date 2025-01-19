package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import owenwang.game.Assets;
import owenwang.game.Timings;
import owenwang.game.Util;

import java.util.Arrays;
import java.util.function.Consumer;

public class SkullEnemy extends Enemy {
    private final Animation<TextureRegion> attack;

    public SkullEnemy() {
        super("Skull");
        healthBar.setMaxHealth(Util.RANDOM.nextInt(20, 26));
        healthBar.setHealth(healthBar.getMaxHealth());
        var textures = Assets.linearSplit(Assets.texture(Assets.skull), 5, 6);
        idle = new Animation<>(0.15f, Arrays.copyOfRange(textures, 0, 4));
        attack = new Animation<>(0.1f, Arrays.copyOfRange(textures, 4, 12));
        death = new Animation<>(0.1f, Arrays.copyOfRange(textures, 16, 26));
        hurt = new Animation<>(0.1f, Arrays.copyOfRange(textures, 12, 16));
        redOnDamage = true;
    }

    @Override
    public float postTurn() {
        switch (intent) {
            case DEFEND: return 0.5f;
            case ATTACK: return attack.getAnimationDuration();
            default: return Timings.STUN_TIME;
        }
    }

    @Override
    public void decideIntent() {
        if (turn % 2 == 0) {
            intent = Intent.DEFEND;
            intentValue = Util.RANDOM.nextInt(5, 11);
        } else {
            intent = Intent.ATTACK;
            intentValue = turn % 4 == 3 ? Util.RANDOM.nextInt(12, 19) : Util.RANDOM.nextInt(4, 9);
        }
    }

    @Override
    public void playTurn() {
        if (intent == Intent.STUN) {
            stunned();
        }
        else if (intent == Intent.ATTACK) {
            attack(0, "Crunch");
            playAnimation(attack);
        } else {
            defend(0, "Harden");
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
        Arrays.stream(attack.getKeyFrames()).forEach(f);
        Arrays.stream(hurt.getKeyFrames()).forEach(f);
    }
}
