package owenwang.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import owenwang.game.Timings;
import owenwang.game.card.CardActor;
import owenwang.game.card.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardSelect extends Table {
    private int maxSelections;
    private final ArrayList<CardActor> _selection = new ArrayList<>();
    public final List<CardActor> selection = Collections.unmodifiableList(_selection);
    private InputListener inputListener;
    public CardSelect(int maxSelections) {
        this.maxSelections = maxSelections;
        this.inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                cardClicked(getCard(event.getTarget()));
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) cardEnter(getCard(event.getTarget()));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) cardExit(getCard(event.getTarget()));
            }

            private CardActor getCard(Actor actor) {
                return actor instanceof CardActor ? (CardActor) actor : ((CardActor) actor.getParent());
            }
        };
    }

    public void addCard(CardActor... cards) {
        var cw = Cards.width();
        for (var card : cards) {
            card.ignoreEnergyCost = true;
            add(card).pad(cw*.2f);
            card.setWidth(cw);
            card.addListener(inputListener);
        }
    }

    public CardActor removeCard(CardActor card, boolean removeActor) {
        _selection.remove(card);
        if (removeActor) removeActor(card);
        card.removeListener(inputListener);
        return card;
    }

    public void clearCards() {
        for (var cell : getCells()) {
            if (cell.getActor() instanceof CardActor) removeCard(((CardActor) cell.getActor()), true);
        }
        clear();
    }

    private void cardClicked(CardActor card) {
        if (card.isArmed()) {
            card.setArmed(false);
            _selection.remove(card);
        } else if (_selection.size() < maxSelections || maxSelections == 1) {
            if (maxSelections == 1 && !selection.isEmpty()) {
                _selection.get(0).setArmed(false);
                _selection.clear();
            }
            card.setArmed(true);
            _selection.add(card);
            card.stateTime = _selection.get(0).stateTime;
        }
    }

    private void cardEnter(CardActor card) {
        card.setOrigin(Align.center);
        if (_selection.contains(card) || _selection.size() < maxSelections || maxSelections == 1) {
            card.clearActions();
            card.addAction(Actions.scaleTo(1.1f, 1.1f, Timings.CARD_HIGHLIGHT_TIME));
        }
    }

    private void cardExit(CardActor card) {
        card.setOrigin(Align.center);
        card.clearActions();
        card.addAction(Actions.scaleTo(1f, 1f, Timings.CARD_HIGHLIGHT_TIME));
    }

    public void clearSelection() {
        for (var card : _selection) {
            card.setArmed(false);
        }
        _selection.clear();
    }
}
