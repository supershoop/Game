package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class ShieldCard2 extends AbilityCard {
    private static final int BASE_VALUE = 3;

    public ShieldCard2(MainGame g) {
        super(g);
    }

    @Override
    public void play(Enemy target) {
        getPlayer().healthBar.addDefence(BASE_VALUE);
        g.drawCard();
    }

    @Override
    public String getArt() {
        return Assets.cardShield;
    }

    @Override
    public String getName() {
        return "Shield+";
    }

    @Override
    public String getDescription() {
        return String.format("Gain %d defence. Draw one card.", BASE_VALUE);
    }

    @Override
    public int cost() {
        return 1;
    }
}
