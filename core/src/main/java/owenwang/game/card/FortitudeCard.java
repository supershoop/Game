package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.Colors;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class FortitudeCard extends Card {
    @Override
    public void play(Enemy target) {
        getPlayer().setFortitude(true);
    }

    @Override
    public String art() {
        return Assets.cardFortitude;
    }

    @Override
    public String name() {
        return "Fortitude";
    }

    @Override
    public String description() {
        return "You no longer lose your defence each turn.\n[LIGHT_GRAY]Exhaust.";
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.75f, 1);
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
    public int cost() {
        return 3;
    }
}
