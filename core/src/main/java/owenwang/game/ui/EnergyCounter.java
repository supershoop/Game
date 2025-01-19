package owenwang.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.Fonts;
import owenwang.game.MainGame;

public class EnergyCounter extends Actor  {
    private MainGame g;

    private BitmapFont font;
    private int fontSize;
    private Texture texture;
    private float stateTime = 0;
    private static final Color borderColor = Color.valueOf("fbf122ff");

    public EnergyCounter(MainGame g) {
        this.g = g;
        texture = Assets.manager.get(Assets.orbHighlight, Texture.class);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        fontSize = (int) (getHeight() / 2);
        if (fontSize > 9) {
            font = Fonts.getBoldFont(fontSize);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (font == null) return;
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() - stateTime * 90f, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        font.draw(batch, Integer.toString(g.player.energy), getX(), getY() + fontSize + getHeight() / 6, getWidth(), Align.center, false);
        batch.end();
        Gdx.gl.glLineWidth(getWidth() * 0.045f);
        g.shapeRenderer.setColor(borderColor.mul(batch.getColor()));
        g.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        g.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        g.shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, getWidth() * .36f);
        g.shapeRenderer.end();
        batch.begin();
    }
}
