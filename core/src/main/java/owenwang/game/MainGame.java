package owenwang.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import owenwang.game.card.*;
import owenwang.game.levels.*;
import owenwang.game.entity.Entity;
import owenwang.game.entity.enemy.Enemy;
import owenwang.game.ui.*;
import owenwang.game.entity.Player;
import owenwang.game.entity.enemy.EnemyGroup;

import java.util.Collections;
import java.util.List;

/** {@link ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {
    public static final MainGame INSTANCE = new MainGame();

    public static Skin SKIN = null;
    public Stage stage;
    public ShapeRenderer shapeRenderer;
    public Hand hand;
    public EnemyGroup enemies;
    public Player player;
    public DrawPile drawPile;
    public DiscardPile discardPile ;
    private EnergyCounter energyCounter;
    private TextButton endTurnButton;
    private VictoryScreen victoryScreen;
    private CardList deckList;
    private CardList drawList;
    private CardList discardList;
    private final Rectangle modal = new Rectangle();
    private EndGameScreen endGame;
    public Widget actor = new Widget();
    public HUD hud;
    private Image deck;
    public Group ui;
    public final List<Level> levels = List.of(
        new TwoOrcLevel(),
        new CrabSlimeLevel(),
        new WitchLevel(),
        new GolemLevel()
    );
    public int level = 0;
    public int turn = 0; // Even is player, odd is enemy

    // State
    private int queuedCardDraws = 0;
    public int queuedOperations = 0;
    private boolean transferringCards = false;
    public float timeSinceCardDraw = 0;
    private boolean transitioning = false;
    private boolean endTurnQueued = false;
    private Image image;

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
            SKIN = new Skin(new TextureAtlas(Gdx.files.internal("gameSkin2/GameSkin.atlas")));
            Fonts.loadFontsIntoSkin(SKIN);
            SKIN.load(Gdx.files.internal("gameSkin2/GameSkin.json"));
            Colors.registerColors();
        }
        player = new Player();
        energyCounter = new EnergyCounter(this);
        endTurnButton = new TextButton("Enemy Turn", SKIN);
        drawPile = new DrawPile();
        int playerSize = (int) (Gdx.graphics.getWidth() * 0.5);
        player.setX(playerSize / 4f);
        player.setY(Gdx.graphics.getHeight() * -0.05f);
        player.setSize(playerSize / 4f, playerSize);
        hand = new Hand(this);
        stage.addActor(player);
        enemies = new EnemyGroup();
        enemies.setPosition(stage.getWidth() / 3, Gdx.graphics.getHeight() * -0.05f);
        enemies.setSize(stage.getWidth() * 2 / 3, stage.getHeight());
        stage.addActor(enemies);
        stage.addActor(hand);
        stage.addActor(energyCounter);
        stage.addActor(endTurnButton);
        hud = new HUD();
        stage.addActor(actor);
        actor.setSize(0, 0);
        image = new Image();
        stage.addActor(image);


        endTurnButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                if (endTurnButton.isDisabled()) return;
                endTurn();
            }
        });
        ui = new Group();
        ui.addActor(hud);
        victoryScreen = new VictoryScreen();
        ui.addActor(victoryScreen);
        endGame = new EndGameScreen();
        ui.addActor(endGame);
        endGame.setVisible(false);
        deck = new Image(Assets.texture(Assets.deck));
        discardPile = new DiscardPile();
        deckList = new CardList(player.deck);
        drawList = new CardList(drawPile.cards);
        discardList = new CardList(discardPile.cards);
        discardList.sorted = false;
        ui.addActor(deck);
        stage.addActor(ui);
        ui.addActor(modal);

        deckList.title = "Your Deck";
        drawList.title = "Draw Pile";
        discardList.title = "Discard Pile";

        ui.addActor(discardPile);
        ui.addActor(drawPile);
        modal.setVisible(false);
        modal.setColor(Color.BLACK);
        layout();
        Gdx.input.setInputProcessor(stage);

        deck.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleDeckList();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    deck.setOrigin(Align.center);
                    deck.addAction(Actions.scaleTo(1.25f, 1.25f, 0.15f, Interpolation.fastSlow));
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    deck.addAction(Actions.scaleTo(1f, 1f, 0.15f, Interpolation.fastSlow));
                }
            }
        });
        drawPile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleDrawList();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    drawPile.setOrigin(Align.center);
                    drawPile.addAction(Actions.scaleTo(1.25f, 1.25f, 0.15f, Interpolation.fastSlow));
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    drawPile.addAction(Actions.scaleTo(1f, 1f, 0.15f, Interpolation.fastSlow));
                }
            }
        });
        discardPile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleDiscardList();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    discardPile.setOrigin(Align.center);
                    discardPile.addAction(Actions.scaleTo(1.25f, 1.25f, 0.15f, Interpolation.fastSlow));
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    discardPile.addAction(Actions.scaleTo(1f, 1f, 0.15f, Interpolation.fastSlow));
                }
            }
        });
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (deckList.hasParent()) {
                        toggleDeckList();
                        return true;
                    }
                    if (drawList.hasParent()) {
                        toggleDrawList();
                        return true;
                    }
                    if (discardList.hasParent()) {
                        toggleDiscardList();
                        return true;
                    }
                }
                return false;
            }
        });

        initialGameState();
        level = 0;
        loadEncounter(levels.get(0));
        beginCombat();
    }

    public void initialGameState() {
        transitioning = false;
        queuedOperations = 0;
        queuedCardDraws = 0;
        endTurnQueued = false;
        level = -1;
        hud.stateTime = 0;
        hud.freeze = false;
        for (var e : enemies.enemies) {
            e.remove();
        }
        player.deck.clear();
        discardPile.cards.clear();
        drawPile.cards.clear();
        player.deck.addAll(List.of(
            new SlashCard(),
            new SlashCard(),
            new SlashCard(),
            new SlashCard(),
            new WhirlCard(),
            new ShieldCard(),
            new ShieldCard(),
            new ShieldCard(),
            new ShieldCard(),
            new ShieldCard()
        ));
        enemies.enemies.clear();
        player.initialState();

    }

    public void restart() {
        initialGameState();
        nextEncounter();
    }

    // 2 * CARD_ACTION_DELAY + CARD_MOVEMENT_TIME + 0.2
    public void addToDiscard(Card card) {
        CardActor ca = new CardActor(card);
        ca.setWidth(Cards.width());
        ca.setScale(CardActor.CARD_LARGE_SCALE);
        ui.addActorAt(0, ca);
        ca.setPosition((Gdx.graphics.getWidth() - ca.getWidth()) / 2, (Gdx.graphics.getHeight() - ca.getHeight()) / 2);
        ca.setOrigin(0);
        queuedOperations++;
        ca.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.delay(Timings.CARD_ACTION_DELAY),
            Actions.fadeIn(0.2f),
            Actions.delay(0.2f),
            Actions.run(() -> {
                queuedOperations--;
                discardCard(ca);
            })
        ));
    }

    public void onEnemyDied(float delay) {
        for (var enemy : enemies.getChildren()) {
            if (((Entity) enemy).healthBar.getHealth() > 0) return;
        }

        hand.disabled = true;
        endTurnButton.setDisabled(true);

        if (level == levels.size() - 1) endGame();
        else actor.addAction(Actions.delay(Math.max(delay, Timings.ENTITY_DIE_TIME), Actions.run(this::victory)));
    }

    public void closeLists() {
        if (drawList.hasParent()) {
            toggleDrawList();
        }
        if (discardList.hasParent())
            toggleDiscardList();
        if (deckList.hasParent())
            toggleDeckList();
    }

    public void victory() {
        closeLists();
        victoryScreen.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        victoryScreen.setVisible(true);
        endTurnButton.setDisabled(true);
        hand.disabled = true;
        victoryScreen.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.delay(Timings.ENTITY_DIE_TIME),
            Actions.fadeIn(Timings.PAGE_ANIMATION_TIME)
        ));
        victoryScreen.generateRewards();
        victoryScreen.setZIndex(ui.getChildren().size);
        deck.setZIndex(ui.getChildren().size);
    }

    public void endGame() {
        closeLists();
        actor.clearActions();
        hud.freeze = true;
        endGame.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        endGame.setVisible(true);
        endTurnButton.setDisabled(true);
        hand.disabled = true;
        endGame.update();
        endGame.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.fadeIn(2 * Timings.PAGE_ANIMATION_TIME)
        ));
        endGame.setZIndex(ui.getChildren().size + 1);
    }

    public void endTurn() {
        if (endTurnButton.isDisabled()) return;
        endTurnButton.setDisabled(true);
        endTurnQueued = true;
        hand.disabled = true;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        layout();
    }

    public void toggleDeckList() {
        toggleCardList(deckList);
        deck.setZIndex(ui.getChildren().size + 1);
    }

    public void toggleDrawList() {
        toggleCardList(drawList);
        drawPile.setZIndex(ui.getChildren().size + 1);
    }

    public void toggleDiscardList() {
        toggleCardList(discardList);
        discardPile.setZIndex(ui.getChildren().size + 1);
    }

    private void toggleCardList(CardList list) {
        if (list.hasParent()) {
            list.clearActions();
            modal.addAction(Actions.sequence(
                Actions.fadeOut(0.3f),
                Actions.visible(false))
            );
            list.addAction(
                Actions.sequence(
                    Actions.parallel(
                        Actions.moveTo(0, 0),
                        Actions.alpha(1),
                        Actions.delay(0.1f, Actions.fadeOut(0.2f)),
                        Actions.moveTo(0, stage.getHeight(), 0.3f, Interpolation.swingIn)
                    ),
                    Actions.removeActor()
                ));
        } else {
            modal.setZIndex(ui.getChildren().size + 1);
            ui.addActor(list);
            list.setBounds(0, 0, stage.getWidth(), stage.getHeight());
            list.update();
            list.clearActions();
            modal.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.alpha(0.9f, 0.3f)
            ));
            modal.setBounds(0, 0, stage.getWidth(), stage.getHeight());
            list.addAction(Actions.sequence(
                Actions.moveTo(0, stage.getHeight()),
                Actions.alpha(0),
                Actions.parallel(
                    Actions.fadeIn(0.3f),
                    Actions.moveTo(0, 0, 0.3f, Interpolation.swingOut)
                )
            ));
        }
        hud.setZIndex(ui.getChildren().size + 1);
    }

    @Override
    public void render() {
        if (Gdx.graphics.getWidth() == 0) return;
        float delta = Gdx.graphics.getDeltaTime();
        timeSinceCardDraw += delta;
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

        if (queuedCardDraws == 0 && timeSinceCardDraw >= Timings.CARD_MOVEMENT_TIME && queuedOperations == 0) {
            if (endTurnQueued) {
                nextTurn();
                endTurnQueued = false;
            } else if (transitioning && turn % 2 == 0) {
                _beginPlayerTurn();
                transitioning = false;
            } else if (transitioning) {
                transitioning = false;
                actor.addAction(Actions.delay(Timings.CARD_MOVEMENT_TIME, Actions.run(this::_beginEnemyTurn)));
            }
        }

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        stage.setDebugAll(Gdx.input.isKeyPressed(Input.Keys.GRAVE));
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH)) endGame();
    }

    public void nextEncounter() {
        level++;
        var rect = new Rectangle();
        ui.addActorAt(0, rect);
        rect.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        rect.setColor(0, 0, 0, 0);
        victoryScreen.addAction(Actions.fadeOut(Timings.PAGE_ANIMATION_TIME));
        endGame.addAction(Actions.fadeOut(Timings.PAGE_ANIMATION_TIME));
        rect.addAction(Actions.sequence(
            Actions.fadeIn(Timings.PAGE_ANIMATION_TIME),
            Actions.run(() -> loadEncounter(levels.get(level))),
            Actions.fadeOut(Timings.PAGE_ANIMATION_TIME),
            Actions.run(this::beginCombat),
            Actions.removeActor()
        ));
    }

    @Override
    public void dispose() {
        Assets.manager.dispose();
        stage.dispose();
    }

    private void layout() {
        var sw = Gdx.graphics.getWidth();
        var sh = Gdx.graphics.getHeight();
        final float marginX = sw * 0.02f;
        final float marginY = sw * 0.01f;
        final float size = sw * 0.03f;
        drawPile.setBounds(marginX, marginY, size, size);
        discardPile.setBounds(sw - marginX - size, marginY,  size, size);
        deck.setBounds(sw - marginX - size, sh - marginY - size, size, size);
        deck.setZIndex(ui.getChildren().size + 1);

        energyCounter.setBounds(sw * .08f, (Cards.height() - sw * .06f) / 2, sw * .06f, sw * .06f);
        endTurnButton.setPosition(sw * .92f - endTurnButton.getWidth(), (Cards.height() - endTurnButton.getHeight()) / 2);

        ui.setBounds(0, 0, sw, sh);
        hud.setPosition(marginX, sh - marginY - hud.getHeight());
        ui.setTouchable(Touchable.childrenOnly);
        victoryScreen.setBounds(0, 0, sw, sh);
        modal.setBounds(0, 0, sw, sh);

        hand.setPosition(0, 0);
        hand.setSize(sw, Cards.height());

    }

    private void beginCombat() {
        endTurnButton.setText("End Turn");
        turn = -1;
        drawPile.cards.clear();
        discardPile.cards.clear();
        Collections.shuffle(player.deck);
        victoryScreen.setVisible(false);
        endGame.setVisible(false);
        hand.disabled = true;
        endTurnButton.setDisabled(true);
        player.beginCombat();
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
        endTurnButton.setText("End Turn");
        endTurnButton.setDisabled(false);
        player.beginTurn();
        hand.disabled = false;
        for (var e : enemies.enemies) {
            e.decideIntent();
        }
    }

    private void _beginEnemyTurn() {
        endTurnButton.setText("Enemy Turn");
        var act = new SequenceAction();
        enemies.enemies.forEach(enemy -> {
            enemy.beginTurn();
            act.addAction(Actions.sequence(
                Actions.delay(enemy.preTurn()),
                Actions.run(enemy::playTurn),
                Actions.delay(enemy.postTurn() + 0.5f))
            );
        });
        act.addAction(Actions.run(this::nextTurn));
        actor.addAction(act);
    }

    public void drawCard() {
        queuedCardDraws++;
    }

    private void transferToDrawPile() {
        if (discardPile.cards.isEmpty()) return;
        transferringCards = true;
        float time = 0f;
        discardPile.cards.shuffle();

        var sw = Gdx.graphics.getWidth();
        var cw = Cards.width() * CardActor.CARD_SMALL_SCALE;
        float[] pointA = {sw * 0.9f, sw * 0.1f};
        float[] pointB = {sw * 0.1f, sw * 0.1f};
        float[] pointC = {drawPile.getX() + drawPile.getWidth() / 2, drawPile.getY() + drawPile.getHeight() / 2};

        for (int i = 0; !discardPile.cards.empty(); i++) {
            var actor = new CardActor(discardPile.cards.pop());
            actor.ignoreEnergyCost = true;
            ui.addActorAt(0, actor);
            actor.setPosition(discardPile.getX(), discardPile.getY());

            if (i == 0) time += Timings.CARD_TRANSFER_TIME;
            else time += Timings.CARD_TRANSFER_OFFSET;

            actor.setWidth(cw);
            actor.setScale(0);

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
                    Actions.scaleTo(1, 1, Timings.CARD_TRANSFER_TIME / 2, Interpolation.fastSlow),
                    Actions.scaleTo(0, 0, Timings.CARD_TRANSFER_TIME / 2, Interpolation.fastSlow)
                )
            )));
        }
        if (discardList.hasParent()) discardList.update();
        actor.addAction(Actions.delay(time + 0.2f, Actions.run(() -> this.transferringCards = false)));
    }

    private void _drawCard() {
        var card = drawPile.cards.pop();
        var actor = new CardActor(card);
        actor.setWidth(Cards.width());
        actor.setScale(0);
        actor.setPosition(drawPile.getX(), drawPile.getY());
        hand.addCard(actor);
        if (drawList.hasParent()) drawList.update();
    }

    public float discardCard(CardActor card) {
        queuedOperations++;
        hand.removeCard(card);
        ui.addActorAt(0, card);
        card.ignoreEnergyCost = true;
        card.setOrigin(Align.center);
        card.addAction(
            Actions.parallel(
                Actions.scaleTo(CardActor.CARD_SMALL_SCALE, CardActor.CARD_SMALL_SCALE, Timings.CARD_ACTION_DELAY, Interpolation.exp10Out),
                Actions.delay(Timings.CARD_ACTION_DELAY, Actions.parallel(
                    Actions.sequence(
                        Actions.scaleTo(0, 0, Timings.CARD_MOVEMENT_TIME, Interpolation.fastSlow),
                        Actions.run(() -> {
                            discardPile.cards.push(card.card);
                            if (discardList.hasParent()) discardList.update();
                            queuedOperations--;
                        }),
                        Actions.removeActor()
                    ),
                    Actions.moveTo(
                        discardPile.getX() + discardPile.getWidth() / 2 - card.getOriginX(),
                        discardPile.getY() + discardPile.getHeight() / 2 - card.getOriginY(),
                        Timings.CARD_MOVEMENT_TIME, Interpolation.fastSlow
                    ),
                    Actions.rotateBy(-90, 0.3f, Interpolation.fastSlow)
                ))
            )
        );
        return 0.65f;
    }

    public float exhaustCard(CardActor card) {
        queuedOperations++;
        hand.removeCard(card);
        ui.addActorAt(0, card);
        card.ignoreEnergyCost = true;
        card.addAction(Actions.delay(Timings.CARD_EXHAUST_DELAY, Actions.sequence(
                        Actions.fadeOut(Timings.CARD_EXHAUST_TIME),
                        Actions.run(() -> {
                            queuedOperations--;
                        }),
                        Actions.removeActor()
                    )
        ));
        return Timings.CARD_EXHAUST_TIME + Timings.CARD_EXHAUST_DELAY;
    }

    public float addToDeck(CardActor card) {
        var c = card.localToScreenCoordinates(new Vector2(card.getX(), card.getY()));
        ui.addActor(card);

        c = card.screenToLocalCoordinates(c);
        card.setPosition(c.x, c.y);
        card.setZIndex(99999);
        card.ignoreEnergyCost = true;
        player.deck.add(card.card);
        card.setOrigin(Align.bottomLeft);
        card.addAction(Actions.parallel(
                Actions.sequence(
                    Actions.scaleTo(0, 0, 0.5f, Interpolation.fastSlow),
                    Actions.removeActor()
                ),
                Actions.moveTo(deck.getX() + deck.getWidth() / 2, deck.getY() + deck.getHeight() / 2, 0.5f, Interpolation.fastSlow
                ),
                Actions.rotateBy(-90, 0.5f, Interpolation.fastSlow)
            )
        );
        return 0.65f;
    }

    public void loadEncounter(Level e) {
        discardPile.cards.clear();
        drawPile.cards.clear();
        var cards = List.copyOf(hand.getCards());
        for (var card : cards) {
            hand.removeCard(card);
            card.remove();
        }

        for (var enemy : e.getEnemies()) {
            enemies.addEnemy(enemy);
        }
        enemies.update();
        image.setDrawable(new TextureRegionDrawable(new TextureRegion(
            Assets.texture(e.getArt())
        )));

        image.setScaling(Scaling.fill);
        image.setAlign(Align.center);
        image.setZIndex(0);
        image.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
