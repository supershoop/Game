package owenwang.game;

import com.badlogic.gdx.graphics.Color;
import owenwang.game.card.Card;

public class Colors {
    public static final Color ATTACK = new Color(0.5f, 0f, 0f, 1);
    public static final Color ABILITY = Color.valueOf("168F9A");
    public static final Color DEFENCE = Color.valueOf("2daecf");
    public static final Color STUN = Color.valueOf("ffe61e");
    public static final Color STUN_DARK = Color.valueOf("c98c2f");
    public static final Color CURSE = Color.DARK_GRAY;

    public static Color colorForType(Card.Type type) {
        switch (type) {
            case ATTACK: return ATTACK;
            case ABILITY: return ABILITY;
            case CURSE: return CURSE;
            default: return Color.WHITE;
        }
    }

    public static void registerColors() {
        com.badlogic.gdx.graphics.Colors.put("ATTACK", ATTACK);
        com.badlogic.gdx.graphics.Colors.put("ABILITY", ABILITY);
        com.badlogic.gdx.graphics.Colors.put("DEFENCE", DEFENCE);
        com.badlogic.gdx.graphics.Colors.put("STUN", STUN);
        com.badlogic.gdx.graphics.Colors.put("STUN_DARK", STUN_DARK);
        com.badlogic.gdx.graphics.Colors.put("CURSE", CURSE);
    }

    private Colors() {}


}
