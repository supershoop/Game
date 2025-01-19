package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class SlashCard extends Card {
    private static final int BASE_DAMAGE = 5;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.takeDamage(Gdx.input.isKeyPressed(Input.Keys.GRAVE) ? 1000 : damage(BASE_DAMAGE));
        attacked();
    }

    @Override
    public String art() {
        return Assets.cardSlash;
    }

    @Override
    public String name() {
        return "Slash";
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
    public int cost() {
        return 1;
    }
}
