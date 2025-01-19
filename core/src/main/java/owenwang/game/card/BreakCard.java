package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class BreakCard extends Card {
    private static final int BASE_DAMAGE = 4;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.healthBar.setDefence(0);
        target.takeDamage(BASE_DAMAGE);
        getPlayer().swing();
    }

    @Override
    public String art() {
        return Assets.cardBreakThrough;
    }

    @Override
    public String name() {
        return "Break";
    }

    @Override
    public String description() {
        return String.format("Remove all defence.\nDeal %d damage.", BASE_DAMAGE);
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
