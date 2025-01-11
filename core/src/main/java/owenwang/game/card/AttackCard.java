package owenwang.game.card;

import owenwang.game.MainGame;

public abstract class AttackCard extends Card {
    public AttackCard(MainGame g) {
        super(g);
    }

    public boolean multiTarget() { return false; }


}
