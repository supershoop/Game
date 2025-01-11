package owenwang.game.entity.enemy;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class EnemyGroup extends Group {
    private static final float WIDTH_PROPORTION = 0.5f;
    private static final float OFFSET_PROPORTION = 0.3f;

    public void addEnemy(Enemy e) {
        addActor(e);
        e.setFlipX(true);
        update();
        e.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));
    }

    private void update() {
        var size = WIDTH_PROPORTION * getWidth();
        var offset = OFFSET_PROPORTION * getWidth();
        float middle = getWidth() / 2;

        for (int i = 0; i < getChildren().size; i++) {
            var x = i%2==0 ? middle + offset * i/2 : middle - offset * i/2;
            var e = getChildren().get(i);
            e.setPosition(x, 0);
            e.setSize(size, size);
        }
    }
}
