package owenwang.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import owenwang.game.Assets;
import owenwang.game.Colors;
import owenwang.game.MainGame;
import owenwang.game.entity.enemy.Enemy;

public class IntentLabel extends WidgetGroup {
    private BitmapFont font;
    private final Label label = new Label("", MainGame.SKIN, "cardTitle");;
    private Enemy.Intent previousIntent;
    private Image image = new Image();
    public Enemy enemy;
    private TextTooltip tooltip;

    public IntentLabel(Enemy e) {
        this.enemy = e;
        addActor(image);
        addActor(label);
    }

    private void intentChanged() {
        previousIntent = enemy.intent;

        setTooltipText();
        if (enemy.intent == null)  image.setDrawable(null);
        else image.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture())));
    }

    private void setTooltipText() {
        String t;
        if (tooltip != null) {
            tooltip.hide();
            image.removeListener(tooltip);
        }
        tooltip = null;
        if (enemy.intent == null) return;
        else if (enemy.intent == Enemy.Intent.UNKNOWN) {
            t = "[BLACK]This enemy's intent is [PURPLE]unknown[BLACK].";
        }
        else if (enemy.intent == Enemy.Intent.STUN) {
            t = String.format("[BLACK]This enemy is [#%s]stunned[].", Colors.STUN.toString());
        }
        else {
            Color color;
            switch (enemy.intent) {
                case ATTACK: color = Colors.ATTACK; break;
                case DEFEND: color = Colors.ABILITY; break;
                case HEAL: color = Color.OLIVE; break;
                default: color = Color.DARK_GRAY; break;
            };
            String name;
            switch (enemy.intent) {
                case ATTACK: name = "[#%s]attack"; break;
                case DEFEND: name = "[#%s]defend"; break;
                case CAST: name = "apply an [#%s]effect"; break;
                case HEAL: name = "[#%s]heal"; break;
                default: name = ""; break;
            };
            t = String.format("[BLACK]This enemy intends to %s[BLACK].", String.format(name, color.toString()));
        }

        tooltip = new TextTooltip(t, MainGame.SKIN);
        image.addListener(tooltip);
        tooltip.setInstant(true);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
    }

    private Texture getTexture() {
        if (enemy.intent == null) return null;
        switch (enemy.intent) {
            case DEFEND: return Assets.manager.get(Assets.iconDefence2);
            case ATTACK: return Assets.manager.get(Assets.iconAttack);
            case CAST: return Assets.manager.get(Assets.iconCast);
            case HEAL: return Assets.manager.get(Assets.iconHeal);
            case STUN: return Assets.manager.get(Assets.iconStun);
            default: return Assets.manager.get(Assets.iconUnknown);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (enemy.intent != previousIntent) intentChanged();
        if (enemy.intent == Enemy.Intent.ATTACK) label.setText(enemy.intentValue);
        else label.setText("");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        var tSize = getWidth() * .25f;

        float totalWidth = tSize + label.getPrefWidth();

        var x = getWidth() / 2 - totalWidth / 2;

        image.setBounds(x, 0, tSize, tSize);

        label.setPosition(x + tSize, (tSize - label.getWidth()) / 2);
    }
}
