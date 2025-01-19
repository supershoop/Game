package owenwang.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Assets;
import owenwang.game.MainGame;
import owenwang.game.Timings;
import owenwang.game.card.Card;
import owenwang.game.card.CardActor;
import owenwang.game.card.Cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CardList extends ScrollPane {
    private int maxSelections;
    private final ArrayList<CardActor> _selection = new ArrayList<>();
    public final List<CardActor> selection = Collections.unmodifiableList(_selection);
    public Collection<Card> source;
    private Table table = new Table(MainGame.SKIN);
    public String title = "";
    public boolean sorted = true;

    public CardList(Collection<Card> source) {
        super(null, MainGame.SKIN);
        setActor(table);
        this.source = source;
        setVariableSizeKnobs(false);
        setFadeScrollBars(false);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget().isDescendantOf(table)) return;
                MainGame.INSTANCE.closeLists();
            }
        });
    }

    public void update() {
        table.clear();
        var COLUMNS = 5;
        if (source == null) return;
        table.add(title, "title").colspan(COLUMNS).padTop(.5f * Cards.width()).getActor().setAlignment(Align.center);
        if (sorted) {
            table.row();
            table.add("Cards shown in order of type and name.", "subtitle").colspan(COLUMNS).padBottom(.5f * Cards.width()).getActor().setAlignment(Align.center);
        }
        int i = 0;
        var list = new ArrayList<>(source);
        table.align(Align.top);
        if (sorted) Collections.sort(list);
        for (var card : list) {
            if (i % COLUMNS == 0) {
                table.row();
            }
            var actor = new CardActor(card);
            actor.ignoreEnergyCost = true;
            actor.setWidth(Cards.width());
            table.add(actor).pad(Cards.width() * .2f);
            i++;
        }
        if (list.isEmpty()) {
            table.row();
            table.add("(Empty)", "subtitle").colspan(5);
        }
        if (hasParent()) {
            getStage().setScrollFocus(this);
            setScrollPercentY(0);
            layout();
        }

    }

}
