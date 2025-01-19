package owenwang.game.card;

import owenwang.game.Assets;

public class SlimeCard extends Card {
    @Override
    public String art() {
        return Assets.cardSlime;
    }

    @Override
    public String name() {
        return "Slime";
    }

    @Override
    public String description() {
        return "[LIGHT_GRAY]Exhaust.";
    }

    @Override
    public int cost() {
        return 1;
    }

    @Override
    public Type type() {
        return Type.CURSE;
    }

    @Override
    public PlayResult result() {
        return PlayResult.EXHAUST;
    }
}
