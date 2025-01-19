package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.Util;
import owenwang.game.card.SlimeCard;

import java.util.Arrays;
import java.util.function.Consumer;

public class SlimeEnemy extends Enemy {
    private final Animation<TextureRegion> attack;
    private final Animation<TextureRegion> ability;

    public SlimeEnemy() {
        super("Slime");
        healthBar.setMaxHealth(Util.RANDOM.nextInt(10, 16));
        healthBar.setHealth(healthBar.getMaxHealth());
        var textures = TextureRegion.split(Assets.texture(Assets.slime), 64, 64);
        idle = new Animation<>(0.15f, Assets.limit(4, textures[0]));
        death = new Animation<>(0.15f, Assets.limit(6, textures[4]));
        attack = new Animation<>(0.1f, Assets.limit(6, textures[1]));
        ability = new Animation<>(0.2f, Assets.limit(4, textures[2]));
        hurt = new Animation<>(0.1f, Assets.limit(4, textures[5]));
        redOnDamage = true;
    }

    @Override
    public float postTurn() {
        switch (intent) {
            case CAST: return Timings.ADD_TO_DISCARD_TIME;
            case ATTACK: return attack.getAnimationDuration();
            default: return Timings.STUN_TIME;
        }
    }

    @Override
    public void decideIntent() {
        if (turn % 2 == 0) {
            intent = Intent.CAST;
        } else {
            intent = Intent.ATTACK;
            intentValue = Util.RANDOM.nextInt(2, 7);
        }
    }

    @Override
    public void playTurn() {
        if (intent == Intent.STUN) {
            stunned();
        }else  if (intent == Intent.ATTACK) {
            attack(0);
            playAnimation(attack);
        } else {
            MainGame.INSTANCE.addToDiscard(new SlimeCard());
            playAnimation(ability);
            actionLabel("Entrap");
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
