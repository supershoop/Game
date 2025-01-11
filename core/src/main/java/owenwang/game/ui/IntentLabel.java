package owenwang.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;

public class IntentLabel extends Actor {
    private BitmapFont font;
    private int fontSize;
    public Enemy enemy;

    public IntentLabel(Enemy e) {
        this.enemy = e;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        fontSize = (int) (getWidth() * .03f);
        if (fontSize > 9) {
            font = Assets.getFont(fontSize);
        }
    }

    private Texture getTexture() {
        if (enemy.intent == null) return null;
        return switch (enemy.intent) {
            case DEFEND -> Assets.manager.get(Assets.iconDefence);
            case ATTACK -> Assets.manager.get(Assets.iconAttack);
            default -> null;
        };
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        var t = getTexture();
        var tSize = getWidth() * .05f;
        var gap = getWidth() * .01f;

        float totalWidth = 0;

        GlyphLayout layout = null;
        if (font != null && enemy.intent == Enemy.Intent.ATTACK) {
            layout = new GlyphLayout(font, Integer.toString(enemy.intentValue));
            totalWidth += layout.width;
        }

        if (t != null) {
            totalWidth += tSize + gap;
        }

        var x = getX() + getWidth() / 2 - totalWidth / 2;

        if (t != null) {
            batch.draw(getTexture(), x, getY(), tSize, tSize);
        }

        if (layout != null) {
            font.draw(batch, layout, x + totalWidth - layout.width, getY() + fontSize);
        }
    }
}
