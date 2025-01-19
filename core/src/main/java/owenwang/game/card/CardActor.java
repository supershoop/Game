package owenwang.game.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.Colors;
import owenwang.game.MainGame;
import owenwang.game.Util;

public class CardActor extends Group {
    static final float CARD_WIDTH_SW_PROPORTION = 0.08f;
    static final float CARD_HEIGHT_SW_PROPORTION = CARD_WIDTH_SW_PROPORTION * (4.5f/3);
    public static final float CARD_LARGE_SCALE = 1.25f;
    public static final float CARD_SMALL_SCALE = 0.75f;

    private static final float GLOW_WIDTH_SCALE = 1.2f;
    private static final float BORDER_WIDTH_SCALE = 1.1f;
    private static final float GLOW_FREQUENCY = 1.3f;
    private Label title;
    private Label description;
    private Label type;
    private Image art;
    private Image energyOrb;
    private Label energyLabel;
    private static final Color FONT_COLOR = new Color(0.9f, 0.85f, 0.85f, 1);
    public boolean ignoreEnergyCost = false;
    public Card card;
    public float stateTime;
    private boolean armed = false;
    private boolean previousStrength = false;

    private float typeX;
    private float typeY;
    private float typeW;
    private float typeH;

    public void setArmed(boolean armed) {
        if (armed != this.armed) stateTime = 0;
        this.armed = armed;
    }

    public boolean isArmed() {
        return armed;
    }

    private String getType() {
        switch (card.type()) {
            case ABILITY: return "Ability";
            case ATTACK: return "Attack";
            case CURSE: return "Curse";
            default: return null;
        }
    }

    public CardActor(Card card) {
        this.card = card;
        this.energyOrb = new Image(Assets.manager.get(Assets.orbHighlight, Texture.class));
        this.energyLabel = new Label(Integer.toString(card.cost()), MainGame.SKIN.get("cardTitle", Label.LabelStyle.class));
        energyLabel.setAlignment(Align.center);
        this.title = new Label(card.name(), MainGame.SKIN.get("cardTitle", Label.LabelStyle.class));
        this.type = new Label(getType(), MainGame.SKIN);
        type.setColor(Colors.colorForType(card.type()).cpy().add(0.8f, 0.8f, 0.8f, 0f));
        title.setAlignment(Align.center);
        title.setWrap(true);
        title.setColor(FONT_COLOR);
        type.setAlignment(Align.center);
        this.description = new Label(card.description(), MainGame.SKIN);
        description.setAlignment(Align.center);
        description.setWrap(true);
        this.art = new Image(Assets.manager.get(card.art(), Texture.class));

        addActor(energyOrb);
        addActor(energyLabel);
        addActor(title);
        addActor(art);
        addActor(description);
        addActor(type);
    }

    @Override
    protected void sizeChanged() {
        float margin = (getWidth() * .05f);
        float orbSize = getWidth() * 0.35f;
        float insetX =  0.6f * orbSize;
        float insetY =  0.45f * orbSize;
        energyOrb.setBounds(getWidth() - insetX, getHeight() - insetY, orbSize, orbSize);
        energyLabel.setBounds(energyOrb.getX(), energyOrb.getY(), orbSize, orbSize);
        float titleHeight = getHeight() / 6;
        title.setBounds(0, getHeight() - titleHeight, getWidth(), titleHeight);
        var fontScale = card.titleFontScale();
        title.setFontScale(fontScale.x, fontScale.y);
        var artWidth = getWidth() - 2 * margin * getScaleX();
        var artHeight = (art.getPrefHeight() / art.getPrefWidth()) * artWidth;
        art.setBounds(margin * getScaleX(), getHeight() - title.getHeight() - artHeight, artWidth, artHeight);
        typeX = margin;
        typeY = getHeight() - titleHeight - artHeight - type.getHeight();
        typeW = getWidth() - 2 * margin;
        typeH = type.getHeight();

        type.setBounds(typeX, typeY, typeW, typeH);
        description.setBounds(margin, 0, getWidth() - 2 * margin, getHeight() - title.getHeight() - art.getHeight() - typeH);

        if (card.result() == Card.PlayResult.UNPLAYABLE) {
            energyLabel.setVisible(false);
            energyOrb.setColor(0.25f, 0.25f, 0.25f, 1f);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (MainGame.INSTANCE.player.isStrength() != previousStrength) {
            previousStrength = MainGame.INSTANCE.player.isStrength();
            description.setText(card.description());
        }
        energyLabel.setColor((ignoreEnergyCost || MainGame.INSTANCE.player.energy >= card.cost()) ? Color.WHITE : Color.RED);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var c = getColor();
        if (armed) {
            var glowPadding = getWidth() * (GLOW_WIDTH_SCALE - 1) / 2;
            batch.setColor(1, 1, 0,(float) Math.abs(0.75f * Math.sin(Math.PI * stateTime * GLOW_FREQUENCY)));
            Util.rect(batch, getX() - glowPadding * getScaleX(), getY() - glowPadding * getScaleY(),  getOriginX(), getOriginY(), getWidth() + 2 * glowPadding, getHeight() + 2 * glowPadding, getScaleX(), getScaleY(), getRotation());
        }

        var padding = getWidth() * (BORDER_WIDTH_SCALE - 1) / 2;

        var borderColor = Colors.colorForType(card.type());
        batch.setColor(borderColor.r * c.r, borderColor.g * c.g, borderColor.b * c.b, borderColor.a * c.a * parentAlpha);
        Util.rect(batch, getX() - padding * getScaleX(), getY() - padding * getScaleY(), getOriginX(), getOriginY(), getWidth() + 2 * padding, getHeight() + 2 * padding, getScaleX(), getScaleY(), getRotation());
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);

        Texture bg = Assets.texture(card.type() == Card.Type.CURSE ? Assets.cardBackgroundCurse : Assets.cardBackground);
        batch.draw(bg, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, bg.getWidth(), bg.getHeight(), false, false);

        var t = Assets.texture(Assets.cardType);
        batch.draw(t,
            getX() + typeX * getScaleX(), getY() + typeY * getScaleX(), getOriginX(), getOriginY(),
            typeW, typeH, getScaleX(), getScaleY(), getRotation(), 0, 0, t.getWidth(), t.getHeight(), false ,false);

        super.draw(batch, parentAlpha);

    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        super.setHeight(width * (4.5f/3));
    }
}
