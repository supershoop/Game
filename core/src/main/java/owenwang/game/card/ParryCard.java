package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.entity.enemy.Enemy;

public class ParryCard extends Card {
    private static final int BASE_DAMAGE = 7;

    @Override
    public void begin(Enemy target) {
        super.begin(target);
        target.takeDamage(Gdx.input.isKeyPressed(Input.Keys.GRAVE) ? 1000 : damage(BASE_DAMAGE));
        attacked();
        MainGame.INSTANCE.queuedOperations++;
        MainGame.INSTANCE.actor.addAction(Actions.delay(Timings.CARD_ACTION_DELAY + 0.1f, Actions.run(() -> {
            MainGame.INSTANCE.queuedOperations--;
            MainGame.INSTANCE.drawCard();
        })));
    }

    @Override
    public String art() {
        return Assets.cardParry;
    }

    @Override
    public String name() {
        return "Parry";
    }

    @Override
    public String description() {
        return String.format("Deal %s damage.\nDraw 1 card.", damageStr(BASE_DAMAGE));
    }

    @Override
    public Type type() {
        return Type.ATTACK;
    }

    @Override
    public int cost() {
        return 1;
    }
}
