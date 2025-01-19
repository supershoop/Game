package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;

public class ShatteredCard extends Card {
    @Override
    public String art() {
        return Assets.cardShattered;
    }

    @Override
    public String name() {
        return "Shattered";
    }

    @Override
    public String description() {
        return "[LIGHT_GRAY]Unplayable.";
    }

    @Override
    public int cost() {
        return 1;
    }

    @Override
    public Type type() {
        return Type.CURSE;
    }

    @Override
    public PlayResult result() {
        return PlayResult.UNPLAYABLE;
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.8f, 1);
    }
}
