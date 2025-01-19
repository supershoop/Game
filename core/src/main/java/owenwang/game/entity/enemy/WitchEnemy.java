package owenwang.game.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.Util;

import java.util.function.Consumer;

import static owenwang.game.Util.getFirst;
import static owenwang.game.Util.getLast;

public class WitchEnemy extends Enemy {
    private final float SUMMON_TIME = 0.75f;
    private final Animation<TextureRegion> cast;
    public WitchEnemy() {
        super("Witch Doctor");
        healthBar.setMaxHealth(45);
        healthBar.setHealth(45);
        var textures = TextureRegion.split(Assets.texture(Assets.witch), 128, 128);
        idle = new Animation<>(0.3f, Assets.limit(4, textures[1]));
        cast = new Animation<>(0.15f, Assets.limit(6, textures[0]));
        redOnDamage = true;
    }

    @Override
    protected float inset() {
        return 0.15f;
    }

    @Override
    protected float getHeightProportion() {
        return 0.6f;
    }

    @Override
    protected void modifyTextures(Consumer<TextureRegion> f) {
        for (var t : idle.getKeyFrames()) f.accept(t);
        for (var t : cast.getKeyFrames()) f.accept(t);
    }

    @Override
    public void playTurn() {
        switch (intent) {
            case STUN: stunned(); break;
            case UNKNOWN:
                addAction(Actions.delay(cast.getAnimationDuration(), Actions.run(() -> {
                    var enemies = MainGame.INSTANCE.enemies;
                    if (!(getLast(enemies.enemies) instanceof SkullEnemy)) {
                        var s = new SkullEnemy();
                        enemies.addEnemy(s);
                    }
                    if (!(getFirst(enemies.enemies) instanceof SkullEnemy)) {
                        var s = new SkullEnemy();
                        enemies.processEnemy(s);
                        enemies.enemies.add(0, s);

                    }
                    enemies.update(0);
                })));
                playAnimation(cast);
                actionLabel("Summon");
                break;
            case HEAL:
                MainGame.INSTANCE.enemies.enemies.forEach(e -> {
                    e.healthBar.heal((int) (e.healthBar.getMaxHealth() * .2f));
                });
                playAnimation(cast);
                actionLabel("Heal");
                break;
            case ATTACK:
                attack(0, "Curse");
                playAnimation(cast);
                break;
            case DEFEND: defend(0, "Dispel"); break;
        }
        super.playTurn();
    }

    @Override
    public float postTurn() {
        switch (intent) {
            case CAST: return cast.getAnimationDuration();
            case ATTACK: return cast.getAnimationDuration();
            case DEFEND: return 0.2f;
            case UNKNOWN: return Math.max(SUMMON_TIME, cast.getAnimationDuration()) + 0.3f;
            default: return Timings.STUN_TIME;
        }
    }

    @Override
    public void decideIntent() {
        var rand = Util.RANDOM.nextInt(3);
        var enemies = MainGame.INSTANCE.enemies.enemies;
        if (turn == 0 ||
            (((!(getFirst(enemies) instanceof SkullEnemy)) || !(getLast(enemies) instanceof SkullEnemy)) && rand == 0)) {
            intent = Intent.UNKNOWN;
        } else {

            switch (turn % 3) {
                case 2:
                    intent = Intent.DEFEND;
                    intentValue = Util.RANDOM.nextInt(4, 5);
                    break;
                case 0:
                    intent = Intent.HEAL;
                    intentValue = -1;
                    break;
                case 1:
                    intent = Intent.ATTACK;
                    intentValue = Util.RANDOM.nextInt(2, 6);
                    break;
            }
        }

    }
}
