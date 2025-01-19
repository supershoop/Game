package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class ShieldCard extends Card {
    private static final int BASE_VALUE = 6;

    @Override
    public void play(Enemy target) {
        getPlayer().healthBar.addDefence(BASE_VALUE);
    }

    @Override
    public String art() {
        return Assets.cardShield;
    }

    @Override
    public String name() {
        return "Shield";
    }

    @Override
    public String description() {
        return String.format("Gain %d defence.", BASE_VALUE);
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
