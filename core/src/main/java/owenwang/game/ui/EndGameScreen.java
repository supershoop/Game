package owenwang.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.MainGame;

public class EndGameScreen extends Table {
    private final TextButton quitButton = new TextButton("Quit", MainGame.SKIN);
    private final TextButton restartButton = new TextButton("Restart", MainGame.SKIN);
    private final Label label = new Label("Title", MainGame.SKIN);
    private final Label timeLabel = new Label("00:00:00", MainGame.SKIN, "cardTitle");
    private final Label healthLabel = new Label("100", MainGame.SKIN, "cardTitle");
    private final Image timerIcon = new Image(Assets.texture(Assets.timer));
    private final Image healthIcon = new Image(Assets.texture(Assets.heart));


    public EndGameScreen() {
        super(MainGame.SKIN);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (quitButton.isDisabled()) return;
                restartButton.setDisabled(true);
                quitButton.setDisabled(true);
                _quit();
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (restartButton.isDisabled()) return;
                restartButton.setDisabled(true);
                quitButton.setDisabled(true);
                _restart();
            }
        });

    }

    private void _quit() {
        Gdx.app.exit();
    }

    private void _restart() {
        MainGame.INSTANCE.restart();;
    }

    public void update() {

        var health = MainGame.INSTANCE.player.healthBar.getHealth();
        clear();
        row();
        var size = timeLabel.getPrefHeight();
        label.setAlignment(Align.center);
        label.setStyle(MainGame.SKIN.get("title", Label.LabelStyle.class));
        var bigPadding = Gdx.graphics.getHeight() / 24;
        var smallPadding = Gdx.graphics.getHeight() / 100;
        add(label).pad(bigPadding).colspan(2);
        row();
        add(timerIcon).width(size).pad(smallPadding).align(Align.right).height(size);
        add(timeLabel).align(Align.left).row();
        if (health != 0) {
            add(healthIcon).pad(smallPadding).width(size).align(Align.right).height(size);
            add(healthLabel).align(Align.left).row();
            add(String.format("Your score: %d", calculateScore()), "subtitle").colspan(2).pad(smallPadding).row();
        }
        add(restartButton).pad(smallPadding).padTop(bigPadding);
        add(quitButton).pad(smallPadding).padTop(bigPadding);


        timeLabel.setText(HUD.formatTime(MainGame.INSTANCE.hud.stateTime));
        healthLabel.setText(health);
        label.setText(health > 0 ? "Victory!" : "Defeat...");

        quitButton.setDisabled(false);
        restartButton.setDisabled(false);
    }

    private int calculateScore() {
        double timeComponent = 400 / (Math.max(0.001, 0.1 * MainGame.INSTANCE.hud.stateTime / 60));
        double healthComponent = 500 * (MainGame.INSTANCE.player.healthBar.getHealth() / 75d);
        return (int) (timeComponent + healthComponent);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var c = getColor();
        batch.setColor(0, 0, 0, 0.9f * getColor().a * parentAlpha);
        batch.draw(Assets.manager.get(Assets.pixel, Texture.class), 0, 0, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, 1, 1, false, false);
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        super.draw(batch, parentAlpha);
    }
    @Override
    public Actor hit(float x, float y, boolean touchable) {
        var h = super.hit(x, y, touchable);
        if (h != null && h.isDescendantOf(this)) return h;
        else if (isVisible()) return this;
        else return h;
    }
}
