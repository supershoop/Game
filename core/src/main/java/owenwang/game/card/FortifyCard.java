package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class FortifyCard extends Card {
    private static final int BASE_VALUE = 25;

    @Override
    public void play(Enemy target) {
        getPlayer().healthBar.addDefence(BASE_VALUE);
    }

    @Override
    public String art() {
        return Assets.cardFortify;
    }

    @Override
    public String name() {
        return "Fortify";
    }

    @Override
    public String description() {
        return String.format("Gain %d defence.", BASE_VALUE);
    }

    @Override
    public Card.Type type() {
        return Card.Type.ABILITY;
    }

    @Override
    public int cost() {
        return 2;
    }
}
