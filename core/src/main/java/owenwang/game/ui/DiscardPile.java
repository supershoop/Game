package owenwang.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import owenwang.game.Assets;
import owenwang.game.card.Card;

import java.util.Stack;

public class DiscardPile extends Actor {
    public Stack<Card> cards = new Stack<>();
    private BitmapFont font;
    private int fontSize;

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        fontSize = (int) (getHeight() * .6);
        if (fontSize > 9) {
            font = Assets.getFont(fontSize);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (font == null) return;
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(Assets.manager.get(Assets.discardPile, Texture.class), getX(), getY(), getWidth(), getHeight());
        var gl = new GlyphLayout(font, Integer.toString(cards.size()));
        font.draw(batch, gl, getX() + getWidth() * 0.2f - gl.width, getY() + getHeight() * 0.2f);
    }
}
