package owenwang.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.List;
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

    public static void rect(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        batch.draw(Assets.manager.get(Assets.pixel, Texture.class), x, y, originX, originY, width, height, scaleX, scaleY, rotation, 0, 0, 1, 1, false, false);
    }

    public static <E> E getFirst(List<E> list) {
        return list.get(0);
    }

    public static <E> E getLast(List<E> list) {
        return list.get(list.size() - 1);
    }
}
