package owenwang.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.MainGame;

public class HUD extends Table {
    private final Image icon = new Image(Assets.texture(Assets.timer));
    private final Image enemy = new Image(Assets.texture(Assets.enemy));
    private static final Label label = new Label("8888:88:88", MainGame.SKIN, "cardTitle");
    private final Label fightCounter = new Label("", MainGame.SKIN, "cardTitle");
    private final Label turnIndicator = new Label("", MainGame.SKIN);
    public boolean freeze = false;

    public float stateTime = 0;

    public HUD() {
        var iconSize = label.getHeight() + turnIndicator.getHeight();
        align(Align.topLeft);
        var padding = label.getHeight() * 0.2f;
        add(icon).width(iconSize).height(iconSize).padRight(padding);
        add(label).width(label.getPrefWidth());
        add(enemy).width(iconSize).height(iconSize).padRight(padding);
        var t = new Table();
        t.add(fightCounter).align(Align.left).row();
        t.add(turnIndicator).align(Align.left);
        t.align(Align.left);
        add(t);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (freeze) return;
        stateTime += delta;

        label.setText(formatTime(stateTime));
        fightCounter.setText(String.format("%d/%d", MainGame.INSTANCE.level + 1, MainGame.INSTANCE.levels.size()));
        turnIndicator.setText(MainGame.INSTANCE.turn % 2 == 0 ? "Your turn" : "Enemy turn");
    }

    public static String formatTime(float time) {
        int minutes = (int) (time / 60);
        time %= 60;
        return String.format("%02d:%02d", minutes, (int) time);
    }
}
