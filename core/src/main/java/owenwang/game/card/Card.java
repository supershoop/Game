package owenwang.game.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import owenwang.game.MainGame;
import owenwang.game.entity.Player;
import owenwang.game.entity.enemy.Enemy;

import java.util.List;

public abstract class Card implements Comparable<Card> {
    public enum PlayResult {
        DISCARD, EXHAUST, UNPLAYABLE;
    }
    public enum Type {
        ATTACK, ABILITY, CURSE
    }

    protected int damage(int base) {
        return getPlayer().isStrength() ? 2 * base : base;
    }

    protected String damageStr(int base) {
        return String.format("[%s]%d[]", getPlayer().isStrength() ? "#" + Color.GREEN : "", damage(base));
    }

    public Card() {
    }
    public MainGame game() { return MainGame.INSTANCE; };
    public abstract String art();
    public abstract String name();
    public abstract String description();
    public abstract int cost();
    public Vector2 titleFontScale() { return new Vector2(1, 1); };
    public PlayResult result() { return PlayResult.DISCARD; }

    public void begin(Enemy target) {
        getPlayer().energy -= cost();
    }

    public void play(Enemy target) {}

    protected Player getPlayer() {
        return game().player;
    }

    protected void attacked() {
        getPlayer().swing();
        getPlayer().setStrength(false);
    }

    protected List<Enemy> enemies() {
        return MainGame.INSTANCE.enemies.enemies;
    }

    public abstract Type type();

    public boolean targeted() {
        return type() == Type.ATTACK;
    }

    @Override
    public int compareTo(Card o) {
        var c = type().compareTo(o.type());
        if (c != 0) return c;
        return name().compareTo(o.name());
    }
}
