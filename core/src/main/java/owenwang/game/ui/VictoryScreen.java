package owenwang.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.card.CardActor;
import owenwang.game.card.Cards;

import java.util.List;

public class VictoryScreen extends Table {
    private Label label = new Label("Level Cleared", MainGame.SKIN);
    private CardSelect select = new CardSelect(2);
    private TextButton continueButton = new TextButton("Skip", MainGame.SKIN);

    public VictoryScreen() {
        row();
        label.setAlignment(Align.center);
        label.setStyle(MainGame.SKIN.get("title", Label.LabelStyle.class));
        add(label).growX().pad(10);
        row();
        add(new Label("Add 2 cards to your deck", MainGame.SKIN.get("subtitle", Label.LabelStyle.class)));
        row();
        add(select).growX().pad(10);
        row();
        add(continueButton).pad(10);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!continueButton.isDisabled()) {
                    _continue();
                }
            }
        });

    }

    private void _continue() {
        var selection = List.copyOf(select.selection);
        for (var card : selection) {
            MainGame.INSTANCE.addToDeck(select.removeCard(card, false));
        }
        continueButton.setDisabled(true);
        MainGame.INSTANCE.nextEncounter();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        continueButton.setDisabled(!visible);
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
    public void act(float delta) {
        super.act(delta);

        if (continueButton.isDisabled()) return;
        continueButton.setText(select.selection.isEmpty() ? "Skip Cards" : select.selection.size() == 1 ? "Add 1 Card" : "Continue");
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        var h = super.hit(x, y, touchable);
        if (h != null && h.isDescendantOf(this)) return h;
        else if (isVisible()) return this;
        else return h;
    }

    public void generateRewards() {
        select.clearCards();
        select.addCard(Cards.randomCards(3).stream().map(CardActor::new).toArray(CardActor[]::new));
    }
}
