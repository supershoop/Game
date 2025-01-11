package owenwang.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import owenwang.game.card.CardActor;
import owenwang.game.card.ShieldCard;
import owenwang.game.card.ShieldCard2;
import owenwang.game.card.SlashCard;
import owenwang.game.entity.enemy.Enemy;
import owenwang.game.ui.DiscardPile;
import owenwang.game.ui.DrawPile;
import owenwang.game.ui.EnergyCounter;
import owenwang.game.entity.Player;
import owenwang.game.entity.enemy.EnemyGroup;
import owenwang.game.entity.enemy.OrcEnemy;
import owenwang.game.ui.Hand;

import java.util.Collections;
import java.util.List;

/** {@link ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {
    public static Skin SKIN = null;
    private Stage stage;
    public ShapeRenderer shapeRenderer;
    private Hand hand;
    public EnemyGroup enemies;
    public Player player;
    public DrawPile drawPile = new DrawPile();
    public DiscardPile discardPile = new DiscardPile();
    private EnergyCounter energyCounter;
    private TextButton endTurnButton;
    public int turn = 0; // Even is player, odd is enemy

    // State
    private int queuedCardDraws = 0;
    private boolean transferringCards = false;
    private float timeSinceCardDraw = 0;
    private float timeSinceCardDiscard = 0;
    private boolean transitioning = false;
    private boolean endTurnQueued = false;

    @Override
    public void create() {
        Assets.load();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        stage = new Stage();
        Assets.manager.finishLoading();
        shapeRenderer = new ShapeRenderer();
        initialize();
    }

    private void initialize() {
        if (SKIN == null) {
            SKIN = new Skin(Gdx.files.internal("gameSkin/gameSkin.json"), new TextureAtlas(Gdx.files.internal("gameSkin/gameSkin.atlas")));
        }
        player = new Player(this);
        energyCounter = new EnergyCounter(this);
        endTurnButton = new TextButton("End Turn", SKIN);
        int playerSize = (int) (Gdx.graphics.getWidth() * 0.5);
        player.setX(0);
        player.setY(0);
        player.setSize(playerSize, playerSize);
        hand = new Hand(this);
        stage.addActor(player);
        hand.setPosition(0, 0);
        hand.setSize(stage.getWidth(), stage.getHeight());
        player.deck.add(new SlashCard(this));
        player.deck.add(new SlashCard(this));
        player.deck.add(new SlashCard(this));
        player.deck.add(new SlashCard(this));
        player.deck.add(new SlashCard(this));
        player.deck.add(new SlashCard(this));
        player.deck.add(new ShieldCard(this));
        player.deck.add(new ShieldCard(this));
        player.deck.add(new ShieldCard(this));
        player.deck.add(new ShieldCard(this));
        player.deck.add(new ShieldCard(this));
        player.deck.add(new ShieldCard(this));
        player.deck.add(new ShieldCard2(this));
        enemies = new EnemyGroup();
        enemies.setPosition(0, 0);
        enemies.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(enemies);
        enemies.addEnemy(new OrcEnemy(this));
        enemies.addEnemy(new OrcEnemy(this));
        stage.addActor(drawPile);
        stage.addActor(discardPile);
        stage.addActor(hand);
        stage.addActor(energyCounter);
        stage.addActor(endTurnButton);
        endTurnButton.addListener(new InputListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (endTurnButton.isDisabled()) return false;
                endTurn();
                return false;
            }
        });

        layout();
        beginCombat();
        Gdx.input.setInputProcessor(stage);
    }

    private void endTurn() {
        endTurnButton.setDisabled(true);
        endTurnQueued = true;
        hand.disabled = true;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        hand.setPosition(0, 0);
        hand.setSize(width, height);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        timeSinceCardDraw += delta;
        timeSinceCardDiscard += delta;
        if (queuedCardDraws > 0 && timeSinceCardDraw >= Timings.CARD_DRAW_INTERVAL && !transferringCards) {
            if (!drawPile.cards.isEmpty()) {
                queuedCardDraws--;
                _drawCard();
                timeSinceCardDraw = 0;
            } else {
                transferToDrawPile();
            }
        }

        if (endTurnQueued && !hand.getCards().isEmpty()) {
            List<CardActor> handCards = List.copyOf(hand.getCards());

            for (var card : handCards) {
                discardCard(card);
            }
        }

        if (queuedCardDraws == 0 && timeSinceCardDraw >= Timings.CARD_MOVEMENT_TIME) {
            if (endTurnQueued) {
                nextTurn();
                endTurnQueued = false;
            } else if (transitioning && turn % 2 == 0) {
                _beginPlayerTurn();
                transitioning = false;
            } else if (transitioning) {
                transitioning = false;
                stage.addAction(Actions.delay(Timings.CARD_MOVEMENT_TIME, Actions.run(this::_beginEnemyTurn)));
            }
        }
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        Assets.manager.dispose();
        SKIN.dispose();
    }

    private void layout() {
        var sw = Gdx.graphics.getWidth();
        var sh = Gdx.graphics.getHeight();
        final float margin = sw * 0.01f;
        final float size = sw * 0.03f;
        drawPile.setBounds(margin, margin, size, size);
        discardPile.setBounds(sw - margin - size, margin,  size, size);

        energyCounter.setBounds(sw * .08f, sw * (CardActor.CARD_HEIGHT_SW_PROPORTION - .06f) / 2, sw * .06f, sw * .06f);
        endTurnButton.setPosition(sw * .92f - endTurnButton.getWidth(), ((sw * CardActor.CARD_HEIGHT_SW_PROPORTION) - endTurnButton.getHeight()) / 2);
    }

    private void beginCombat() {
        turn = -1;
        drawPile.cards.clear();
        discardPile.cards.clear();
        Collections.shuffle(player.deck);
        for (var card : player.deck) {
            drawPile.cards.push(card);
        }
        nextTurn();
    }

    private void nextTurn() {
        turn++;
        if (turn % 2 == 0) {
            transitioning = true;
            for (int i = 0; i < 5; i++) {
                drawCard();
            }
        } else {
            transitioning = true;
        }
    }

    private void _beginPlayerTurn() {
        endTurnButton.setDisabled(false);
        player.energy = 3;
        player.healthBar.setDefence(0);
        hand.disabled = false;
        for (var e : enemies.getChildren()) {
            ((Enemy) e).decideIntent(turn + 1);
        }
    }

    private void _beginEnemyTurn() {
        var act = new SequenceAction();
        for (var e : enemies.getChildren()) {
            Enemy enemy = (Enemy) e;
            enemy.beginTurn();
            act.addAction(Actions.delay(enemy.turnLength(), Actions.run(enemy::playTurn)));
            act.addAction(Actions.delay(0.7f));
        }
        act.addAction(Actions.run(this::nextTurn));
        stage.addAction(act);
    }

    public void drawCard() {
        queuedCardDraws++;
    }

    private void transferToDrawPile() {
        if (discardPile.cards.isEmpty()) return;
        transferringCards = true;
        float time = 0f;
        Collections.shuffle(discardPile.cards);

        var sw = Gdx.graphics.getWidth();
        var cw = CardActor.CARD_WIDTH_SW_PROPORTION * CardActor.CARD_SMALL_SCALE * sw;
        var ch = CardActor.CARD_HEIGHT_SW_PROPORTION * CardActor.CARD_SMALL_SCALE * sw;
        float[] pointA = {sw * 0.9f, sw * 0.1f};
        float[] pointB = {sw * 0.1f, sw * 0.1f};
        float[] pointC = {drawPile.getX() + drawPile.getWidth() / 2, drawPile.getY() + drawPile.getHeight() / 2};

        for (int i = 0; !discardPile.cards.empty(); i++) {
                var actor = new CardActor(this, discardPile.cards.pop());
            stage.addActor(actor);
            actor.setPosition(discardPile.getX(), discardPile.getY());

            if (i == 0) time += Timings.CARD_TRANSFER_TIME;
            else time += Timings.CARD_TRANSFER_OFFSET;

            actor.addAction(Actions.delay((i) * Timings.CARD_TRANSFER_OFFSET, Actions.parallel(
                Actions.sequence(
                    Actions.moveTo(pointA[0], pointA[1], Timings.CARD_TRANSFER_TIME / 3, Interpolation.fastSlow),
                    Actions.moveTo(pointB[0], pointB[1], Timings.CARD_TRANSFER_TIME / 3, Interpolation.fastSlow),
                    Actions.moveTo(pointC[0], pointC[1], Timings.CARD_TRANSFER_TIME / 3, Interpolation.fastSlow),
                    Actions.run(() -> {
                        drawPile.cards.push(actor.card);
                    }),
                    Actions.removeActor()
                ),
                Actions.sequence(
                    Actions.sizeTo(cw, ch, Timings.CARD_TRANSFER_TIME / 2, Interpolation.fastSlow),
                    Actions.sizeTo(0, 0, Timings.CARD_TRANSFER_TIME / 2, Interpolation.fastSlow)
                )
            )));
        }
        stage.addAction(Actions.delay(time + 0.2f, Actions.run(() -> this.transferringCards = false)));
    }

    private void _drawCard() {
        var card = drawPile.cards.pop();
        var actor = new CardActor(this, card);
        actor.setPosition(drawPile.getX(), drawPile.getY());
        hand.addCard(actor);
    }

    public float discardCard(CardActor card) {
        hand.removeCard(card);
        stage.getActors().insert(0, card);
        card.ignoreEnergyCost = true;
        card.addAction(
            Actions.parallel(
                Actions.sizeTo(Gdx.graphics.getWidth() * CardActor.CARD_WIDTH_SW_PROPORTION * CardActor.CARD_SMALL_SCALE,
                    Gdx.graphics.getWidth() * CardActor.CARD_HEIGHT_SW_PROPORTION * CardActor.CARD_SMALL_SCALE,
                    0.15f, Interpolation.fastSlow),
                Actions.delay(Constants.CARD_ACTION_DELAY + 0.2f, Actions.parallel(
                    Actions.sequence(
                        Actions.sizeTo(0, 0, 0.3f, Interpolation.fastSlow),
                        Actions.run(() -> discardPile.cards.push(card.card)),
                        Actions.removeActor()
                    ),
                    Actions.moveTo(
                        discardPile.getX() + discardPile.getWidth() / 2,
                        discardPile.getY() + discardPile.getHeight() / 2,
                        0.3f, Interpolation.fastSlow
                    )
                ))
            )
        );
        return 0.65f;
    }
}
