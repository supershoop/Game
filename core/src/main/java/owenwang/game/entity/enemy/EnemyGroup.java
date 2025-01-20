package owenwang.game.entity.enemy;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class EnemyGroup extends Group {
    private static final float WIDTH_PROPORTION = 0.7f;
    private static final float OFFSET_PROPORTION = 0.02f;

    public List<Enemy> enemies = new ArrayList<>();

    public void processEnemy(Enemy e) {
        e.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        e.setFlipX(true);
    }

    public void addEnemy(Enemy e) {
        processEnemy(e);
        enemies.add(e);
    }

    public void update() {
        var size = WIDTH_PROPORTION * getWidth();
        var gap = OFFSET_PROPORTION * getWidth();
        if (enemies.isEmpty()) return;
        float totalWidth = size / 4 * enemies.size() + gap * (enemies.size() - 1);
        totalWidth -= gap;
        var x = (getWidth() - totalWidth) / 2;
        for (var e : enemies) {
            if (!e.isDescendantOf(this)) addActor(e);
            e.setSize(size/4, size);
            e.setPosition(x, 0);
            x += size / 4 + gap;
        }
    }
}
