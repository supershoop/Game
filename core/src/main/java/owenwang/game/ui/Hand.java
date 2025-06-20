package owenwang.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import owenwang.game.MainGame;
import owenwang.game.card.Card;
import owenwang.game.card.CardActor;
import owenwang.game.card.Cards;
import owenwang.game.entity.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Hand extends Group {
    private CardActor selected = null;
    private CardActor dragged = null;

    private List<CardActor> actors = new ArrayList<>();

    private static final float GAP_PROPORTION = 0.02f;

    private static final float SELECT_OFFSET_PROPORTION = 0.05f; // proportional to card height
    private final MainGame g;
    private Rectangle restingZone;
    public boolean disabled = false;

    public Hand(MainGame g) {
        this.g = g;
    }

    public List<CardActor> getCards() {
        return actors;
    }

    public void addCard(CardActor ca) {
        addActorAt(0, ca);
        actors.add(ca);
        ca.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (disabled || dragged != null || pointer != -1) return;
                selected = ca;
                update(0.1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (dragged == ca || selected != ca || pointer != -1) return;
                selected = null;
                update(0.1f);
            }
        });

        ca.addListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                if (disabled) {
                    cancel();
                    return;
                }
                dragged = ca;
                ca.setZIndex(99999);
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                selected = ca;
                dragged = ca;
                var offset = SELECT_OFFSET_PROPORTION * ca.getHeight();
                ca.clearActions();
                ca.addAction(Actions.moveTo(Gdx.input.getX() + offset - ca.getWidth(),
                    Gdx.graphics.getHeight() - Gdx.input.getY() - offset, 0.02f, Interpolation.exp5In));

                var mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                var armThreshold = Cards.height() * 0.95f;
                var rejectThreshold = Cards.height() * 2f;
                if (ca.card.result() == Card.PlayResult.UNPLAYABLE || (ca.card.cost() > g.player.energy)) {
                    if (mouseY > rejectThreshold) {
                        ca.setArmed(false);
                        cancel();
                        dragStop(null, 0, 0, 0);
                    }
                } else {
                    ca.setArmed(!disabled && mouseY > armThreshold);
                    if (ca.card.targeted()) targetEnemy();
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (ca.isArmed()) {
                    var e = targetEnemy();
                    ca.card.begin(e);
                    g.player.addAction(Actions.delay(0.15f, Actions.run(() -> ca.card.play(e))));
                    switch (ca.card.result()) {
                        case DISCARD: g.discardCard(ca); break;
                        case EXHAUST: g.exhaustCard(ca); break;
                    }
                }
                dragged = null;
                selected = null;
                update(0.15f);
                ca.setZIndex(0);
                ca.setArmed(false);
                targetEnemy(); // clear
            }
        });
        //Gdx.input.setInputProcessor(getStage());
        update(0.3f);
    }

    public void removeCard(CardActor ca) {
        if (!ca.isDescendantOf(this)) return;
        actors.remove(ca);
        ca.clearActions();
        ca.clearListeners();
    }

    private Enemy targetEnemy() {
        float minDist = Float.POSITIVE_INFINITY;
        Enemy closestEnemy = null;
        for (Actor e : g.enemies.getChildren()) {
            var enemy = (Enemy) e;
            enemy.targeted = false;
            if (enemy.healthBar.getHealth() == 0) continue;
            if (dragged != null && dragged.isArmed()) {
                var cardCoord = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                var eCoord = e.localToScreenCoordinates(new Vector2(e.getWidth() / 2, e.getHeight() / 2));
                var a = cardCoord.x - eCoord.x;
                var b = cardCoord.y - eCoord.y;
                var d = a * a + b * b;
                if (d < minDist) {
                    minDist = d;
                    closestEnemy = (Enemy) e;
                }
            }
        }
        if (closestEnemy != null) {
            closestEnemy.targeted = true;
        }
        return closestEnemy;
    }

    @Override
    protected void sizeChanged() {
        if (getWidth() == 0) return;
        update(0);
    }

    public void update(float duration) {
        if (actors.isEmpty()) return;
        float cw = Cards.width();
        float ch = Cards.height();

        float gap = getWidth() * GAP_PROPORTION;
        float totalWidth = cw * actors.size() + gap * (actors.size() - 1);
        restingZone = new Rectangle(0, 0, getWidth(), ch * 0.7f);
        if (selected != null) {
            totalWidth += cw * (CardActor.CARD_LARGE_SCALE  - 1);
        }
        var x = (getWidth() - totalWidth) / 2f;
        for (var c : actors) {
            c.setOrigin(Align.bottomLeft);
            var scale = c == selected ? CardActor.CARD_LARGE_SCALE : 1;
            if (dragged != c) {
                c.clearActions();
                c.addAction(Actions.parallel(
                    Actions.scaleTo(scale, scale, duration, Interpolation.exp5Out), Actions.moveTo(x, 0, duration, Interpolation.exp5Out)));
            }
            x += Cards.width() * scale + gap * scale;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
