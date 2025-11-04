
package io.jbnu.test.Level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.jbnu.test.Collision.Block;
import io.jbnu.test.Collision.CoinObject;
import io.jbnu.test.Collision.Flag;
import io.jbnu.test.GameCharacter;

public abstract class LevelBase {

    public abstract Array<Block> getBlocks();
    public abstract void render(SpriteBatch batch);
    public abstract int getLevel();
    public abstract boolean isCleared(GameCharacter player);
    public abstract void dispose();
    public void update(float delta){}
    public boolean isTimeOver(){
        return false;
    }

}
