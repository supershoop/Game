package owenwang.game;

public class Timings {
    public static final float CARD_MOVEMENT_TIME = 0.3f;
    public static final float END_TURN_TIME = 0.65f;
    public static final float CARD_TRANSFER_TIME = 0.7f;
    public static final float CARD_DRAW_INTERVAL = 0.3f;
    public static final float CARD_TRANSFER_OFFSET = 0.05f;
    public static final float CARD_HIGHLIGHT_TIME = 0.05f;
    public static final float ENTITY_DIE_TIME = 0.7f;
    public static final float PAGE_ANIMATION_TIME = 1f;
    public static final float CARD_ACTION_DELAY = 0.45f;
    public static final float CARD_EXHAUST_DELAY = 0.4f;
    public static final float CARD_EXHAUST_TIME = 0.75f;
    public static final float ADD_TO_DISCARD_TIME = 0.2f + Timings.CARD_MOVEMENT_TIME + 2 * Timings.CARD_ACTION_DELAY;
    public static final float STUN_TIME = 0f;
    public static final float INITIAL_TOOLTIP_TIME = 0.001f;
}
