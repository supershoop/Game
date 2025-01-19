package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.entity.enemy.Enemy;

public class ThousandCutsCard extends Card {
    private static final int BASE_DAMAGE = 4;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.takeDamage(damage(BASE_DAMAGE));
        getPlayer().swing();
        MainGame.INSTANCE.addToDiscard(this);
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.5f, 1f);
    }

    @Override
    public String art() {
        return Assets.cardThousandCuts;
    }

    @Override
    public String name() {
        return "A Thousand Cuts";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage.\nAdd a copy of this card to your discard pile.", damageStr(BASE_DAMAGE));
    }

    @Override
    public Type type() {
        return Type.ATTACK;
    }

    @Override
    public int cost() {
        return 0;
    }
}
