package io.jbnu.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ClearImage {
    private Vector2 position;
    private Texture texture;


    private static int width = 150;
    private static int height = 150;
    public ClearImage(float x, float y, Texture texture) {
        this.position = new Vector2(x, y);
        this.texture = texture;
    }
    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }
}
