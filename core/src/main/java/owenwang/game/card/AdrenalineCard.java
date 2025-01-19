package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.Colors;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class AdrenalineCard extends Card {
    @Override
    public void play(Enemy target) {
        getPlayer().energy += 2;
    }

    @Override
    public String art() {
        return Assets.cardAdrenaline;
    }

    @Override
    public String name() {
        return "Adrenaline";
    }

    @Override
    public String description() {
        return "Gain 2 energy.\n[LIGHT_GRAY]Exhaust.";
    }

    @Override
    public Type type() {
        return Type.ABILITY;
    }

    @Override
    public PlayResult result() {
        return PlayResult.EXHAUST;
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.7f, 1);
    }

    @Override
    public int cost() {
        return 0;
    }
}
