package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class CriticalStrikeCard extends Card {
    private static final int BASE_DAMAGE = 30;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.takeDamage(Gdx.input.isKeyPressed(Input.Keys.GRAVE) ? 1000 : damage(BASE_DAMAGE));
        attacked();
    }

    @Override
    public String art() {
        return Assets.cardCriticalStrike;
    }

    @Override
    public String name() {
        return "Critical Strike";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage.", damageStr(BASE_DAMAGE));
    }

    @Override
    public Type type() {
        return Type.ATTACK;
    }


    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.6f, 1);
    }

    @Override
    public int cost() {
        return 3;
    }
}
