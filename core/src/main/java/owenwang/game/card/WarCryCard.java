package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.Colors;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class WarCryCard extends Card {
    @Override
    public void play(Enemy target) {
        MainGame.INSTANCE.enemies.enemies.forEach(e -> e.intent = Enemy.Intent.STUN);
    }

    @Override
    public String art() {
        return Assets.cardWarCry;
    }

    @Override
    public String name() {
        return "War Cry";
    }

    @Override
    public String description() {
        return String.format("[#%s]Stun[] all enemies.", Colors.STUN.toString());
    }

    @Override
    public Type type() {
        return Type.ABILITY;
    }

    @Override
    public int cost() {
        return 2;
    }
}
