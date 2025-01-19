package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class WalkItOffCard extends Card {
    private static final int BASE_VALUE = 10;

    @Override
    public void play(Enemy target) {
        getPlayer().healthBar.heal(BASE_VALUE);
    }

    @Override
    public String art() {
        return Assets.cardWalkItOff;
    }

    @Override
    public String name() {
        return "Red Stew";
    }

    @Override
    public String description() {
        return String.format("Heal %d.\n[LIGHT_GRAY]Exhaust.", BASE_VALUE);
    }

    @Override
    public int cost() {
        return 1;
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.8f, 1f);
    }


    @Override
    public Type type() {
        return Type.ABILITY;
    }

    @Override
    public PlayResult result() {
        return PlayResult.EXHAUST;
    }
}
