package owenwang.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.util.List;
import java.util.stream.Stream;

public final class Assets {
    public static final String playerIdle = "characters/soldier/Soldier with shadows/Soldier-Idle.png";
    public static final String playerSwing = "characters/soldier/Soldier with shadows/Soldier-Attack01.png";
    public static final String playerHurt = "characters/soldier/Soldier with shadows/Soldier-Hurt.png";
    public static final String orcIdle = "characters/Orc/Orc with Shadows/Orc-Idle.png";
    public static final String orcHurt = "characters/Orc/Orc with Shadows/Orc-Hurt.png";
    public static final String orcSwing = "characters/Orc/Orc with Shadows/Orc-Attack01.png";
    public static final String orcDeath = "characters/Orc/Orc with Shadows/Orc-Death.png";
    public static final String cardSlash = "cards/active1.png";
    public static final String cardShield = "cards/active6.png";
    public static final String cardBackground = "cards/background.png";
    public static final String iconDefence = "defence.png";
    public static final String iconAttack = "iconAttack.png";
    public static final String boldFont = "NoticiaText-Bold.ttf";
    public static final String font = "NoticiaText-Regular.ttf";
    public static final String orb = "orb.png";
    public static final String drawPile = "drawPile.png";
    public static final String discardPile = "discardPile.png";
    public static final AssetManager manager = new AssetManager();

    public static void load() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        final List<String> l = List.of(
            playerIdle,
            playerSwing,
            playerHurt,
            cardSlash,
            cardShield,
            cardBackground,
            orb,
            orcIdle,
            orcHurt,
            orcDeath,
            orcSwing,
            drawPile,
            discardPile,
            iconAttack,
            iconDefence
        );
        for (var t : l) {
            manager.load(t, Texture.class);
        }
    }

    public static TextureRegion[] linearSplit(Texture texture, int r, int c) {
        return Stream.of(TextureRegion.split(texture, texture.getWidth() / c,
            texture.getHeight() / r)).flatMap(Stream::of).toArray(TextureRegion[]::new);
    }

    public static BitmapFont getBoldFont(int size) {
        var name = String.format("bold%d.ttf", size);
        if (manager.contains(name)) return manager.get(name);
        var p = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        p.fontParameters.size = size;
        p.fontFileName = boldFont;
        p.fontParameters.borderColor = Color.BLACK;
        p.fontParameters.borderWidth = (float) size / 10;
        manager.load(name, BitmapFont.class, p);
        return manager.finishLoadingAsset(name);
    }

    public static BitmapFont getFont(int size) {

        var name = String.format("normal%d.ttf", size);
        if (manager.contains(name)) return manager.get(name);
        var p = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        p.fontParameters.size = size;
        p.fontFileName = font;
        manager.load(name, BitmapFont.class, p);
        return manager.finishLoadingAsset(name);
    }
}
