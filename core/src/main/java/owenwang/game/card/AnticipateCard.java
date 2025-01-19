package owenwang.game.card;

import com.badlogic.gdx.math.Vector2;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class AnticipateCard extends Card {
    private static final int DEFENCE = 10;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        if (target.intent != Enemy.Intent.ATTACK) return;
        getPlayer().healthBar.addDefence(DEFENCE);
    }

    @Override
    public String art() {
        return Assets.cardAnticipate;
    }

    @Override
    public String name() {
        return "Anticipate";
    }

    @Override
    public String description() {
        return String.format("If your opponent intends to attack, gain %d defence.", DEFENCE);
    }

    @Override
    public Type type() {
        return Type.ABILITY;
    }

    @Override
    public boolean targeted() {
        return true;
    }

    @Override
    public Vector2 titleFontScale() {
        return new Vector2(0.8f, 1f);
    }

    @Override
    public int cost() {
        return 0;
    }
}
