package owenwang.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.util.List;
import java.util.stream.Stream;

public final class Assets {
    public static final String playerIdle = "characters/soldier/Soldier/Soldier-Idle.png";
    public static final String playerSwing = "characters/soldier/Soldier/Soldier-Attack01.png";
    public static final String playerHurt = "characters/soldier/Soldier/Soldier-Hurt.png";
    public static final String playerDeath = "characters/soldier/Soldier/Soldier-Death.png";
    public static final String orcIdle = "characters/Orc/Orc/Orc-Idle.png";
    public static final String orcHurt = "characters/Orc/Orc/Orc-Hurt.png";
    public static final String orcSwing = "characters/Orc/Orc/Orc-Attack01.png";
    public static final String orcDeath = "characters/Orc/Orc/Orc-Death.png";
    public static final String crab = "characters/crab/crab.png";
    public static final String slime = "characters/slime/slime.png";
    public static final String golem = "characters/golem/golem.png";
    public static final String skull = "characters/skull/skull.png";
    public static final String witch = "characters/witch/sheet.png";
    public static final String cardSlash = "cards/active1.png";
    public static final String cardWhirl = "cards/active10.png";
    public static final String cardAnticipate = "cards/active15.png";
    public static final String cardShield = "cards/active6.png";
    public static final String cardWalkItOff = "cards/12.png";
    public static final String cardBreakThrough = "cards/active2.png";
    public static final String cardThousandCuts = "cards/active4.png";
    public static final String cardSlime = "cards/slime.png";
    public static final String cardSlam = "cards/active7.png";
    public static final String cardWarCry = "cards/active9.png";
    public static final String cardConclude = "cards/active11.png";
    public static final String cardSeverSoul = "cards/active5.png";
    public static final String cardFortitude = "cards/active14.png";
    public static final String cardAdrenaline = "cards/passive5.png";
    public static final String cardFortify = "cards/active13.png";
    public static final String cardCriticalStrike = "cards/active12.png";
    public static final String cardShattered = "cards/active3a.png";
    public static final String cardFury = "cards/active8.png";
    public static final String cardParry = "cards/passive1.png";
    public static final String cardStrength = "cards/passive4.png";
    public static final String cardBulwark = "cards/passive2.png";
    public static final String cardType = "cardType.png";
    public static final String cardBackground = "cards/background.png";
    public static final String cardBackgroundCurse = "cards/backgroundCurse.png";
    public static final String iconDefence = "defence.png";
    public static final String iconDefence2 = "shieldSmall.png";
    public static final String iconAttack = "iconAttack.png";
    public static final String iconUnknown = "iconUnknown.png";
    public static final String iconCast = "iconCast.png";
    public static final String iconHeal = "iconHeal.png";
    public static final String iconStun = "iconStun.png";
    public static final String boldFont = "NoticiaText-Bold.ttf";
    public static final String font = "NoticiaText-Regular.ttf";
    public static final String orb = "orb.png";
    public static final String orbHighlight = "orbHighlight.png";
    public static final String drawPile = "drawPile.png";
    public static final String discardPile = "discardPile.png";;
    public static final String deck = "deck.png";
    public static final String pixel = "square.png";
    public static final String timer = "timer.png";
    public static final String enemy = "enemy.png";
    public static final AssetManager manager = new AssetManager();
    public static final String heart = "heart.png";
    public static final String level0 = "background/level0.png";
    public static final String level1 = "background/level1.jpg";
    public static final String level2 = "background/level2.jpg";
    public static final String level3 = "background/level3.jpg";
    public static final String statusStrength = "status/strength.png";
    public static final String statusBulwark = "status/bulwark.png";
    public static final String statusFortitude = "status/fortitude.png";

    public static void load() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        final List<String> pixelArt = List.of(
            playerIdle,
            playerSwing,
            playerHurt,
            playerDeath,
            crab,
            skull,
            slime,
            orcIdle,
            orcHurt,
            orcDeath,
            orcSwing,
            golem,
            pixel,
            witch
        );
        final List<String> art = List.of(
            cardSlash,
            cardShield,
            cardBreakThrough,
            cardWhirl,
            cardType,
            cardThousandCuts,
            cardWalkItOff,
            cardAnticipate,
            cardSlime,
            cardBackground,
            cardBackgroundCurse,
            cardSlam,
            cardWarCry,
            cardShattered,
            cardConclude,
            cardSeverSoul,
            cardFortitude,
            cardAdrenaline,
            cardFortify,
            cardCriticalStrike,
            cardFury,
            cardParry,
            cardStrength,
            cardBulwark,
            orb,
            iconUnknown,
            orbHighlight,
            drawPile,
            discardPile,
            iconAttack,
            iconDefence2,
            iconDefence,
            iconHeal,
            iconCast,
            iconStun,
            deck,
            timer,
            enemy,
            heart,
            level0,
            level1,
            level2,
            level3,
            statusStrength,
            statusBulwark,
            statusFortitude
        );
        var param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        for (var t : art) {
            manager.load(t, Texture.class, param);
        }
        for (var t : pixelArt) {
            manager.load(t, Texture.class);
        }
    }

    public static TextureRegion[] linearSplit(Texture texture, int r, int c) {
        return Stream.of(TextureRegion.split(texture, texture.getWidth() / c,
            texture.getHeight() / r)).flatMap(Stream::of).toArray(TextureRegion[]::new);
    }

    public static Texture texture(String s) {
        return manager.get(s);
    }

    public static TextureRegion[] limit(int limit, TextureRegion... textures) {
        return Stream.of(textures).limit(limit).toArray(TextureRegion[]::new);
    }
}
