package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class WhirlCard extends Card {
    private static final int BASE_DAMAGE = 7;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        MainGame.INSTANCE.enemies.enemies.forEach(e -> e.takeDamage(damage(BASE_DAMAGE)));
        attacked();
    }

    @Override
    public String art() {
        return Assets.cardWhirl;
    }

    @Override
    public String name() {
        return "Whirl";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage to ALL enemies.", damageStr(BASE_DAMAGE));
    }

    @Override
    public Type type() {
        return Type.ATTACK;
    }

    @Override
    public boolean targeted() {
        return false;
    }

    @Override
    public int cost() {
        return 2;
    }
}
