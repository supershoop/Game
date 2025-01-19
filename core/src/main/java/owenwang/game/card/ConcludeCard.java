package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class ConcludeCard extends Card {
    private static final int BASE_DAMAGE = 15;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        enemies().forEach(e -> e.takeDamage(damage(BASE_DAMAGE)));
        attacked();
        MainGame.INSTANCE.endTurn();
    }

    @Override
    public String art() {
        return Assets.cardConclude;
    }

    @Override
    public String name() {
        return "Conclude";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage to ALL enemies. End your turn.", damageStr(BASE_DAMAGE));
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.8f, 1);
    }

    @Override
    public Card.Type type() {
        return Card.Type.ATTACK;
    }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public boolean targeted() {
        return false;
    }
}
