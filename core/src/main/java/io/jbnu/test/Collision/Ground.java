package io.jbnu.test.Collision;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ground {
    private Vector2 position;
    private Texture texture;
    private Rectangle bound;
    private static int width = 500;
    private static int height = 60;

    public Ground(float x, float y, Texture texture) {
        this.position = new Vector2(x, y);
        this.texture = texture;

        bound = new Rectangle();
        bound.set(x,y,width,height);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Rectangle getBound(){
        return bound;

    }
}
