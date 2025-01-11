package owenwang.game.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.Constants;
import owenwang.game.MainGame;
import owenwang.game.Util;

public class CardActor extends Actor {
    public static final float CARD_WIDTH_SW_PROPORTION = 0.08f;
    public static final float CARD_HEIGHT_SW_PROPORTION = CARD_WIDTH_SW_PROPORTION * (4f/3);
    public static final float CARD_LARGE_SCALE = 1.25f;
    public static final float CARD_SMALL_SCALE = 0.75f;

    private static final float GLOW_WIDTH_SCALE = 1.2f;
    private static final float BORDER_WIDTH_SCALE = 1.1f;
    private static final float GLOW_FREQUENCY = 1.3f;
    private static TextureRegion energyOrb;
    private static TextureRegion energyOrbTexture() {
        if (energyOrb == null) {
            energyOrb = new TextureRegion(Assets.manager.get(Assets.orb, Texture.class), 0, 32*5, 32, 32);
        }
        return energyOrb;
    }

    private static final Color FONT_COLOR = new Color(0.9f, 0.85f, 0.85f, 1);
    private final MainGame g;
    public boolean ignoreEnergyCost = false;
    public Card card;
    private int titleFontSize = 0;
    private int descriptionFontSize = 0;
    private BitmapFont titleFont;
    private BitmapFont descriptionFont;
    private float stateTime;
    private boolean armed = false;

    public void setArmed(boolean armed) {
        if (armed != this.armed) stateTime = 0;
        this.armed = armed;
    }

    public boolean isArmed() {
        return armed;
    }

    public CardActor(MainGame g, Card card) {
        this.g = g;
        this.card = card;
    }

    @Override
    protected void sizeChanged() {
        titleFontSize = (int) (getHeight() / 8);
        if (titleFontSize < 11) {
            titleFont = null;
        } else {
            titleFont = Assets.getBoldFont(titleFontSize);
        }
        descriptionFontSize = (int) (getHeight() / 12);
        if (descriptionFontSize < 11) {
            descriptionFont = null;
        } else {
            descriptionFont = Assets.getFont(descriptionFontSize);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        var color = getColor();
        g.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        g.shapeRenderer.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        Util.glEnableTransparent();
        g.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (armed) {
            var glowPadding = getWidth() * (GLOW_WIDTH_SCALE - 1) / 2;
            g.shapeRenderer.setColor(1, 1, 0,(float) Math.abs(0.75f * Math.sin(Math.PI * stateTime * GLOW_FREQUENCY)));
            g.shapeRenderer.rect(getX() - glowPadding, getY() - glowPadding, getWidth() + 2 * glowPadding, getHeight() + 2 * glowPadding);
        }
        var padding = getWidth() * (BORDER_WIDTH_SCALE - 1) / 2;

        if (card instanceof AttackCard) g.shapeRenderer.setColor(Constants.ATTACK);
        else if (card instanceof AbilityCard) g.shapeRenderer.setColor(Constants.ABILITY);

        g.shapeRenderer.rect(getX() - padding, getY() - padding, getWidth() + 2 * padding, getHeight() + 2 * padding);
        g.shapeRenderer.end();
        Util.glDisableTransparent();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.begin();

        Texture bg = Assets.manager.get(Assets.cardBackground);
        batch.draw(bg, getX(), getY(), getWidth(), getHeight());
        Texture a = Assets.manager.get(card.getArt());
        int margin = (int) (getWidth() * 0.05);
        int artWidth = (int) (getWidth() - 2 * margin);
        int artHeight = (int) (((double) a.getHeight() / a.getWidth()) * artWidth);

        // ORB

        float energyOrbDiameter =  getWidth() * 0.4f;
        float offset = energyOrbDiameter * 0.4f;
        var orbX = getX() + getWidth() - energyOrbDiameter + offset;
        var orbY = getY() + getHeight() - energyOrbDiameter + offset;
        batch.draw(energyOrbTexture(), orbX, orbY, energyOrbDiameter, energyOrbDiameter);

        // TITLE
        var y = getY() + getHeight() - margin;
        if (titleFont != null) {
            var layout = new GlyphLayout(titleFont, card.getName(), FONT_COLOR, getWidth(), Align.center, false);
            titleFont.draw(batch, layout, getX(), y);

            layout = new GlyphLayout(titleFont, Integer.toString(card.cost()),
                ignoreEnergyCost || g.player.energy >= card.cost() ? FONT_COLOR : Color.RED,
                energyOrbDiameter, Align.center, false);
            titleFont.draw(batch, layout, orbX, orbY + energyOrbDiameter - (energyOrbDiameter - layout.height) / 2);
        }

        // ART
        y -= margin + artHeight + titleFontSize;
        batch.draw(a, getX() + margin, y,  artWidth, artHeight);

        // DESC

        if (descriptionFont != null) {
            var layout = new GlyphLayout(descriptionFont, card.getDescription(), FONT_COLOR, getWidth() - 2 * margin, Align.center, true);
            y = getY() + ((y - getY() - layout.height) / 2) + layout.height;
            descriptionFont.draw(batch, layout, getX() + margin, y);
        }

    }
}
