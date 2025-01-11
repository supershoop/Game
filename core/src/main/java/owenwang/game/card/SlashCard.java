package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class SlashCard extends AttackCard {
    private static final int BASE_DAMAGE = 3;

    public SlashCard(MainGame g) {
        super(g);
    }

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.takeDamage(BASE_DAMAGE);
        getPlayer().swing();
    }

    @Override
    public String getArt() {
        return Assets.cardSlash;
    }

    @Override
    public String getName() {
        return "Slash";
    }

    @Override
    public String getDescription() {
        return String.format("Deal %d damage.", BASE_DAMAGE);
    }

    @Override
    public int cost() {
        return 1;
    }
}
