package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class SeverSoulCard extends Card {
    private static final int THRESHOLD = 30;
    @Override
    public void begin(Enemy target) {
        super.begin(target);
        if (target.healthBar.getHealth() < 30) {
            target.takeDamage(target.healthBar.getDefence() + target.healthBar.getHealth());
        }
        attacked();
    }

    @Override
    public String art() {
        return Assets.cardSeverSoul;
    }

    @Override
    public String name() {
        return "Sever Soul";
    }

    @Override
    public String description() {
        return String.format("If your enemy has less than %d health, set their health to 0.", THRESHOLD);
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.7f, 1);
    }

    @Override
    public Type type() {
        return Type.ATTACK;
    }

    @Override
    public int cost() {
        return 1;
    }
}
