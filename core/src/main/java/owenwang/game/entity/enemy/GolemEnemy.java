package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.Util;
import owenwang.game.card.ShatteredCard;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static owenwang.game.Assets.limit;

public class GolemEnemy extends Enemy {
    private final Animation<TextureRegion> unarmoredIdle;
    private final Animation<TextureRegion> armoredIdle;
    private final Animation<TextureRegion> breakArmor;
    private final Animation<TextureRegion> transform;
    private final Animation<TextureRegion> attackA;
    private final Animation<TextureRegion> attackB;
    private final Animation<TextureRegion> attackC;
    private final Animation<TextureRegion> defend;
    public GolemEnemy() {
        super("The Golem");
        healthBar.setMaxHealth(50);
        healthBar.setHealth(50);
        var textures = TextureRegion.split(Assets.texture(Assets.golem), 64, 64);
        unarmoredIdle = new Animation<>(0.2f, limit(4, textures[0]));
        armoredIdle = new Animation<>(0.2f, limit(4, textures[12]));
        death = new Animation<>(0.1f, limit(4, textures[11]));
        breakArmor = new Animation<>(0.1f, limit(5, textures[19]));
        transform = new Animation<>(0.15f, limit(11, textures[5]));
        attackA = new Animation<>(0.12f, limit(11, textures[14]));
        attackB = new Animation<>(0.1f, limit(8, textures[15]));
        attackC = new Animation<>(0.1f, limit(10, textures[16]));
        defend = new Animation<>(0.1f, limit(11, textures[17]));

        idle = unarmoredIdle;
        redOnDamage = true;
    }
    @Override
    protected float bottomInset() {
        return 0.2f;
    }

    @Override
    protected float inset() {
        return 0.05f;
    }

    @Override
    protected float getHeightProportion() {
        return 0.9f;
    }

    @Override
    protected void modifyTextures(Consumer<TextureRegion> f) {
        Stream.of(unarmoredIdle.getKeyFrames()).forEach(f);
        Stream.of(armoredIdle.getKeyFrames()).forEach(f);
        Stream.of(death.getKeyFrames()).forEach(f);
        Stream.of(breakArmor.getKeyFrames()).forEach(f);
        Stream.of(transform.getKeyFrames()).forEach(f);
        Stream.of(attackA.getKeyFrames()).forEach(f);
        Stream.of(attackB.getKeyFrames()).forEach(f);
        Stream.of(attackC.getKeyFrames()).forEach(f);
        Stream.of(defend.getKeyFrames()).forEach(f);
    }

    @Override
    public void playTurn() {
        var anim = getAnimation();
        switch (intent) {
            case STUN: stunned(); break;
            case UNKNOWN:
                playAnimation(transform);
                idle = armoredIdle;
                actionLabel("Transform");
                turn(transform.getAnimationDuration(), Actions.run(() -> {
                    healthBar.addDefence(40);
                    healthBar.setMaxHealth(120);
                    healthBar.heal(120 - 50);
                }));
                break;
            case DEFEND:
                defend(anim == null ? 0.2f : anim.getAnimationDuration() * 0.8f, anim == defend ? "Reinforce" : "Armor");
                if (anim != null) playAnimation(anim);
                break;
            case ATTACK:
                attack(anim.getAnimationDuration() * 0.8f, anim == attackA ? "Shatter Earth" : "Volley");
                playAnimation(anim);
                break;
            case CAST:
                playAnimation(anim);
                MainGame.INSTANCE.queuedOperations++;
                turn(attackC.getAnimationDuration() * 0.8f, Actions.run(() -> {
                    MainGame.INSTANCE.queuedOperations--;
                    MainGame.INSTANCE.addToDiscard(new ShatteredCard());
                }));
                actionLabel("Shatter Blade");
                break;
        }
        super.playTurn();
    }

    @Override
    public void die() {
        if (idle == unarmoredIdle) super.die();
        else {
            float pause = 0f;
            _die(breakArmor.getAnimationDuration() + pause, death.getAnimationDuration());
            death.setPlayMode(Animation.PlayMode.NORMAL);
            breakArmor.setPlayMode(Animation.PlayMode.NORMAL);
            playAnimation(breakArmor);
            addAction(Actions.delay(breakArmor.getAnimationDuration() + pause, Actions.run(() -> {
                playAnimation(death);
            })));
            returnToIdle = false;
        }
    }

    private Animation<TextureRegion> getAnimation() {
        if (intent == Intent.ATTACK) return (intentValue >= 40 ? attackA : attackC);
        if (intent == Intent.DEFEND && intentValue >= 37) return defend;
        if (intent == Intent.CAST) return attackB;
        return idle;
    }

    @Override
    public float postTurn() {
        var anim = getAnimation();
        switch (intent) {
            case UNKNOWN: return transform.getAnimationDuration();
            case DEFEND: return anim != null ? anim.getAnimationDuration() : 0.2f;
            case ATTACK: return anim.getAnimationDuration();
            case CAST: return Timings.ADD_TO_DISCARD_TIME + anim.getAnimationDuration() * 0.5f;
            default: return Timings.STUN_TIME;
        }
    }

    @Override
    public void decideIntent() {
        if (turn == 0) {
            intent = Intent.UNKNOWN;
        } else if (turn % 7 == 0) {
          intent = Intent.ATTACK;
          intentValue = Util.RANDOM.nextInt(40, 65);
        } else {
            var rand = Util.RANDOM.nextInt(7);
            if (healthBar.getDefence() == 0 && rand == 0) {
                intent = Intent.DEFEND;
                intentValue = Util.RANDOM.nextInt(37, 53);
            }
            else if (healthBar.getDefence() == 0 && rand < 3) {
                intent = Intent.DEFEND;
                intentValue = Util.RANDOM.nextInt(10, 17);
            } else if (rand < 5) {
                intent = Intent.ATTACK;
                intentValue = Util.RANDOM.nextInt(15, 23);
            }
            else {
                intent = Intent.CAST;
            }
        }
    }

    @Override
    protected Rectangle getBounds() {
        var size = getHeight() * 0.4f;
        var margin = (getHeight()  - size) / 2;
        return new Rectangle(margin, margin * 1.4f, size, size);
    }
}
