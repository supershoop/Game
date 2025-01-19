package owenwang.game.card;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class FuryCard extends Card {
    public static final int VALUE = 15;
    @Override
    public void play(Enemy target) {
        target.takeDamage(damage(VALUE));
        attacked();
    }

    @Override
    public String art() {
        return Assets.cardFury;
    }

    @Override
    public String name() {
        return "Fury";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage.\n[LIGHT_GRAY]Exhaust.[]", damageStr(VALUE));
    }

    @Override
    public Type type() {
        return Type.ATTACK;
    }

    @Override
    public PlayResult result() {
        return PlayResult.EXHAUST;
    }

    @Override
    public int cost() {
        return 0;
    }
}
