package owenwang.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Fonts {
    private Fonts() {}

    public static void loadFontsIntoSkin(Skin skin) {
        FreeTypeFontGenerator.setMaxTextureSize(2048);
        var title = getBoldFont(Gdx.graphics.getHeight() / 16);
        var subtitle = getFont(Gdx.graphics.getHeight() / 36);
        var cardTitle = getBoldFont(Gdx.graphics.getHeight() / 36);
        var regular = getFont(Gdx.graphics.getHeight() / 70);
        regular.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cardTitle.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("title", title);
        skin.add("subtitle", subtitle);
        skin.add("cardTitle", cardTitle);
        skin.add("regular", regular);
    }

    public static BitmapFont getBoldFont(int size) {
        var name = String.format("bold%d.ttf", size);
        if (Assets.manager.contains(name)) return Assets.manager.get(name);
        var p = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        p.fontParameters.size = size;
        p.fontFileName = Assets.boldFont;
        p.fontParameters.borderColor = Color.BLACK;
        p.fontParameters.borderWidth = (float) size / 10;
        Assets.manager.load(name, BitmapFont.class, p);
        BitmapFont f = Assets.manager.finishLoadingAsset(name);
        f.getData().markupEnabled = true;
        return f;
    }

    public static BitmapFont getFont(int size) {

        var name = String.format("normal%d.ttf", size);
        if (Assets.manager.contains(name)) return Assets.manager.get(name);
        var p = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        p.fontParameters.size = size;
        p.fontFileName = Assets.font;
        Assets.manager.load(name, BitmapFont.class, p);
        BitmapFont f = Assets.manager.finishLoadingAsset(name);
        f.getData().markupEnabled = true;
        return f;
    }
}
