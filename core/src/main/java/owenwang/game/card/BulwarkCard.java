package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class BulwarkCard extends Card {
    @Override
    public void play(Enemy target) {
        getPlayer().setBulwark(true);
    }

    @Override
    public String art() {
        return Assets.cardBulwark;
    }

    @Override
    public String name() {
        return "Bulwark";
    }

    @Override
    public String description() {
        return "The next time you take damage, reduce it to 1.\n [LIGHT_GRAY]Exhaust.";
    }

    @Override
    public Type type() {
        return Type.ABILITY;
    }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public PlayResult result() {
        return PlayResult.EXHAUST;
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.9f, 1);
    }
}
