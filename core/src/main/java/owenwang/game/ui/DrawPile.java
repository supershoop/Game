package owenwang.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.MyStack;
import owenwang.game.card.Card;

import java.util.Stack;

public class DrawPile extends Group {
    public MyStack<Card> cards = new MyStack<>();
    private Label number = new Label("", MainGame.SKIN, "cardTitle");

    public DrawPile() {
        setTouchable(Touchable.enabled);
        addActor(number);
        setScale(1);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        number.setPosition(+ getWidth() * 0.8f,  getHeight() * 0.15f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        number.setText(Integer.toString(cards.size()));
        final var t = Assets.manager.get(Assets.drawPile, Texture.class);
        batch.draw(t, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, t.getWidth(), t.getHeight(), false, false);

        super.draw(batch, parentAlpha);
    }
}
