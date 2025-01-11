package owenwang.game.card;

import owenwang.game.MainGame;
import owenwang.game.entity.Player;
import owenwang.game.entity.enemy.Enemy;

public abstract class Card {
    public enum PlayResult {
        DISCARD;
    }
    protected MainGame g;
    public Card(MainGame g) {
        this.g = g;
    }
    public abstract String getArt();
    public abstract String getName();
    public abstract String getDescription();
    public abstract int cost();
    public PlayResult result() { return PlayResult.DISCARD; }

    public void begin(Enemy target) {
        getPlayer().energy -= cost();
    }

    public void play(Enemy target) {}

    protected Player getPlayer() {
        return g.player;
    }
}
