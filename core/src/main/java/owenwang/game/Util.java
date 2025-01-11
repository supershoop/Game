package owenwang.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.Random;

public class Util {
    public static final Random RANDOM = new Random();
    public static void glEnableTransparent() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void glDisableTransparent() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
