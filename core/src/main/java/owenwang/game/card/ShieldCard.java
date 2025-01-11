package owenwang.game.card;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.Assets;
import owenwang.game.Constants;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class ShieldCard extends AbilityCard {
    private static final int BASE_VALUE = 3;

    public ShieldCard(MainGame g) {
        super(g);
    }

    @Override
    public void play(Enemy target) {
        getPlayer().healthBar.addDefence(BASE_VALUE);
    }

    @Override
    public String getArt() {
        return Assets.cardShield;
    }

    @Override
    public String getName() {
        return "Shield";
    }

    @Override
    public String getDescription() {
        return String.format("Gain %d defence.", BASE_VALUE);
    }

    @Override
    public int cost() {
        return 1;
    }
}
