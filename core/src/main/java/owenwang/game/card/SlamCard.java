package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class SlamCard extends Card {
    private static final int BASE_VALUE = 5;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.takeDamage(damage(BASE_VALUE));
        getPlayer().healthBar.addDefence(BASE_VALUE);
        attacked();
    }

    @Override
    public String art() {
        return Assets.cardSlam;
    }

    @Override
    public String name() {
        return "Slam";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage.\nGain %d defence.", damageStr(BASE_VALUE), BASE_VALUE);
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
