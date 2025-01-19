package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class StrengthCard extends Card {
    @Override
    public void play(Enemy target) {
        getPlayer().setStrength(true);
    }

    @Override
    public String art() {
        return Assets.cardStrength;
    }

    @Override
    public String name() {
        return "Strength";
    }

    @Override
    public String description() {
        return "This combat, your next attack deals double damage.";
    }

    @Override
    public Type type() {
        return Type.ABILITY;
    }

    @Override
    public int cost() {
        return 1;
    }
}
